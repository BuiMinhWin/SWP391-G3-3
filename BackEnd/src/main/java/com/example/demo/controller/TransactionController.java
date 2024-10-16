package com.example.demo.controller;

import com.example.demo.dto.request.TransactionDTO;
import com.example.demo.dto.request.UpdateStatusDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.service.iml.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = transactionService.createTransaction(transactionDTO.getOrderId());
        TransactionDTO savedTransactionDTO = TransactionMapper.mapToTransactionDTO(transaction);

        return new ResponseEntity<>(savedTransactionDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransactionStatus(@PathVariable String transactionId, @RequestBody UpdateStatusDTO updateStatusDTO) {
        Transaction updatedTransaction = transactionService.updateTransactionStatus(transactionId, updateStatusDTO.getNewStatus());
        TransactionDTO transactionDTO = TransactionMapper.mapToTransactionDTO(updatedTransaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }
}
