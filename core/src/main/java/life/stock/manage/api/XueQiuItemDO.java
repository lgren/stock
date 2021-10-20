package life.stock.manage.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XueQiuItemDO extends BaseItemAbs {
    // 代码
    private String symbol;
    private Object net_profit_cagr;
    private Object north_net_inflow;
    private Double ps;
    private Integer type;
    // 涨幅
    private Double percent;
    private Boolean has_follow;
    private Double tick_size;
    private Object pb_ttm;
    private Long float_shares;
    // 当前价格
    private Double current;
    private Double amplitude;
    private Object pcf;
    // 开服至今涨幅
    private Double current_year_percent;
    // 流通市值
    private Double float_market_capital;
    private Object north_net_inflow_time;
    // 市值
    private Long market_capital;
    private Object dividend_yield;
    private Integer lot_size;
    private Object roe_ttm;
    private Double total_percent;
    private Double percent5m;
    private Object income_cagr;
    private Double amount;
    private Double chg;
    private Long issue_date_ts;
    private Object eps;
    private Integer main_net_inflows;
    private Integer volume;
    private Double volume_ratio;
    // 市净率
    private Double pb;
    private Integer followers;
    // 换手率
    private Double turnover_rate;
    private Double first_percent;
    private String name;
    // 市盈率
    private Double pe_ttm;
    private Long total_shares;
    private Integer limitup_days;


    @Override
    public Double getTurnoverRatio() {
        return getTurnover_rate();
    }

    @Override
    public Double getNmcW() {
        return Optional.ofNullable(getFloat_market_capital()).map(o -> o / 1_0000).orElse(null);
    }

    @Override
    public Double getPer() {
        return getPe_ttm();
    }

    @Override
    public Double getVolumeRatio() {
        return getVolume_ratio();
    }

    @Override
    public void setVolumeRatio(Double volumeRatio) {
        setVolume_ratio(volumeRatio);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
