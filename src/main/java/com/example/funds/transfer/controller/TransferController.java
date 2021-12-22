package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.service.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferServiceImpl transferServiceimpl;

    @Autowired
    public TransferController(TransferServiceImpl transferServiceimpl) {
        this.transferServiceimpl = transferServiceimpl;
    }

    @GetMapping
    public ResponseEntity<List<TransferHistory>> getAll() {
        return ResponseEntity.ok().body(transferServiceimpl.getAll());
    }

    @PostMapping("/transfer_funds")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferDto transfer) {
        TransferResponse response = transferServiceimpl.transfer(transfer);
        if(response.getErrors().isEmpty()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponse> getRate() {
        return ResponseEntity.ok(transferServiceimpl.getExchangeRate());
    }
}
