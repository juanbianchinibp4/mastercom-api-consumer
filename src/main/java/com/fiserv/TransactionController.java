package com.fiserv;

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
    public ResponseEntity<String> executeTransactionSearch(@RequestBody TransactionSearch transactionSearch) {

        Object search = transactionService.executeTransactionSearch(transactionSearch);

        return ResponseEntity.ok(search.toString());

    }

}
