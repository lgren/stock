package life.stock.controller;

import life.stock.entity.MyAccountPO;
import life.stock.service.IStockService;
import life.stock.service.impl.StockServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("stock")
public class StockController {
    private final IStockService stockService = StockServiceImpl.INSTANCE;

    @GetMapping("getAccount/{id}")
    @ResponseBody
    public MyAccountPO getAccount(@PathVariable Long id) {
        return stockService.getAccount(id);
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }

    // @GetMapping("previewHoldStock/{id}")
    // public MyAccountPO previewHoldStock(@PathVariable Long id, ) {
    //     return stockService.previewHoldStock(id, );
    // }
}
