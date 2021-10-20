package life.stock.manage.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XinLangItemDO extends BaseItemAbs {
    // 代码
    private String symbol;
    private String code;
    // 名字
    private String name;
    // 交易价
    private Double trade;
    // 涨跌额
    private Double pricechange;
    // 涨跌幅
    private Double changepercent;
    // 买入价格
    private Double buy;
    // 卖出价格
    private Double sell;
    // 昨日收价
    private Double settlement;
    // 今日开盘价
    private Double open;
    // 最高
    private Double high;
    // 最低
    private Double low;
    // 成交量/手
    private Integer volume;
    // 成交额/万
    private Integer amount;

    private String ticktime;
    // 市盈率
    private Double per;
    // 平均市净率
    private Double pb;
    // 市值/万
    private Double mktcap;
    // 流通市值/万
    private Double nmc;
    // 换手率
    private Double turnoverratio;
    // 量比 新浪默认是没有的, 需要再获取
    private Double volumeRatio;
    // 市销率 新浪默认是没有的, 需要再获取
    private Double ps;

    @Override
    public Double getPercent() {
        return getChangepercent();
    }

    public Double getTurnoverratio() {
        return turnoverratio;
    }

    @Override
    public Double getTurnoverRatio() {
        return getTurnoverratio();
    }

    @Override
    public Double getNmcW() {
        return getNmc();
    }


    @Override
    public Double getCurrent() {
        return getTrade();
    }

    @Override
    public Double getVolumeRatio() {
        return volumeRatio;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
