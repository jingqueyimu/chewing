package com.jingqueyimu.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 数据库初始化工具类
 *
 * @author zhuangyilian
 */
public class DbInitUtil {
    
    /**
     * 校验数据库初始化数据
     *
     * @param dbInitData
     */
    public static void checkDbInitData(JSONObject dbInitData) {
        String initKey = dbInitData.getString("initKey");
        if (StringUtils.isBlank(initKey)) {
            throw new RuntimeException("initKey不能为空");
        }
        JSONArray sqls = JSONObject.parseArray(dbInitData.getString("sqls"));
        if (sqls == null || sqls.isEmpty()) {
            throw new RuntimeException("sqls不能为空");
        }
    }
    
    /**
     * 获取DML语句中的表名(只支持单表)
     *
     * @param sql
     * @return
     */
    public static String getTableNameForDML(String sql) {
        // SELECT * FROM 表名/(表名) WHERE
        // UPDATE 表名/(表名) SET
        // DELETE FROM 表名 WHERE
        // INSERT [INTO] 表名 [()] VALUES/VALUE
        sql = sql.replace("(", " ").replace(")", " ").trim().toLowerCase();
        String tableName = null;
        if (sql.startsWith("select") || sql.startsWith("delete")) {
            tableName = sql.split("from")[1].trim();
            tableName = tableName.split(" ")[0].trim();
            return tableName;
        }
        if (sql.startsWith("update")) {
            tableName = sql.replace("update", "").trim().split(" ")[0].trim();
            return tableName;
        }
        if (sql.startsWith("insert")) {
            sql = sql.replace("values", "value");
            tableName = sql.replace("insert", "").replace("into", "").trim();
            tableName = tableName.split(" ")[0].trim();
            return tableName;
        }
        return null;
    }
}
