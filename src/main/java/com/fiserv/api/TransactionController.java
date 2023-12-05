package com.fiserv.api;

import bp4.mastercom_api_client.model.TransactionSummary;
import com.fiserv.dto.TransactionSearchDTO;
import com.fiserv.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/search")
    public ResponseEntity<String> executeTransactionSearch(@RequestBody TransactionSearchDTO transactionSearchDTO) {

        TransactionSummary search = transactionService.executeTransactionSearch(transactionSearchDTO);

        return ResponseEntity.ok(search.toString());

    }

}
