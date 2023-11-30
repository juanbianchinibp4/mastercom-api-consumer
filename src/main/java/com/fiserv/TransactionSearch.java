package com.fiserv;

import lombok.Data;

@Data
public class TransactionSearch {
    private String acquirerRefNumber;
    private String tranStartDate;
    private String tranEndDate;

    public TransactionSearch(String acquirerRefNumber, String tranStartDate, String tranEndDate) {
        this.acquirerRefNumber = acquirerRefNumber;
        this.tranStartDate = tranStartDate;
        this.tranEndDate = tranEndDate;
    }
}
