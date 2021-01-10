package com.licong.notemap.repository.caibaoshuo;

public interface FinancialRepository {
    /**
     * 根据股票代码提取财报信息
     *
     * @param stockCode 股票代码
     * @return
     */
    Financial get(String stockCode);
}
