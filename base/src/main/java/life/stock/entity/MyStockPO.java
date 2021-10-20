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
import java.util.Objects;
import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class MyStockPO implements IGenerateHtml<BigDecimal> {
    // 代码
    private String symbol;
    // 名称
    private String name;
    // 持有价格
    private BigDecimal holdPrice;
    // 数量
    private int num;

    // 当前价格
    private BigDecimal current;
    // 涨幅
    private BigDecimal percent;
    // 收益
    @Setter(AccessLevel.NONE)
    private BigDecimal income;
    // 收益率
    @Setter(AccessLevel.NONE)
    private BigDecimal incomeRatio;


    public MyStockPO(String dataArrStr) {
        String[] dataArr = dataArrStr.split("\\|\\||<=>");
        name = dataArr[0];
        symbol = dataArr[1];
        holdPrice = NumberUtil.toBigDecimal(dataArr[2]);
        num = Integer.parseInt(dataArr[3]);
        // ||
        current = NumberUtil.toBigDecimal(dataArr[5]);
        percent = Optional.of(dataArr[6]).filter(s -> !Objects.equals(s, "null")).map(NumberUtil::toBigDecimal).orElse(null);
        // ||
        income = NumberUtil.toBigDecimal(dataArr[7]);
        incomeRatio = NumberUtil.toBigDecimal(dataArr[8]);
    }

    // 总价
    public BigDecimal getCurrentTotal() {
        return NumberUtil.mul(current, num);
    }

    // 总价
    public BigDecimal getHoldPriceTotal() {
        return NumberUtil.mul(holdPrice, num);
    }

    // 计算收益
    public MyStockPO calIncome() {
        incomeRatio = NumberUtil.div(NumberUtil.sub(current, holdPrice), holdPrice);
        income = NumberUtil.mul(getCurrentTotal(), incomeRatio);
        return this;
    }

    // 设置当前价格 并 计算收益
    public MyStockPO calIncome(BigDecimal current) {
        this.current = current;
        return calIncome();
    }

    // 计算佣金
    public BigDecimal calBrokerage(BigDecimal brokerageRatio) {
        return NumberUtil.mul(holdPrice, num, brokerageRatio);
    }

    // 通常是新的合并老的
    public MyStockPO mergeBuyNew(MyStockPO stock) {
        if (stock != null) {
            int numAll = this.num + stock.getNum();
            return MyStockPO.builder()
                    .symbol(symbol)
                    .holdPrice(NumberUtil.div(NumberUtil.add(getHoldPriceTotal(), stock.getHoldPriceTotal()), numAll))
                    .num(numAll)
                    .current(stock.getCurrent())
                    .build();
        }
        return this;
    }

    public MyStockPO refresh(StockNewDO stockNew) {
        setSymbol(stockNew.getSymbol());
        setName(stockNew.getName());
        setCurrent(stockNew.getCurrent());
        setPercent(stockNew.getPercent());
        calIncome(stockNew.getCurrent());
        return this;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}<=>{1}<=>{2,NUMBER,#.####}<=>{3,NUMBER,#}<=>{4,NUMBER,#.####}||{5,NUMBER,#.####}<=>{6,NUMBER,#.####}||{7,NUMBER,#.####}<=>{8,NUMBER,#.####}"
                , name, symbol, holdPrice, num, getHoldPriceTotal(), current, percent, income, incomeRatio);
    }

    public String printInfo(String prefix) {
        return String.format("%1$s代码: %2$s" +
                        "\n%1$s名称: %3$s" +
                        "\n%1$s持有价格: %4$s" +
                        "\n%1$s数量: %5$s" +
                        "\n%1$s当前价格: %6$s" +
                        "\n%1$s涨幅: %7$s" +
                        "\n%1$s收益: %8$s" +
                        "\n%1$s收益率: %9$s"
                , prefix, symbol, name, holdPrice, num, current, percent, income, incomeRatio);
    }
}
