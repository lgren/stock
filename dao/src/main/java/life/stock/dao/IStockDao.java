package life.stock.dao;

import life.stock.config.ConfigData;
import life.stock.entity.MyAccountPO;

public interface IStockDao extends ConfigData {
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
    MyAccountPO getAccount(long id);

    /**
     * 更新账户
     * @param account 账户信息
     */
    MyAccountPO updateAccount(MyAccountPO account);
}
