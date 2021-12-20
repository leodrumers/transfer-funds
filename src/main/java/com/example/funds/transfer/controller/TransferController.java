package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public ResponseEntity<List<TransferHistory>> getAll() {
        return ResponseEntity.ok().body(transferService.getAll());
    }

    @PostMapping("/transfer_funds")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferDto transfer){
        return ResponseEntity.ok(transferService.transfer(transfer));
    }
}
