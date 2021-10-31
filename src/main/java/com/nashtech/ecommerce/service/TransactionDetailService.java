package com.nashtech.ecommerce.service;

import com.nashtech.ecommerce.domain.Cart;
import com.nashtech.ecommerce.domain.Transaction;
import com.nashtech.ecommerce.domain.TransactionDetail;
import com.nashtech.ecommerce.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
public class TransactionDetailService {
    private final TransactionRepository transactionRepository;

    public TransactionDetailService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //TODO: get the number of purchased products by productid
//    public int getPurchasedByProductId(int productId) {
//        transactionRepository.;
//    }
}
