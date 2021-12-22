package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferStatus;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.repositories.TransferHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.funds.transfer.entity.TransferStatus.*;
import static com.example.funds.transfer.utils.TaxesUtil.*;

@Service
public class TransferServiceImpl implements TransferService {

    private final AccountService accountService;
    private final RestTemplate restTemplate;
    private final TransferHistoryRepository transferHistoryRepository;

    @Value("${exchange.baseUrl}")
    private String exchangeUrl;

    @Value("${exchange.accessKey}")
    private String accessKey;

    @Autowired
    public TransferServiceImpl(TransferHistoryRepository transferHistoryRepository, AccountService accountService, RestTemplate restTemplate) {
        this.transferHistoryRepository = transferHistoryRepository;
        this.accountService = accountService;
        this.restTemplate = restTemplate;
    }


    @Override
    public List<TransferHistory> getAll() {
        return this.transferHistoryRepository.findAll();
    }

    @Override
    public Integer countTransfers(Long accountId) {
        LocalDateTime after = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime before = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return transferHistoryRepository.countTransferHistoryByOriginAccountAndTransferDateBeforeAndTransferDateAfterAndStatusEquals(
                accountId, before, after, TRANSFER_SUCCESS.label
        );
    }

    @Override
    public TransferResponse transfer(TransferDto transferDto) {
        TransferResponse response = new TransferResponse();
        List<String> errors = new ArrayList<>();
        response.setTaxCollected(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR));

        Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
        Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());

        TransferStatus transferStatus = getTransferStatus(transferDto);
        response.setStatus(transferStatus.label);
        if (transferStatus == TRANSFER_OK) {
            BigDecimal taxes = getTaxes(transferDto.getAmount());
            AccountDto accountDto = originAccount.get();
            AccountDto destinationAccountDto = destinationAccount.get();

            accountDto = subtractFunds(accountDto, transferDto.getAmount());
            addFunds(destinationAccountDto, transferDto.getAmount());
            saveHistory(transferDto, TRANSFER_SUCCESS.label);

            response.setTaxCollected(taxes.setScale(2, RoundingMode.FLOOR));
            response.setCad(exchangeAmount(accountDto.getFunds(), accountDto.getCurrency()));
            response.setDescription(transferDto.getDescription());
        }else {
            response.setStatus(TRANSFER_ERROR.label);
            errors.add(transferStatus.label);
        }

        response.setErrors(errors);
        return response;
    }

    @Override
    public RateResponse getExchangeRate() {
        String url = exchangeUrl + "latest?access_key=" + accessKey +"&base=EUR&symbols=USD,CAD";
        return restTemplate.getForObject(url, RateResponse.class);
    }

    private AccountDto subtractFunds(AccountDto accountDto, BigDecimal amount) {
        accountDto.setFunds(accountDto.getFunds().subtract(getDiscountWithTaxes(amount)).setScale(2, RoundingMode.FLOOR));
        return accountService.save(accountDto);
    }

    private AccountDto addFunds(AccountDto destinationAccount, BigDecimal amount) {
        destinationAccount.setFunds(destinationAccount.getFunds().add(amount).setScale(2, RoundingMode.FLOOR));
        return  accountService.save(destinationAccount);
    }

    private void saveHistory(TransferDto transferDto, String status) {
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setOriginAccount(transferDto.getOriginAccount());
        transferHistory.setDestinationAccount(transferDto.getDestinationAccount());
        transferHistory.setAmount(transferDto.getAmount());
        transferHistory.setStatus(status);
        transferHistory.setDescription(transferDto.getDescription());
        transferHistoryRepository.save(transferHistory);
    }

    private RateResponse getRateResponse(String base) {
        String url = exchangeUrl + "latest?access_key=" + accessKey + "&base=EUR&symbols=CAD," +base;
        return restTemplate.getForObject(url, RateResponse.class);
    }

    private BigDecimal exchangeAmount(BigDecimal amount, String base) {
        RateResponse rate = getRateResponse(base);
        BigDecimal usd = BigDecimal.valueOf(rate.getRates().get(base));
        BigDecimal cad = BigDecimal.valueOf(rate.getRates().get("CAD"));
        BigDecimal eur = amount.divide(usd, RoundingMode.FLOOR);
        return eur.multiply(cad);
    }

    @Override
    public boolean isLimitExceeded(Long accountId) {
        Integer transfers = countTransfers(accountId);
        return transfers.compareTo(3) > 0;
    }

    @Override
    public TransferStatus getTransferStatus(TransferDto transferDto) {
        if(transferDto.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            return TransferStatus.NEGATIVE_AMOUNT;
        }

        if(transferDto.getOriginAccount().equals(transferDto.getDestinationAccount())) {
            return TransferStatus.SAME_ACCOUNT;
        }

        Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
        Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());

        if(originAccount.isEmpty() || destinationAccount.isEmpty()) {
            return TransferStatus.ACCOUNT_NOT_FOUND;
        }

        if(isLimitExceeded(transferDto.getOriginAccount())){
            return TransferStatus.LIMIT_EXCEEDED;
        }

        if(!hasEnoughFunds(getDiscountWithTaxes(transferDto.getAmount()), originAccount.get())){
            return TransferStatus.INSUFFICIENT_FUNDS;
        }

        return TransferStatus.TRANSFER_OK;
    }

}
