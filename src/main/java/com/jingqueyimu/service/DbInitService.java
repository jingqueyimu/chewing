package com.jingqueyimu.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.mapper.DbInitMapper;
import com.jingqueyimu.mapper.SqlMapper;
import com.jingqueyimu.model.DbInit;
import com.jingqueyimu.util.DbInitUtil;

/**
 * 数据库初始化服务
 *
 * @author zhuangyilian
 */
@Service
public class DbInitService extends BaseService<DbInit> {
    
    @Autowired
    private DbInitMapper dbInitMapper;
    @Autowired
    private SqlMapper sqlMapper;
    
    /**
     * 根据初始化标识获取初始化记录
     *
     * @param initKey
     * @return
     */
    public DbInit getByInitKey(String initKey) {
        DbInit dbInit = new DbInit();
        dbInit.setInitKey(initKey);
        return dbInitMapper.selectOne(dbInit);
    }
    
    /**
     * 保存初始化记录
     *
     * @param initKey
     * @param content
     * @return
     */
    public DbInit saveDbInit(String initKey, String content) {
        DbInit dbInit = new DbInit();
        dbInit.setInitKey(initKey);
        dbInit.setContent(content);
        return save(dbInit);
    }
    
    /**
     * 执行数据库初始化
     *
     * @param data
     */
    public void doDbInit(JSONObject data) {
        // 校验数据库初始化数据
        DbInitUtil.checkDbInitData(data);
        String initKey = data.getString("initKey");
        DbInit dbInit = getByInitKey(initKey);
        if (dbInit != null) {
            return;
        }
        // 初始化SQL
        JSONArray sqls = JSONObject.parseArray(data.getString("sqls"));
        for (Object sqlObj : sqls) {
            if (sqlObj == null || StringUtils.isBlank(sqlObj.toString())) {
                continue;
            }
            // 插入数据
            sqlMapper.insert(sqlObj.toString());
        }
        // 保存初始化记录
        saveDbInit(initKey, data.toJSONString());
        log.info("初始化数据成功：{}", initKey);
    }
}
