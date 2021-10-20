package life.stock.entity;

public interface IGenerateHtml<T> {
    // 代码
    String getSymbol();
    // 名称
    String getName();
    // 当前价格
    T getCurrent();
    // 涨幅
    T getPercent();
}
