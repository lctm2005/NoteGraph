package com.licong.notemap.service.internal;

import com.licong.notemap.repository.caibaoshuo.Financial;
import com.licong.notemap.repository.caibaoshuo.FinancialRepository;
import com.licong.notemap.service.FinancialAnalysisService;
import com.licong.notemap.service.domain.FinancialAnalysis;
import com.licong.notemap.service.domain.FinancialAnalysisItem;
import com.licong.notemap.service.domain.FinancialAnalysisItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialAnalysisServiceImpl implements FinancialAnalysisService {

    @Autowired
    private FinancialRepository financialRepository;

    @Override
    public FinancialAnalysis analysis(String stockCode) {
        Financial financial = financialRepository.get(stockCode);
        FinancialAnalysis financialAnalysis = new FinancialAnalysis();
        financialAnalysis.setItems(buildItems(financial));
        return financialAnalysis;
    }

    /**
     * 构造财务指标分析项清单
     * @param financial 财报
     * @return 财务指标分析项清单
     */
    private List<FinancialAnalysisItem> buildItems(Financial financial) {
        List<FinancialAnalysisItem> items = new ArrayList<>();
        // A 比气长
        items.add(buildItem("A2 现金占总资产比率(70%)",
                "至少>10%。如果是烧钱行业，即“总资产周转率<1”的公司，这个指标要求>25%",
                financial.getCashToTotalAssetsRatio(),
                value -> {
                    if (value >= 25) return FinancialAnalysisItemStatus.GOOD;
                    if (value < 10) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));
        items.add(buildItem("A3 应收账款周转天数(20%)",
                "应收账款周转天数”在60天~90天内都属于正常范围",
                financial.getReceivableTurnoverDays(),
                value -> {
                    if (value > 90) return FinancialAnalysisItemStatus.BAD;
                    if (value < 60) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));
        items.add(buildItem("A1 现金流量比率",
                "现金流量比率>100%比较好！满足这个指标代表公司赚回的现金比较多，对外欠款比较少！",
                financial.getCashFlowRatio(),
                value -> {
                    if (value > 100) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("A1 现金流量允当比率",
                "现金流量允当比率>100%比较好！ 表示公司最近5个年度自己所赚的钱已经够用，不太需要看银行或股东的脸色。",
                financial.getCashFlowAdequacyRatio(),
                value -> {
                    if (value > 100) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("A1 现金再投资比率",
                "大于10%更稳妥。该比率越高，表明企业可用于再投资在各项资产的现金越多，企业再投资能力强；反之，则表示企业再投资能力弱。",
                financial.getCashReinvestmentRatio(),
                value -> {
                    if (value > 10) return FinancialAnalysisItemStatus.GOOD;
                    if (value < 7) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));

        // B 经营能力
        items.add(buildItem("B1 总资产周转率(次/年)",
                "总资产周转率介于1~2之间的，都是运营正常的公司。数字接近1，表示公司经营能力比较普通；接近2，表示其经营能力非常优秀。\n" +
                        "总资产周转率<1，代表它是资本密集或奢侈品行业，又叫“烧钱的行业”。",
                financial.getTotalAssetsTurnover(),
                value -> {
                    if (value > 1) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("B2 存货周转率(次/年)",
                "1、<30天，经营能力非常优异！\n" +
                        "2、30~50天，通常在流通业中属于模范生了！\n" +
                        "3、50~80天，经营能力不错！\n" +
                        "4、80~100天，大多为B2B（公司对公司）业务！\n" +
                        "5、100~150天，工业类或原物料行业（如石油、钢铁等，需求不旺盛）。\n" +
                        "6、>150天，要么能力不佳、要么属于特殊行业（如造船、造飞机、奢侈品、房地产公司等）。",
                financial.getInventoryTurnover(),
                value -> {
                    if (value > 4.5) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("B3 应收账款周转率(次/年)",
                "“应收账款周转率”在6次以上的都算经营不错的公司",
                financial.getReceivableTurnover(),
                value -> {
                    if (value > 6) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));

        // C 获利能力
        items.add(buildItem("C1 毛利率",
                "（1）特征1：\n" +
                        "毛利率越高，代表这真是一门好生意！\n" +
                        "毛利率越低，代表这真是一门艰难的生意。\n" +
                        "如果毛利率是负的，代表这真是一门烂生意，坚决不能投资！\n" +
                        "MJ老师建议不要投资任何“连续三年毛利率都是负值”的公司！\n" +
                        "（2）特征2：\n" +
                        "如果一个行业是挣钱的，那么就会有其他人参与竞争，这样毛利率就会慢慢下降。\n" +
                        "（3）特征3：\n" +
                        "毛利率是一个相对平稳的指标，如果出现突变，一定是出现了重大转变，我们要去研究到底出了什么重大事件！\n" +
                        "因此，看毛利率要五年一起看，从数字中看到趋势，而不能只看一年。",
                financial.getGrossMargin(),
                value -> {
                    if (value > 0) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));

        items.add(buildItem("C2 营业利润率",
                "",
                financial.getOperatingMargin(),
                value -> {
                    if (value > 0) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));

        items.add(buildItem("C2.1 营业费用率（毛利率-营业利润率）",
                "＜7％：通常代表这家公司不但已经具有很大的规模经济，而且在经营上的费用也相当节省\n" +
                        "＜10％：通常代表这家公司在自己的领域已经具有相当的规模经济\n" +
                        ">20%，可能出现在以下行业中：\n" +
                        "（1）、自有品牌，广告推销的费用非常贵。\n" +
                        "（2）、尚未具有经济规模的公司，因为分母（营业收入）小，所以指标高。\n" +
                        "（3）、市场蓬勃发展，但仍需持续投入的行业。\n" +
                        "（4）、需要不断促销，才有回头客的行业，如超市。\n" +
                        "（5）、餐饮业。餐饮业的费用率一般都在33%以上，所以餐饮业的毛利率也必须在50%以上才能持续经营。",
                financial.getOperatingExpenseRatio(),
                value -> {
                    if (value < 10) return FinancialAnalysisItemStatus.GOOD;
                    if (value > 20) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));

        items.add(buildItem("C3 经营安全边际率（营业利润率/毛利率)",
                "经营安全边际率>60%，则代表这家公司有较宽裕的获利空间，即使面临突如其来的市场波动，也将比其他竞争对手具有较高的抵抗能力。",
                financial.getOperatingSafetyRatio(),
                value -> {
                    if (value > 60) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("C4 净利率",
                "净利率>资金成本，即报酬>成本，以银行企业贷款为基准\n" +
                        "https://loans.cardbaobao.com/news/loansnews_54832.shtml\n" +
                        "虽然净收益和每股收益(EPS)是衡量公司盈利能力和价值的最广泛使用的参数，但它是最不可靠的。\n" +
                        "原因在于，通过调整折旧、损耗、非机动化和非经常性项目等任何数字，都可以很容易地操纵所报告的收益。",
                financial.getNetProfitRatio(),
                value -> {
                    if (value > 4.9) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("C5 每股获利（EPS）",
                "个股趋势对比——分析公司最近几年的每股收益情况，如果逐步增高，说明公司的盈利状况在稳步增长。\n" +
                        "行业横向对比——在整个行业中，将所有上市公司的每股收益进行对比，每股收益越多，说明公司的经营能力越强，越有可能为股东带来更多的分红。",
                financial.getEps(),
                value -> {
                    return FinancialAnalysisItemStatus.UNKNOWN;
                }));
        items.add(buildItem("C6 股东报酬率（RoE)",
                "1、ROE>20%的公司，是非常好的公司。一年平均报酬率20%或许不算什么，但长年保持20%的回报率是非常惊人的，这就是时间复利的力量！\n" +
                        "2、ROE<7%,可能就不值得投资。主要是由于以下两方面：\n" +
                        "（1）资金成本一般为2%~7%，至少要能保本吧；\n" +
                        "（2）机会成本。如果我们的钱投了ROE偏低的公司，这笔钱就不能投ROE更高的公司，会错失机会！",
                financial.getRoe(),
                value -> {
                    if (value > 20) return FinancialAnalysisItemStatus.GOOD;
                    if (value < 7) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));
        //D 破产危机
        items.add(buildItem("D1 负债资产比率",
                "经营稳健的公司，负债占资产比率 < 60%，股东出资多，不需要向银行借很多钱，股东认可公司发展并看好未来前景。\n" +
                        "下市破产的公司，负债占资产比率 > 80%，如果遇到这类公司一定要小心。",
                financial.getDebtToAssetRatio(),
                value -> {
                    if (value < 60) return FinancialAnalysisItemStatus.GOOD;
                    if (value > 80) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));
        items.add(buildItem("D2 长期资金占不动产、厂房、设备比率",
                "以长支长、越长越好，指标要>100%",
                financial.getLongTermCapitalToAssetsRatio(),
                value -> {
                    if (value > 100) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));

        //E 还债能力
        items.add(buildItem("E1 流动比率",
                "适合投资的公司，流动比率会要求最好是 >= 300% ，而且是近 3 年都是如此。",
                financial.getCurrentRatio(),
                value -> {
                    if (value > 300) return FinancialAnalysisItemStatus.GOOD;
                    return FinancialAnalysisItemStatus.BAD;
                }));
        items.add(buildItem("E2 速动比率",
                "速动比率速动比率越高，公司的流动性状况越好，一般该比率需要大于 100%。\n" +
                        "适合投资的公司，速动比率会要求最好是 >= 150% ，而且是近 3 年都是如此。",
                financial.getQuickRatio(),
                value -> {
                    if (value > 150) return FinancialAnalysisItemStatus.GOOD;
                    if (value < 100) return FinancialAnalysisItemStatus.BAD;
                    return FinancialAnalysisItemStatus.MIDDLE;
                }));
        return items;
    }


    /**
     * 构造财务指标分析项
     * @param title         财务指标标题
     * @param description   财务指标说明
     * @param value         财务指标标值
     * @param judger        财务指标状态判断器
     * @return  财务指标分析项
     */
    private FinancialAnalysisItem buildItem(String title, String description, Double value, Judger judger) {
        FinancialAnalysisItem item = new FinancialAnalysisItem();
        item.setTitle(title);
        item.setValue(value);
        item.setDescription(description);
        item.setStatus(value == null ? FinancialAnalysisItemStatus.UNKNOWN : judger.judge(value));
        return item;
    }

    /**
     * 财务指标状态判断器
     */
    @FunctionalInterface
    interface Judger {
        FinancialAnalysisItemStatus judge(Double value);
    }
}
