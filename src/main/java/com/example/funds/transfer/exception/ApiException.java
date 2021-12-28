package com.example.funds.transfer.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ApiException {
    private final String messsage;
    private final Throwable throwable;
    private final ZonedDateTime time;
}
