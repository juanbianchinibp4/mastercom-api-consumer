package com.fiserv.api;

import com.fiserv.dto.TransactionSearchDTO;
import com.fiserv.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
        transactionController = new TransactionController(transactionService);
    }

    @Test
    public void executeTransactionSearch_givenCorrectData_shouldReturnSuccess() {

        TransactionSearchDTO transactionSearch = new TransactionSearchDTO(
                "12345678901234567890123", "2023-01-01", "2023-01-31");

        ResponseEntity<String> stringResponseEntity = transactionController.executeTransactionSearch(transactionSearch);

        assertTrue(stringResponseEntity.getStatusCode().is2xxSuccessful());

    }

}