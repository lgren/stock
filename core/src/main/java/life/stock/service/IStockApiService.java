package life.stock.service;

import life.stock.config.ConfigData;
import life.stock.entity.StockNewDO;
import life.stock.manage.api.BaseItemAbs;

import java.util.List;
import java.util.Map;

public interface IStockApiService extends ConfigData {
    List<BaseItemAbs> getList(String type);

    double getVolumeRatio(String code);

    StockNewDO getStockNew(String code);

    List<StockNewDO> listStockNew(String code);

    Map<String, StockNewDO> mapStockNew(String code);

}
