package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.Product;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.ProductService;

/**
 * 产品控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/product")
public class ConsoleProductController extends BaseController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 分页查询产品
     *
     * @param params
     * @return
     */
    @Perm(group="product", name="分页查询产品", description="产品管理-产品列表-分页查询产品")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Product> page = productService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
}
