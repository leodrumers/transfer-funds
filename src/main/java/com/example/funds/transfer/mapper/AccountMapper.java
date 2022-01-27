package com.example.funds.transfer.mapper;

import com.example.funds.transfer.dto.AccountBalanceDto;
import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.entity.Account;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountDto.class})
public interface AccountMapper {

    @Mappings({
            @Mapping(source = "accountId", target = "id"),
            @Mapping(source = "currency.currency", target = "currency"),
            @Mapping(source = "funds", target = "funds"),
    })
    AccountDto toAccountDto(Account account);
    List<AccountDto> toAccountsDto(List<Account> accounts);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "transfers", ignore = true),
            @Mapping(target = "currency", ignore = true)
    })
    Account toAccount(AccountDto accountDto);
}
