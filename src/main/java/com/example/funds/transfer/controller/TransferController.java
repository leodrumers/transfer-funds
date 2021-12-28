package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @GetMapping
    public ResponseEntity<List<TransferHistory>> getAll() {
        return ResponseEntity.ok().body(transferService.getAll());
    }

    @PostMapping("/transfer_funds")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferDto transfer) {
        TransferResponse response = transferService.transfer(transfer);
        if(!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponse> getRate() {
        return ResponseEntity.ok(transferService.getExchangeRate());
    }
}
