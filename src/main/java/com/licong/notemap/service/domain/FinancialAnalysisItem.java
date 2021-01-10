package com.licong.notemap.service.domain;

import lombok.Data;

import java.util.Collections;

@Data
public class FinancialAnalysisItem implements Comparable {

    private String title;
    private Double value;
    private String description;
    private FinancialAnalysisItemStatus status;

    public boolean isGood() {
        return FinancialAnalysisItemStatus.GOOD == status;
    }

    public boolean isBad() {
        return FinancialAnalysisItemStatus.BAD == status;
    }

    public boolean isMiddle() {
        return FinancialAnalysisItemStatus.MIDDLE == status;
    }

    @Override
    public int compareTo(Object o) { FinancialAnalysisItem other = (FinancialAnalysisItem) o;
        return this.title.compareTo(other.title);
    }
}
