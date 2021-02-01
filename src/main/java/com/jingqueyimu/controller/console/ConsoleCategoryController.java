package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.CategoryService;

/**
 * 类目控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/category")
public class ConsoleCategoryController extends BaseController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 查询主类目
     *
     * @param params
     * @return
     */
    @PostMapping("/list_main")
    public ResultData listMain(@RequestBody JSONObject params) {
        // TODO
        return ResultData.succ(null);
    }
}
