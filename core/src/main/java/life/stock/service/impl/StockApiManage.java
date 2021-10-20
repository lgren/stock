package life.stock.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import life.stock.entity.StockNewDO;
import life.stock.manage.api.BaseItemAbs;
import life.stock.manage.api.XinLangItemDO;
import life.stock.manage.api.XueQiuItemDO;
import life.stock.service.IStockApiService;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum StockApiManage implements IStockApiService {
    // 新浪网数据
    XinLang(t -> IntStream.range(1, 10).mapToObj(i -> {
        //
        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData";
        return JSONUtil.toList(HttpUtil.createGet(url)
                .header("Content-Type", "application/json; charset=gbk")
                .form(MapUtil.<String, Object>builder()
                        .put("page", i)
                        .put("num", "100")
                        .put("sort", "changepercent")
                        .put("asc", "0")
                        .put("node", "hs_a")
                        .put("symbol", "")
                        .put("_s_r_a", "page")
                        .build())
                .execute()
                .body(), XinLangItemDO.class);
    }).flatMap(Collection::stream).collect(Collectors.toList())),
    // 雪球网网站 https://xueqiu.com/hq#type=sha&exchange=CN&firstName=%E6%B2%AA%E6%B7%B1%E8%82%A1%E5%B8%82&secondName=%E6%8E%92%E8%A1%8C&market=CN&order=desc&order_by=percent&plate=%E6%B2%AAA%E6%B6%A8%E5%B9%85%E6%A6%9C
    // 雪球网数据 https://xueqiu.com/service/v5/stock/screener/quote/list?page=2&size=30&order=desc&order_by=percent&exchange=CN&market=CN&type=sha&_=1633655714141
    XueQiu(t -> {
        HttpUtil.get("https://xueqiu.com/");
        return IntStream.range(1, 10).mapToObj(i -> {
            String url = "https://xueqiu.com/service/v5/stock/screener/quote/list";
            return JSONUtil.parseObj(HttpUtil.createGet(url)
                    .header("Content-Type", "application/json; charset=gbk")
                    .form(MapUtil.<String, Object>builder()
                            .put("page", i)
                            .put("size", "100")
                            .put("order", "desc")
                            .put("order_by", "percent")
                            .put("exchange", "CN")
                            .put("market", "CN")
                            .put("type", t)// 沪A:sha(SH), 沪B:shb(SH), 深A:sza(SZ), 深B:szb(SZ)
                            .put("_", System.currentTimeMillis())
                            .build())
                    .execute()
                    .body()).getJSONObject("data").getJSONArray("list").toList(XueQiuItemDO.class);
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }),
    ;

    public static final String TYPE_ALL = "sha,sza";
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    private Function<String, List<BaseItemAbs>> listFunc;
    /**
     * 数据来源
     * @param listFunc 参数: 类型, 返回值: IBaseItem的列表
     */
    StockApiManage(Function<String, List<BaseItemAbs>> listFunc) {
        this.listFunc = listFunc;
    }

    @Override
    public List<BaseItemAbs> getList(String type) {
        return listFunc.apply(type);
    }

    @Override
    public double getVolumeRatio(String code) {
        code = code.toUpperCase();
        JSONObject result = JSONUtil.parseObj(HttpUtil.get(MessageFormat.format("https://stock.xueqiu.com/v5/stock/quote.json?symbol={0}&extend=detail", code)));
        return result.getJSONObject("data").getJSONObject("quote").getBigDecimal("volume_ratio").doubleValue();
    }

    @Override
    public StockNewDO getStockNew(String code) {
        code = code.toLowerCase();
        return new StockNewDO(code, HttpUtil.get(MessageFormat.format("http://hq.sinajs.cn/?list={0}", code)));
    }

    @Override
    public List<StockNewDO> listStockNew(String code) {
        code = code.toLowerCase();
        return Arrays.stream(code.split(",")).map(this::getStockNew).collect(Collectors.toList());
    }

    @Override
    public Map<String, StockNewDO> mapStockNew(String code) {
        code = code.toLowerCase();
        return listStockNew(code).stream().collect(Collectors.toMap(StockNewDO::getSymbol, o -> o));
    }
}
