package com.licong.notemap.repository.caibaoshuo.internal;

import com.licong.notemap.repository.caibaoshuo.Financial;
import com.licong.notemap.repository.caibaoshuo.FinancialRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 从财报说提取财报信息
 * e.g. https://caibaoshuo.com/companies/603288/financials
 */
@Slf4j
public class FinancialRepositoryImpl implements FinancialRepository {

    @Override
    public Financial get(Integer stockCode) {

        return null;
    }

    /**
     * 信任所有站点
     * https://blog.csdn.net/shaochong047/article/details/79636142
     */
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer stockCode = 603288;
        Element body;
        try {
            trustEveryone();
            Document doc = Jsoup.connect(String.format("https://caibaoshuo.com/companies/%s/financials", stockCode))
                    .ignoreHttpErrors(true)
                    .get();
            body = doc.body();
        } catch (IOException e) {
            log.error("Get caibaoshuo document failed.", e);
            return;
        }

        Financial financial = new Financial();

        // 资产负债比率
        Element assetLiabilityRatio = body.getElementById("albs-yearly");

        // 五大财务比率
        Element fiveFinancialRatios = body.getElementById("alkey-yearly");

        //================   A 比气长   ================//
        // 现金与约当现金(%)
        String cashToTotalAssetsRatio = assetLiabilityRatio
                .getElementsByTag("tbody").first()
                .getElementsByTag("tr").first()
                .getElementsByTag("td").last()
                .getElementsByTag("span").last()
                .text();
        log.info("现金与约当现金(%):" + cashToTotalAssetsRatio);
        financial.setCashToTotalAssetsRatio(Double.valueOf(cashToTotalAssetsRatio));

        // 应收款项周转天数(天)
        String receivableTurnoverDays = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 6);
        log.info("应收款项周转天数(天):" + receivableTurnoverDays);
        financial.setReceivableTurnoverDays(Double.valueOf(receivableTurnoverDays));

        // 现金流量比率(%)
        String cashFlowRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 27);
        log.info("现金流量比率:" + cashFlowRatio);
        financial.setCashFlowRatio(Double.valueOf(cashFlowRatio));

        // 现金流量允当比率(%)
        String cashFlowAdequacyRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 28);
        log.info("现金流量允当比率(%):" + cashFlowAdequacyRatio);
        financial.setCashFlowAdequacyRatio(Double.valueOf(cashFlowAdequacyRatio));

        // 现金再投资比率(%)
        String cashReinvestmentRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 29);
        log.info("现金再投资比率(%):" + cashReinvestmentRatio);
        financial.setCashReinvestmentRatio(Double.valueOf(cashReinvestmentRatio));

        //================   B 经营能力   ================//
        /**
         * 总资产周转率(次/年)
         */
        String totalAssetsTurnover = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 13);
        log.info("总资产周转率(次/年):" + totalAssetsTurnover);
        financial.setTotalAssetsTurnover(Double.valueOf(totalAssetsTurnover));

        /**
         * 存货周转率(次/年)
         */
        String inventoryTurnover = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 7);
        log.info("存货周转率(次/年):" + inventoryTurnover);
        financial.setInventoryTurnover(Double.valueOf(inventoryTurnover));

        /**
         * 	应收款项周转率(次/年)
         */
        String receivableTurnover = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 5);
        log.info("应收款项周转率(次/年):" + receivableTurnover);
        financial.setReceivableTurnover(Double.valueOf(receivableTurnover));

        //================   C 获利能力   ================//
        /**
         * 毛利率(%)
         */
        String grossMargin = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 18);
        log.info("毛利率(%):" + grossMargin);
        financial.setGrossMargin(Double.valueOf(grossMargin));


        /**
         * 营业利润率(%)
         */
        String operatingMargin = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 19);
        log.info("营业利润率(%):" + operatingMargin);
        financial.setOperatingMargin(Double.valueOf(operatingMargin));

        /**
         * 营业费用率(%)
         */
        String operatingExpenseRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 21);
        log.info("营业费用率(%):" + operatingExpenseRatio);
        financial.setOperatingExpenseRatio(Double.valueOf(operatingExpenseRatio));

        /**
         * 经营安全边际率(%):
         */
        String operatingSafetyRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 22);
        log.info("经营安全边际率(%):" + operatingSafetyRatio);
        financial.setOperatingSafetyRatio(Double.valueOf(operatingSafetyRatio));

        /**
         * 净利率(%)
         */
        String netProfitRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 20);
        log.info("净利率(%):" + netProfitRatio);
        financial.setNetProfitRatio(Double.valueOf(netProfitRatio));

        /**
         * EPS=基本每股收益(元)
         */
        String eps = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 23);
        log.info("EPS=基本每股收益(元):" + eps);
        financial.setEps(Double.valueOf(eps));

        /**
         * ROE=净资产收益率(%)
         */
        String roe = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 15);
        log.info("ROE=净资产收益率(%):" + roe);
        financial.setEps(Double.valueOf(roe));

        //================   D 破产危机   ================//
        /**
         * 负债占资产比率(%)
         */
        String debtToAssetRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 0);
        log.info("负债占资产比率(%):" + debtToAssetRatio);
        financial.setDebtToAssetRatio(Double.valueOf(debtToAssetRatio));

        /**
         *  长期资金占重资产比率(%)
         */
        String longTermCapitalToAssetsRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 1);
        log.info("长期资金占重资产比率(%):" + longTermCapitalToAssetsRatio);
        financial.setLongTermCapitalToAssetsRatio(Double.valueOf(longTermCapitalToAssetsRatio));

        //================   E 还债能力   ================//
        /**
         *  流动比率(%)
         */
        String currentRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 2);
        log.info("流动比率(%):" + currentRatio);
        financial.setCurrentRatio(Double.valueOf(currentRatio));

        /**
         * 速动比率(%)
         */
        String quickRatio = getFiveFinancialRatiosItemValue(fiveFinancialRatios, 3);
        log.info("速动比率(%):" + quickRatio);
        financial.setQuickRatio(Double.valueOf(quickRatio));
    }

    /**
     * 获取五大财务比率项的值
     *
     * @param fiveFinancialRatios 五大财务比率
     * @param rowIndex            行数,从0开始
     * @return
     */
    private static String getFiveFinancialRatiosItemValue(Element fiveFinancialRatios, int rowIndex) {
        return fiveFinancialRatios
                .getElementsByTag("tbody").first()
                .getElementsByTag("tr").get(rowIndex)
                .getElementsByTag("td").last()
                .text().replace(",", "");
    }
}
