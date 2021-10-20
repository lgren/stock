package life.stock.entity;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class StockNewDO implements IGenerateHtml<BigDecimal> {
    // 代码
    public String symbol;
    // 名称
    public String name;
    // 今日开盘价格
    public BigDecimal todayOpen;
    // 昨天收盘价格
    public BigDecimal yesterdayClose;
    // 当前价格
    public BigDecimal current;
    // 涨幅
    public BigDecimal percent;

    public StockNewDO(String symbol, String dataArrStr) {
        String[] dataArr = dataArrStr.split("[\",]");
        name = dataArr[1];
        this.symbol = symbol;
        todayOpen = NumberUtil.toBigDecimal(dataArr[2]);
        yesterdayClose = NumberUtil.toBigDecimal(dataArr[3]);
        current = NumberUtil.toBigDecimal(dataArr[4]);
        percent = NumberUtil.div(NumberUtil.sub(current, yesterdayClose), yesterdayClose);
    }

    @Override
    public String toString() {
        return "{" +
                "代码:'" + getSymbol() + '\'' +
                "名称:'" + getName() + '\'' +
                "涨幅:'" + getPercent() + '\'' +
                "换手率:'" + getCurrent() + '\'' +
                '}';
    }
}
