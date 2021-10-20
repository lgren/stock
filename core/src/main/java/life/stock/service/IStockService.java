package life.stock.service;

import life.stock.config.ConfigData;
import life.stock.entity.MyAccountPO;
import life.stock.manage.api.BaseItemAbs;
import life.stock.model.strategy.IStrategyModel;

import java.util.List;

public interface IStockService extends ConfigData {
    /**
     * 跑策略
     * @param strategyService {@link IStrategyModel}的实现类门
     * @param isOpenView 是否打开预览 打开一个离线html界面
     * @param isSendMail 是否发送邮箱
     */
    List<BaseItemAbs> runStrategy(IStrategyModel strategyService, boolean isOpenView, boolean isSendMail);

    /**
     * 新增账户
     * @param id 账户id
     * @param name 账户名称
     * @param money 初始化金额
     */
    MyAccountPO addAccount(long id, String name, double money);

    /**
     * 获取账户
     * @param id 账户id
     */
    MyAccountPO getAccount(Long id);

    /**
     * 刷新账户
     * 通过调用接口等方式刷新到最新数据
     * @param id 账户id
     */
    MyAccountPO refreshAccount(Long id);

    /**
     * 预览持有股票
     * @param id 账户id
     * @param isOpenView 是否打开预览 打开一个离线html界面
     * @param isSendMail 是否发送邮箱
     */
    MyAccountPO previewHoldStock(Long id, boolean isOpenView, boolean isSendMail);

    /**
     * 购买股票
     * 作为测试使用, 可手动输入价格
     * @param id 账户id
     * @param symbol 股票编号
     * @param num 数量
     * @param currentVar 当前价格
     */
    @Deprecated
    MyAccountPO buy(Long id, String symbol, int num, double currentVar);

    /**
     * 购买股票
     * @param id 账户id
     * @param symbol 股票编号
     * @param num 数量
     */
    MyAccountPO buy(Long id, String symbol, int num);
}
