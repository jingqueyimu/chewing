package com.jingqueyimu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.mapper.SiteConfigMapper;
import com.jingqueyimu.model.SiteConfig;
import com.jingqueyimu.service.component.RedisService;

/**
 * 网站配置服务
 *
 * @author zhuangyilian
 */
@Service
public class SiteConfigService extends BaseService<SiteConfig> {
    
    @Autowired
    private SiteConfigMapper siteConfigMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 保存配置
     *
     * @param params
     * @return
     */
    public SiteConfig saveSiteConfig(JSONObject params) {
        SiteConfig siteConfig = getByCode(params.getString("code"));
        if (siteConfig != null) {
            throw new AppException(StatusCode.ERR_PARAM, "配置代码已存在");
        }
        siteConfig = JSONObject.toJavaObject(params, SiteConfig.class);
        siteConfig = save(siteConfig);
        if (siteConfig.getPublicFlag()) {
            updatePubSiteConfigDataCache(siteConfig);
        }
        return siteConfig;
    }
    
    /**
     * 更新配置
     *
     * @param params
     * @return
     */
    public SiteConfig updateSiteConfig(JSONObject params) {
        params.remove("code");
        SiteConfig siteConfig = JSONObject.toJavaObject(params, SiteConfig.class);
        siteConfig = updateSelective(siteConfig);
        siteConfig = getById(siteConfig.getId());
        if (siteConfig.getPublicFlag()) {
            updatePubSiteConfigDataCache(siteConfig);
        }
        return siteConfig;
    }
    
    /**
     * 根据配置代码查询网站配置
     *
     * @param code
     * @return
     */
    public SiteConfig getByCode(String code) {
        SiteConfig siteConfig = new SiteConfig();        
        siteConfig.setCode(code);
        return siteConfigMapper.selectOne(siteConfig);
    }
    
    /**
     * 根据配置代码查询配置内容
     *
     * @param code
     * @param defaultContent
     * @return
     */
    public String getContent(String code, String defaultContent) {
        SiteConfig siteConfig = getByCode(code);
        if (siteConfig == null || siteConfig.getContent() == null) {
            return defaultContent;
        }
        return siteConfig.getContent();
    }
    
    /**
     * 获取公有网站配置数据
     *
     * @return
     */
    public JSONObject getPubSiteConfigData() {
        Object siteConfigObj = redisService.get(CacheConstant.PUB_SITE_CONFIG);
        if (siteConfigObj != null) {
            return (JSONObject) siteConfigObj;
        }
        JSONObject params = new JSONObject();
        params.put("publicFlag", true);
        List<SiteConfig> list = list(params);
        JSONObject siteConfigData = new JSONObject();
        for (SiteConfig siteConfig : list) {
            siteConfigData.put(siteConfig.getCode(), siteConfig.getContent());
        }
        redisService.set(CacheConstant.PUB_SITE_CONFIG, siteConfigData);
        return siteConfigData;
    }
    
    /**
     * 更新公有网站配置的数据缓存
     *
     * @param siteConfig
     */
    public void updatePubSiteConfigDataCache(SiteConfig siteConfig) {
        JSONObject pubSiteConfigData = getPubSiteConfigData();
        pubSiteConfigData.put(siteConfig.getCode(), siteConfig.getContent());
        redisService.set(CacheConstant.PUB_SITE_CONFIG, pubSiteConfigData);
    }
}
