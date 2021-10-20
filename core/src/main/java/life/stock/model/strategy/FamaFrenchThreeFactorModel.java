package life.stock.model.strategy;

import life.stock.manage.api.BaseItemAbs;
import life.stock.service.IStockApiService;

import java.util.List;

/**
 * 实现参考
 * 多因子选股Alpha策略
 * https://blog.csdn.net/weixin_42219751/article/details/96856006
 *
 * 1，选取小盘价值股构建超配组合，选取小盘成长股构建低配组合。做多超配组合的同时，做空等市值的低配组合，从而获取alpha收益。
 * 2，超配组合选股条件：市值<200亿，市净率PB<1.5，0<市盈率PE<30，市销率PS(price-to-sales)<5。选取前20只符合条件的股票组成超配组合，若可选股票少于5只，不开仓。
 * 3，低配组合选股条件：市净率PB>4，市盈率PE<0或PE>200，可融券。选取前20只符合条件的股票组成低配组合，若可选股票少于5只，不开仓。
 * 4，调仓频率：逐月调仓。
 */
public enum FamaFrenchThreeFactorModel implements IStrategyModel {
    INSTANCE;

    @Override
    public List<BaseItemAbs> chooseStock(IStockApiService stockApi, List<BaseItemAbs> baseList) {
        List<BaseItemAbs> tempList = baseList;
        // 市值 < 200E
        tempList = filter(tempList, BaseItemAbs::getNmcE, null, 200D);
        // 市净率PB < 1.5
        tempList = filter(tempList, BaseItemAbs::getPb, null, 1.5D);
        // 0 < 市盈率PE < 30
        tempList = filter(tempList, BaseItemAbs::getPer, 0D, 30D);
        // 市销率PS < 5 TODO 新浪没有 需要再获取
        tempList = filter(tempList, BaseItemAbs::getPer, null, 5D);


        // // 量比小于1的全部剔除 此步骤放最后, 因为部分网站获取的数据没有量比, 需要再处理
        // if (Objects.equal(stockApi, StockApiManage.XinLang)) {
        //     tempList.forEach(o -> o.setVolumeRatio(stockApi.getVolumeRatio(o.getSymbol())));
        // }
        // tempList = filter(tempList, (o -> o.getVolumeRatio() != null && o.getVolumeRatio() >= 1));
        return tempList;
    }
}
