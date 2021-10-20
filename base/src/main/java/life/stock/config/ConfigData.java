package life.stock.config;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public interface ConfigData {
    String ACCOUNT_DATA_PATHNAME = "/Users/lgren/Project/Java/0My/stock1.1/core/src/main/resources/data/%s.txt";
    String CHOOSE_STOCK_VIEW_PATHNAME_FORMAT = "/Users/lgren/Project/Java/0My/stock1.1/core/src/main/resources/data/choose/%s.html";
    String HOLD_STOCK_VIEW_PATHNAME_FORMAT = "/Users/lgren/Project/Java/0My/stock1.1/core/src/main/resources/data/query/%s.html";

    String RECEIVE_MAILS = "625552409@qq.com";

    BigDecimal BUY_BROKERAGE_RATIO = NumberUtil.toBigDecimal(0.00032);

    DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
}
