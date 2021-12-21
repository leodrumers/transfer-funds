package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;
    private final RestTemplate restTemplate;

    @Autowired
    public TransferController(TransferService transferService, RestTemplate restTemplate) {
        this.transferService = transferService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<List<TransferHistory>> getAll() {
        return ResponseEntity.ok().body(transferService.getAll());
    }

    @PostMapping("/transfer_funds")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferDto transfer) {
        return ResponseEntity.ok(transferService.transfer(transfer));
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponse> getRate() {
        String url = "http://api.exchangeratesapi.io/v1/latest?access_key=81d8c993d9ffac0f22d7e87e4e6a1bf0&base=EUR&symbols=USD,CAD";
        RateResponse rateResponse = restTemplate.getForObject(url, RateResponse.class);
        return ResponseEntity.ok(rateResponse);
    }
}
