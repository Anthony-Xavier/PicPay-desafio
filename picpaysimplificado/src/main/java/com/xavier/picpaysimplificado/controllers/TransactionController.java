package com.xavier.picpaysimplificado.controllers;

import com.xavier.picpaysimplificado.domain.transaction.Transaction;
import com.xavier.picpaysimplificado.dto.TransactionDto;
import com.xavier.picpaysimplificado.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTrasaction(@RequestBody TransactionDto transaction) throws Exception {
        Transaction newTransaction = this.transactionService.createTransation(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}
