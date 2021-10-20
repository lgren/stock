package life.stock.manage.api;

import life.stock.entity.IGenerateHtml;

public abstract class BaseItemAbs implements IGenerateHtml<Double> {
    // 代码
    public abstract String getSymbol();
    // 名称
    public abstract String getName();
    // 涨幅
    public abstract Double getPercent();
    // 换手率
    public abstract Double getTurnoverRatio();
    // 流通市值/万
    public abstract Double getNmcW();
    // 市净率
    public abstract Double getPb();
    // 市盈率
    public abstract Double getPer();
    // 市销率
    public abstract Double getPs();
    // 当前价格
    public abstract Double getCurrent();
    // 量比
    public abstract Double getVolumeRatio();
    public abstract void setVolumeRatio(Double volumeRatio);

    // 流通市值/亿
    public Double getNmcE() {
        return getNmcW() / 1_0000;
    }
    @Override
    public String toString() {
        return "{" +
                "代码:'" + getSymbol() + '\'' +
                "名称:'" + getName() + '\'' +
                "涨幅:'" + getPercent() + '\'' +
                "换手率:'" + getTurnoverRatio() + '\'' +
                "流通市值_万:'" + getNmcW() + '\'' +
                "换手率:'" + getCurrent() + '\'' +
                "量比:'" + getVolumeRatio() + '\'' +
                '}';
    }
}
