package life.stock.entity;

import cn.hutool.core.util.NumberUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;
import java.util.function.Function;


@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class MyAccountPO {
    // ID
    private Long id;
    // 名称
    private String name;
    // 投入
    private BigDecimal invest;
    // 现金
    private BigDecimal money;
    // 持有股票
    private Map<String, MyStockPO> myStockMap;

    // 资产
    @Setter(AccessLevel.NONE)
    private BigDecimal asset;
    // 收益
    @Setter(AccessLevel.NONE)
    private BigDecimal income;
    // 收益率
    @Setter(AccessLevel.NONE)
    private BigDecimal incomeRatio;

    public MyAccountPO(String dataArrStr) {
        String[] dataArr = dataArrStr.split("\\|\\||<=>");
        money = NumberUtil.toBigDecimal(dataArr[0]);
        asset = NumberUtil.toBigDecimal(dataArr[1]);
        invest = NumberUtil.toBigDecimal(dataArr[2]);
        // <=>
        id = Long.valueOf(dataArr[3]);
        name = dataArr[4];
        // <=>
        income = NumberUtil.toBigDecimal(dataArr[5]);
        incomeRatio = NumberUtil.toBigDecimal(dataArr[6]);
    }

    public static MyAccountPO create(long id, String name, double money) {
        BigDecimal moneyVar = NumberUtil.toBigDecimal(money);
        return MyAccountPO.builder().id(id).name(name).money(moneyVar).asset(moneyVar).invest(moneyVar).build().calIncome();
    }

    public MyAccountPO calIncome() {
        asset = NumberUtil.add(myStockMap.values().stream().reduce(BigDecimal.ZERO, (r, o) -> NumberUtil.add(r, o.getCurrentTotal()), NumberUtil::add), money);
        income = NumberUtil.sub(asset, invest);
        incomeRatio = NumberUtil.div(income, invest);
        return this;
    }

    public MyAccountPO buy(String symbol, BigDecimal current, int num, BigDecimal brokerageRatio) {
        if (num % 100 != 0) throw new IllegalArgumentException("数量为100的倍数");

        // 生成新的购买股票
        MyStockPO buyItem = MyStockPO.builder().symbol(symbol).holdPrice(current).num(num).current(current).build();

        // 佣金
        BigDecimal brokerage = buyItem.calBrokerage(brokerageRatio);
        // 剩下的钱
        BigDecimal leftMoney = NumberUtil.sub(money, brokerage, buyItem.getHoldPriceTotal());
        // 没钱购买了
        if (NumberUtil.isLess(leftMoney, new BigDecimal(0))) {
            throw new RuntimeException("钱不够啦");
        }
        // 将之前的股票和新买的股票合并并写入
        myStockMap.put(symbol, buyItem.mergeBuyNew(myStockMap.get(symbol)).calIncome());
        // 购买后资产
        asset = NumberUtil.sub(asset, brokerage);
        // 购买后剩余现金
        money = leftMoney;

        calIncome();
        return this;
    }

    public MyAccountPO refreshNew(Function<String, Map<String, StockNewDO>> getStockNewMap) {
        Map<String, StockNewDO> stockNewMap = getStockNewMap.apply(String.join(",", myStockMap.keySet()));// 通过id获取当前最新信息
        myStockMap.forEach((k1, v) -> v.refresh(stockNewMap.get(v.getSymbol())));
        return calIncome();
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0,number,#.####}<=>{1,number,#.####}<=>{2,number,#.####}||{3,number,#}<=>{4}||{5,number,#.####}<=>{6,number,#.####}"
                , money, asset, invest, id, name, income, incomeRatio);
    }

    public String printInfo(String prefix) {
        String accountPrintInfo = String.format("ID:%s" +
                        "\n名称: %s" +
                        "\n投入: %s" +
                        "\n现金: %s" +
                        "\n资产: %s" +
                        "\n收益: %s" +
                        "\n收益率: %s"
                , id, name, invest, money, asset, income, incomeRatio);
        StringBuilder sb = new StringBuilder(accountPrintInfo);
        sb.append("\n持有股票如下:");
        String nextPrefix = prefix + prefix;
        myStockMap.forEach((k,v) -> {
            sb.append(String.format("\n%s%s:\n%s", prefix, v.getName(), v.printInfo(nextPrefix)));
        });
        return sb.toString();
    }
//ID id
//名称 name
//投入 invest
//现金 money
//持有股票 myStockMap
//资产 asset
//收益 income
//收益率 incomeRatio
}
