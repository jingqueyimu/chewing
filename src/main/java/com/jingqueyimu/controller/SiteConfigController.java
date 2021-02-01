package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.SiteConfig;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.SiteConfigService;

/**
 * 网站配置控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/site_config")
public class SiteConfigController extends BaseController {
    
    @Autowired
    private SiteConfigService siteConfigService;
    
    /**
     * 获取配置
     *
     * @param params
     * @return
     */
    @RequestMapping("/get")
    public ResultData get(@RequestBody JSONObject params) {
        params.put("publicFlag", true);
        SiteConfig siteConfig = siteConfigService.get(params);
        return ResultData.succ(siteConfig);
    }
}
