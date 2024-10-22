package com.example.demo.mapper;

import com.example.demo.dto.request.TransactionDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Transaction;

public class TransactionMapper {

    public static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getOrder().getOrderId(),
                transaction.getTransactionDate(),
                transaction.getVnpTxnRef(),
                transaction.getTotalPrice()
        );
    }

    public static Transaction mapToTransaction(TransactionDTO transactionDTO, Order order) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDTO.getTransactionId());
        transaction.setOrder(order);
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        return transaction;
    }
}
