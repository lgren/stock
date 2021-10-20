package life.stock.model.strategy;

import life.stock.manage.api.BaseItemAbs;
import life.stock.service.IStockApiService;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IStrategyModel {
    List<BaseItemAbs> chooseStock(IStockApiService stockApi, List<BaseItemAbs> baseList);

    /**
     * 数据筛选简化
     */
    default  <T> List<T> filter(List<T> list, Predicate<T> func) {
        return list.stream().filter(func).collect(Collectors.toList());
    }

    /**
     * 数据筛选简化
     */
    default <T> List<T> filter(Collection<T> coll, Function<T, Double> numberFunc, Double greater, Double less) {
        return coll.stream()
                .filter(o -> greater == null || numberFunc.apply(o) > greater)
                .filter(o -> less == null || numberFunc.apply(o) < less)
                .collect(Collectors.toList());
    }

    /**
     * 数据筛选简化
     */
    default  <T> List<T> filter(Collection<T> coll, Function<T, Double> numberFunc, Double greater, Double less, boolean isEquals) {
        return coll.stream()
                .filter(isEquals
                        ? o -> greater == null || (numberFunc.apply(o) != null && numberFunc.apply(o) >= greater)
                        : o -> greater == null || (numberFunc.apply(o) != null && numberFunc.apply(o) > greater))
                .filter(isEquals
                        ? o -> less == null || (numberFunc.apply(o) != null && numberFunc.apply(o) <= less)
                        : o -> less == null || (numberFunc.apply(o) != null && numberFunc.apply(o) < less))
                .collect(Collectors.toList());
    }
}
