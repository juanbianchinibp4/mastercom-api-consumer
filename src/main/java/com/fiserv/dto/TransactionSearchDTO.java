package com.fiserv.dto;

import lombok.Data;

@Data
public class TransactionSearchDTO {
    private String acquirerRefNumber;
    private String tranStartDate;
    private String tranEndDate;

    public TransactionSearchDTO(String acquirerRefNumber, String tranStartDate, String tranEndDate) {
        this.acquirerRefNumber = acquirerRefNumber;
        this.tranStartDate = tranStartDate;
        this.tranEndDate = tranEndDate;
    }
}
