package com.licong.notemap.service.domain;

import lombok.Data;

import java.util.List;

/**
 * 财务分析
 */
@Data
public class FinancialAnalysis {

    private List<FinancialAnalysisItem> items;
}
