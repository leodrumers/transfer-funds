package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.exception.ApiRequestException;
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

    public Integer countTransfers(Long accountId) {
        LocalDateTime after = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime before = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return transferHistoryRepository.countTransferHistoryByOriginAccountAndTransferDateBeforeAndTransferDateAfterAndStatusEquals(
                accountId, before, after, TRANSFER_SUCCESS.label
        );
    }

    @Override
    public TransferResponse transfer(TransferDto transferDto) {
        List<String> errors = new ArrayList<>();
        TransferResponse response = new TransferResponse("", errors, BigDecimal.valueOf(0.0), null, null);
        response.setTaxCollected(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR));

        try {
            Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
            Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());
            getTransferStatus(transferDto);
            response.setStatus(TRANSFER_OK.label);
            BigDecimal taxes = getTaxes(transferDto.getAmount());
            AccountDto accountDto = originAccount.get();
            AccountDto destinationAccountDto = destinationAccount.get();

            accountDto = subtractFunds(accountDto, transferDto.getAmount());
            addFunds(destinationAccountDto, transferDto.getAmount());
            saveHistory(transferDto, TRANSFER_SUCCESS.label);

            response.setTaxCollected(taxes.setScale(2, RoundingMode.FLOOR));
            response.setCad(exchangeAmount(accountDto.getFunds(), accountDto.getCurrency()));
            response.setDescription(transferDto.getDescription());

        } catch (Exception e) {
            errors.add(e.getMessage());
            response.setStatus(ERROR.label);
            response.setTaxCollected(BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR));
        }

        response.setErrors(errors);
        return response;
    }

    @Override
    public RateResponse getExchangeRate() {
        String url = exchangeUrl + "latest?access_key=" + accessKey + "&base=EUR&symbols=USD,CAD";
        return restTemplate.getForObject(url, RateResponse.class);
    }

    private AccountDto subtractFunds(AccountDto accountDto, BigDecimal amount) {
        accountDto.setFunds(accountDto.getFunds().subtract(getDiscountWithTaxes(amount)).setScale(2, RoundingMode.FLOOR));
        return accountService.save(accountDto);
    }

    private AccountDto addFunds(AccountDto destinationAccount, BigDecimal amount) {
        destinationAccount.setFunds(destinationAccount.getFunds().add(amount).setScale(2, RoundingMode.FLOOR));
        return accountService.save(destinationAccount);
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
        try {
            String url = exchangeUrl + "latest?access_key=" + accessKey + "&base=EUR&symbols=CAD," + base;
            return restTemplate.getForObject(url, RateResponse.class);
        }catch (Exception e){
            throw new ApiRequestException(THIRD_SERVICE_ERROR.label);
        }
    }

    private BigDecimal exchangeAmount(BigDecimal amount, String base) {
        RateResponse rate = getRateResponse(base);
        BigDecimal usd = BigDecimal.valueOf(rate.getRates().get(base));
        BigDecimal cad = BigDecimal.valueOf(rate.getRates().get("CAD"));
        BigDecimal eur = amount.divide(usd, RoundingMode.FLOOR);
        return eur.multiply(cad);
    }

    public boolean isLimitExceeded(Long accountId) {
        Integer transfers = countTransfers(accountId);
        return transfers.compareTo(3) > -1;
    }

    public void getTransferStatus(TransferDto transferDto) {
        if (transferDto.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ApiRequestException(NEGATIVE_AMOUNT.label);
        }

        if (transferDto.getOriginAccount().equals(transferDto.getDestinationAccount())) {
            throw new ApiRequestException(SAME_ACCOUNT.label);
        }

        Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
        Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());

        if (originAccount.isEmpty() || destinationAccount.isEmpty()) {
            throw new ApiRequestException(ACCOUNT_NOT_FOUND.label);
        }

        if (isLimitExceeded(transferDto.getOriginAccount())) {
            throw new ApiRequestException(LIMIT_EXCEEDED.label);
        }

        if (!hasEnoughFunds(getDiscountWithTaxes(transferDto.getAmount()), originAccount.get())) {
            throw new ApiRequestException(INSUFFICIENT_FUNDS.label);
        }
    }

}
