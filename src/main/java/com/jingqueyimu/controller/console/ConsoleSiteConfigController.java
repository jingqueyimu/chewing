package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.SiteConfig;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.SiteConfigService;
import com.jingqueyimu.util.SysUtil;

/**
 * 网站配置控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/site_config")
public class ConsoleSiteConfigController extends BaseController {
    
    @Autowired
    private SiteConfigService siteConfigService;
    
    /**
     * 分页查询网站配置
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="分页查询网站配置", description="系统管理-网站配置列表-分页查询网站配置")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<SiteConfig> page = siteConfigService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
    
    /**
     * 获取网站配置
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="获取网站配置", description="系统管理-网站配置列表-获取网站配置")
    @RequestMapping("/get")
    public ResultData get(@RequestBody JSONObject params) {
        if (params.isEmpty()) {
            throw new AppException(StatusCode.ERR_PARAM, "查询参数不能为空");
        }
        SiteConfig siteConfig = siteConfigService.get(params);
        return ResultData.succ(siteConfig);
    }
    
    /**
     * 保存网站配置
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="保存网站配置", description="系统管理-网站配置列表-保存网站配置")
    @RequestMapping("/save")
    public ResultData save(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "code", "请输入配置代码");
        SysUtil.checkParam(params, "name", "请输入配置名称");
        SysUtil.checkParam(params, "contentType", "请选择配置内容类型");
        SysUtil.checkParam(params, "content", "请输入配置内容");
        SysUtil.checkParam(params, "publicFlag", "请选择是否公开访问");
        SiteConfig siteConfig = siteConfigService.saveSiteConfig(params);
        return ResultData.succ(siteConfig);
    }
    
    /**
     * 更新网站配置
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="更新网站配置", description="系统管理-网站配置列表-更新网站配置")
    @RequestMapping("/update")
    public ResultData update(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "id", "请选择配置");
        SiteConfig siteConfig = siteConfigService.updateSiteConfig(params);
        return ResultData.succ(siteConfig);
    }
}
