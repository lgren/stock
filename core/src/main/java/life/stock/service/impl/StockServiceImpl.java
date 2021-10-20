package life.stock.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.mail.MailUtil;
import life.stock.config.ConfigData;
import life.stock.dao.IStockDao;
import life.stock.dao.impl.StockTxtDaoImpl;
import life.stock.entity.IGenerateHtml;
import life.stock.entity.MyAccountPO;
import life.stock.entity.StockNewDO;
import life.stock.manage.api.BaseItemAbs;
import life.stock.model.strategy.IStrategyModel;
import life.stock.service.IStockApiService;
import life.stock.service.IStockService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现参考
 * 湖南有一位炒股奇才，每天只在尾盘30分钟选股，短短三年时间就实现了财富自由！ - 骑士的文章 - 知乎
 * https://zhuanlan.zhihu.com/p/412959317
 */
public enum StockServiceImpl implements IStockService {
    INSTANCE;
    @Getter
    @Setter
    @Accessors(chain = true)
    private String chooseStockPathnameFormat = ConfigData.CHOOSE_STOCK_VIEW_PATHNAME_FORMAT;
    @Getter
    @Setter
    @Accessors(chain = true)
    private String holdStockViewPathnameFormat = ConfigData.HOLD_STOCK_VIEW_PATHNAME_FORMAT;

    @Getter
    @Setter
    @Accessors(chain = true)
    private String receiveMails = ConfigData.RECEIVE_MAILS;

    private final IStockDao stockDao = StockTxtDaoImpl.INSTANCE;
    private final IStockApiService stockApiService = StockApiManage.XueQiu;


    @Override
    public MyAccountPO addAccount(long id, String name, double money) {
        return stockDao.addAccount(id, name, money);
    }

    @Override
    public MyAccountPO getAccount(Long id) {
        return stockDao.getAccount(id);
    }

    @Override
    public MyAccountPO refreshAccount(Long id) {
        MyAccountPO account = getAccount(id);// 获取账号
        account.refreshNew(stockApiService::mapStockNew);// 通过id获取当前最新信息
        return stockDao.updateAccount(account);// 更新数据
    }

    @Override
    public MyAccountPO previewHoldStock(Long id, boolean isOpenView, boolean isSendMail) {
        MyAccountPO account = getAccount(id);// 获取账号
        String content = generateHtml(account.getMyStockMap().values());// 生成预览界面内容
        // 打开预览
        if (isOpenView) open(writeToFile(String.format(holdStockViewPathnameFormat, id.toString()), content));
        // 发送邮箱
        if (isSendMail) sendMail(id.toString(), content);
        return account;
    }

    @Override
    public MyAccountPO buy(Long id, String symbol, int num, double current) {
        // 1.获取账户
        MyAccountPO account = getAccount(id);
        // 2.购买股票
        account = account.buy(symbol, NumberUtil.toBigDecimal(current), num, BUY_BROKERAGE_RATIO);
        // 3.更新数据
        return stockDao.updateAccount(account);
    }

    @Override
    public MyAccountPO buy(Long id, String symbol, int num) {
        // 1.获取账户
        MyAccountPO account = getAccount(id);
        // 2.找到该股票最新信息
        StockNewDO stockNew = stockApiService.getStockNew(symbol);
        // 3.购买股票
        account = account.buy(symbol, stockNew.getCurrent(), num, BUY_BROKERAGE_RATIO);
        // 4.更新数据
        return stockDao.updateAccount(account);
    }

    @Override
    public List<BaseItemAbs> runStrategy(IStrategyModel strategyService, boolean isOpenView, boolean isSendMail) {
        // 获取策略后的结果
        List<BaseItemAbs> resultList = strategyService.chooseStock(stockApiService, stockApiService.getList("sha,sza"));

        String nowStr = DTF.format(LocalDateTime.now());// 当前时间
        String content = generateHtml(resultList);// 生成预览界面内容
        // 打开预览
        if (isOpenView) open(writeToFile(String.format(holdStockViewPathnameFormat, nowStr), content));
        // 发送邮箱
        if (isSendMail) sendMail(nowStr, content);
        return resultList;
    }

    /**
     * 将内容写入到文件中
     * @param filePathname 文件完整路径
     * @param content 内容
     */
    private String writeToFile(String filePathname, String content) {
        FileUtil.writeUtf8String(content, filePathname);
        return filePathname;
    }

    /**
     * 生成html内容
     * @param stockList 股票信息
     */
    private String generateHtml(Collection<? extends IGenerateHtml<? extends Number>> stockList) {
        String html = "<!DOCTYPE html><html lang=\"cn\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>{0}</title></head><body>{1}</body></html>";
        String item = "<div style=\"display: flex;\"><div style=\"width: 100px;\"><div>{1}</div><div>{0}</div><div>{2,number,#.####}</div></div><div><img src=\"http://image.sinajs.cn/newchart/min/n/{0}.gif\" alt=\"时\" style=\"width: 50%;\"><img src=\"http://image.sinajs.cn/newchart/daily/n/{0}.gif\" alt=\"日K\" style=\"width: 50%;\"></div></div>";
        return MessageFormat.format(html, "K", stockList.stream().map(o -> MessageFormat.format(item, o.getSymbol().toLowerCase(), o.getName(), o.getCurrent())).collect(Collectors.joining()));
    }

    /**
     * 发送邮箱 默认是html内容
     * @param title 标题
     * @param content 内容
     */
    private void sendMail(String title, String content) {
        for (String mail : receiveMails.split(",")) {
            MailUtil.send(mail, title, content, true);
        }
    }

    /**
     * 打开文件
     * @param pathname 文件完整路径
     */
    @SneakyThrows
    private void open(String pathname) {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.contains("Mac")) {
                Runtime.getRuntime().exec("open " + pathname);
            } else if (osName.contains("Windows")) {
                Runtime.getRuntime().exec("cmd /c start " + pathname);
            } else {
                System.out.println("文件输出目录:" + pathname);
            }
        }
    }
}
