import life.stock.entity.MyAccountPO;
import life.stock.manage.api.BaseItemAbs;
import life.stock.model.strategy.ZHModel;
import life.stock.service.IStockService;
import life.stock.service.impl.StockServiceImpl;
import org.junit.Test;

import java.util.List;

public class AppTest {
    private final IStockService stockService = StockServiceImpl.INSTANCE;


    /** 挑选模型1 + 预览 */
    @Test
    public void runStrategy() {
        List<BaseItemAbs> resultList = stockService.runStrategy(ZHModel.INSTANCE, true, false);
        resultList.forEach(o -> System.out.println(o.getSymbol() + "\t" + o.getName() + "\t" + o.getCurrent()));
    }

    /** 打印账户信息 */
    @Test
    public void printAccountInfo() {
        System.out.println(stockService.refreshAccount(1L).printInfo("  "));
    }

    /** 购买 */
    @Test
    public void buy() {
        // stockService.buy(1L, "sh600868", 3000, 3.21);
        // stockService.buy(1L, "sz002646", 500, 22.2);
        // stockService.buy(1L, "sh603618", 1400, 7.84);
        refreshAccount();
    }

    /** 预览持有 */
    @Test
    public void previewStockView() {
        MyAccountPO account = stockService.previewHoldStock(1L, true, false);
        System.out.println();
    }

    /** 刷新账户 */
    @Test
    public void refreshAccount() {
        MyAccountPO account = stockService.refreshAccount(1L);
        System.out.println();
    }

    /** 获取账户 */
    @Test
    public void getAccount() {
        MyAccountPO account = stockService.getAccount(1L);
        System.out.println();
    }

    /** 新增账户 */
    @Test
    public void addAccount() {
        MyAccountPO account = stockService.addAccount(1L, "一号选手", 10_0000);
        System.out.println();
    }
}
