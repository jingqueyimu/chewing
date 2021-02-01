package com.jingqueyimu;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.mapper.SqlMapper;
import com.jingqueyimu.model.DbInit;
import com.jingqueyimu.service.DbInitService;
import com.jingqueyimu.util.DbInitTestUtil;
import com.jingqueyimu.util.DbInitTestUtil2;
import com.jingqueyimu.util.ResourceUtil;

/**
 * 数据库初始化测试
 *
 * @author zhuangyilian
 */
public class DBInitTest extends BaseTest {

    @Autowired
    private ResourceUtil resourceUtil;
    @Autowired
    private DbInitService dbInitService;
    @Autowired
    private SqlMapper sqlMapper;
    
    public static void main(String[] args) {
        testGetTableName();
    }
    
    private static void testGetTableName() {
        String sql = "SELECT * FROM t_site_config WHERE id = 1";
        String tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "SELECT * FROM t_site_config";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "SELECT * FROM t_site_config t";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "SELECT * FROM (t_site_config)";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "DELETE FROM t_site_config WHERE id = 1";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "UPDATE t_site_config SET name = '测试' WHERE id = 1";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "INSERT INTO t_site_config (code, name) VALUE ('test', '测试')";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "INSERT INTO t_site_config(code, name) VALUE ('test', '测试')";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "INSERT INTO t_site_config(code, name)VALUE ('test', '测试')";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
        
        sql = "INSERT INTO t_site_config VALUE ('test', '测试')";
        tableName = DbInitTestUtil.getTableNameForDML(sql);
        System.out.println(tableName);
    }
    
    @Test
    public void test() {
        try {
            JSONArray dbInitArr = resourceUtil.loadJsonArrConfigs("classpath:config/test-dbinit.json");
            System.out.println("--------DBInit--------:" + dbInitArr);
            JSONObject dbInitData = null;
            for (Object dbInitObj : dbInitArr) {
                dbInitData = (JSONObject) dbInitObj;
                try {
                    doDbInit(dbInitData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 执行数据库初始化
     *
     * @param dbInitData
     */
    private void doDbInit(JSONObject dbInitData) {
        // 校验数据库初始化数据
        DbInitTestUtil.checkDbInitData(dbInitData);
        String initKey = dbInitData.getString("initKey");
        DbInit dbInit = dbInitService.getByInitKey(initKey);
        if (dbInit != null) {
            return;
        }
        // 初始化SQL
        JSONArray sqls = JSONObject.parseArray(dbInitData.getString("sqls"));
        for (Object sqlObj : sqls) {
            if (sqlObj == null || StringUtils.isBlank(sqlObj.toString())) {
                continue;
            }
            // 插入数据
            sqlMapper.insert(sqlObj.toString());
        }
        // 保存初始化记录
        dbInitService.saveDbInit(initKey, dbInitData.toJSONString());
        System.out.println("初始化数据成功：" + initKey);
    }
    
    @Test
    public void test2() {
        try {
            JSONArray dbInitArr = resourceUtil.loadJsonArrConfigs("classpath:config/test-dbinit2.json");
            System.out.println("--------DBInit2--------:" + dbInitArr);
            JSONObject dbInitData = null;
            for (Object dbInitObj : dbInitArr) {
                dbInitData = (JSONObject) dbInitObj;
                try {
                    doDbInit2(dbInitData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 执行数据库初始化
     *
     * @param dbInitData
     */
    private void doDbInit2(JSONObject dbInitData) {
        // 校验数据库初始化数据
        DbInitTestUtil2.checkDbInitData(dbInitData);
        String initKey = dbInitData.getString("initKey");
        DbInit dbInit = dbInitService.getByInitKey(initKey);
        if (dbInit != null) {
            return;
        }
        // 组装SQL
        List<JSONObject> sqlDatas = DbInitTestUtil2.buildDbInitSql(dbInitData);
        String checkSql = null;
        String initSql = null;
        for (JSONObject sqlData : sqlDatas) {
            checkSql = sqlData.getString("checkSql");
            // 检查数据是否已存在
            if (!StringUtils.isBlank(checkSql)) {
                if (sqlMapper.count(checkSql) > 0) {
                    System.out.println("该数据已初始化：" + sqlData);
                    continue;
                }
            }
            initSql = sqlData.getString("initSql");
            if (StringUtils.isBlank(initSql)) {
                continue;
            }
            // 插入数据
            sqlMapper.insert(initSql);
        }
        // 保存初始化记录
        dbInitService.saveDbInit(initKey, dbInitData.toJSONString());
        System.out.println("初始化数据成功：" + initKey);
    }
}
