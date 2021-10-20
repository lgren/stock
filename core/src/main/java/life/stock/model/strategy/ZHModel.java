package life.stock.model.strategy;

import life.stock.manage.api.BaseItemAbs;
import life.stock.service.IStockApiService;
import life.stock.service.impl.StockApiManage;

import java.util.List;
import java.util.Objects;

/**
 * 实现参考
 * 湖南有一位炒股奇才，每天只在尾盘30分钟选股，短短三年时间就实现了财富自由！ - 骑士的文章 - 知乎
 * https://zhuanlan.zhihu.com/p/412959317
 */
public enum ZHModel implements IStrategyModel {
    INSTANCE;

    @Override
    public List<BaseItemAbs> chooseStock(IStockApiService stockApi, List<BaseItemAbs> baseList) {
        List<BaseItemAbs> tempList = baseList;

        // 涨幅 3-5%
        tempList = filter(tempList, BaseItemAbs::getPercent, 3D, 5D, true);

        // 换手率 5-10%
        tempList = filter(tempList, BaseItemAbs::getTurnoverRatio, 5D , 10D, true);

        // 流通市值50-200E
        tempList = filter(tempList, BaseItemAbs::getNmcE, 50D, 200D, true);

        // 量比小于1的全部剔除 此步骤放最后, 因为部分网站获取的数据没有量比, 需要再处理
        if (Objects.equals(stockApi, StockApiManage.XinLang)) {
            tempList.forEach(o -> o.setVolumeRatio(stockApi.getVolumeRatio(o.getSymbol())));
        }
        tempList = filter(tempList, (o -> o.getVolumeRatio() != null && o.getVolumeRatio() >= 1));

        /*
        5. 成交量持续放大留下, 像台阶式更好. 成交量一高一低的, 不稳定的剔除
           分时: http://image.sinajs.cn/newchart/min/n/sh600163.gif
           日 K: http://image.sinajs.cn/newchart/daily/n/sh600163.gif
           周 K: http://image.sinajs.cn/newchart/weekly/n/sh600163.gif
           月 K: http://image.sinajs.cn/newchart/monthly/n/sh600163.gif

        6. 看个股的K线形态，短期看5/10/20日均线，搭配60日均线多头向上发散就是最好的形态. 把一些K线上方没有任何压力的留下，这样冲高也会更加轻松，做大概率的事件
           如果K线形态显示在重要的均线下方，则说明近期个股的走势是冲高回落，上方的套牢盘压力过高，处于成交密集区，这种的继续进行剔除.

        7. 分时图来判断强势股的特征，能够跑赢大盘的都是属于逆势上涨的，强者恒强的市场，只有选取强势股才能把收益做到最大，最好能搭配当下热点题材板块，这样支撑就更加有力度
           比如近期火热的锂电，小金属原材料，有色顺周期股等等
           把剩下的优质股叠加上证指数的分时图，个股价格的走势，必须是全天在分时图价格的上方，这样的表示个股的涨幅较好，市场的气氛充足，在车上的人都可以吃到一波盈利，次日的冲高会更加的有力度。
         */
        return tempList;
    }

}
