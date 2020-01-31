package com.licong.notemap.web.controller;

import com.licong.notemap.service.FinancialAnalysisService;
import com.licong.notemap.service.domain.FinancialAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FinancialAnalysisController {
    @Autowired
    private FinancialAnalysisService financialAnalysisService;

    @RequestMapping(value = "/api/financial_analysis/{stock_code}", method = RequestMethod.GET)
    public FinancialAnalysis get(@PathVariable("stock_code") String stockCode) {
        return financialAnalysisService.analysis(stockCode);
    }
}
