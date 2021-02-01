package com.jingqueyimu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.mapper.SiteConfigMapper;
import com.jingqueyimu.model.SiteConfig;

/**
 * 网站配置服务
 *
 * @author zhuangyilian
 */
@Service
public class SiteConfigService extends BaseService<SiteConfig> {
    
    @Autowired
    private SiteConfigMapper siteConfigMapper;

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
}
