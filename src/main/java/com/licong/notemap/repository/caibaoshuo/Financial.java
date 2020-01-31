package com.licong.notemap.repository.caibaoshuo;

import lombok.Data;

@Data
public class Financial {

    //================   A 比气长   ================//

    /**
     * A2 现金占总资产比率(70%)
     */
    private Double cashToTotalAssetsRatio;

    /**
     * 应收款项周转天数
     */
    private Double receivableTurnoverDays;

    /**
     * A1 现金流量比率
     */
    private Double cashFlowRatio;

    /**
     * A1 现金流量允当比率
     */
    private Double cashFlowAdequacyRatio;

    /**
     * A1 现金再投资比率
     */
    private Double cashReinvestmentRatio;

    //================   B 经营能力   ================//
    /**
     * B1 总资产周转率(次/年)
     */
    private Double totalAssetsTurnover;

    /**
     * B2 存货周转率(次/年)
     */
    private Double inventoryTurnover;

    /**
     * B3 应收账款周转率(次/年)
     */
    private Double receivableTurnover;

    //================   C 获利能力   ================//
    /**
     * C1 毛利率
     */
    private Double grossMargin;

    /**
     * C2 营业利润率
     */
    private Double operatingMargin;

    /**
     * C2.1 营业费用率（毛利率-营业利润率）
     */
    private Double operatingExpenseRatio;

    /**
     * C3 经营安全边际率（营业利润率/毛利率)
     */
    private Double operatingSafetyRatio;

    /**
     * C4 净利率
     */
    private Double netProfitRatio;

    /**
     * C5 每股获利（EPS）
     */
    private Double eps;

    /**
     * C6 股东报酬率（RoE)
     */
    private Double roe;

    //================   D 破产危机   ================//
    /**
     * D1 负债资产比率
     */
    private Double debtToAssetRatio;

    /**
     *  D2 长期资金占不动产、厂房、设备比率
     */
    private Double longTermCapitalToAssetsRatio;

    //================   E 还债能力   ================//
    /**
     *  E1 流动比率
     */
    private Double currentRatio;

    /**
     * E2 速动比率
     */
    private Double quickRatio;


}
