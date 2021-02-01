package com.jingqueyimu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 数据库初始化工具类
 *
 * @author zhuangyilian
 */
public class DbInitTestUtil2 {
    
    /**
     * 校验数据库初始化数据
     *
     * @param dbInitData
     */
    public static void checkDbInitData(JSONObject dbInitData) {
        String tableName = dbInitData.getString("tableName");
        if (StringUtils.isBlank(tableName)) {
            throw new RuntimeException("tableName不能为空");
        }
        String initKey = dbInitData.getString("initKey");
        if (StringUtils.isBlank(initKey)) {
            throw new RuntimeException("initKey不能为空");
        }
        JSONArray colKeys = JSONObject.parseArray(dbInitData.getString("colKeys"));
        if (colKeys == null || colKeys.isEmpty()) {
            throw new RuntimeException("colKeys不能为空");
        }
        JSONArray maps = dbInitData.getJSONArray("maps");
        if (maps == null || maps.isEmpty()) {
            throw new RuntimeException("maps不能为空");
        }
    }
    
    /**
     * 组装初始化SQL
     *
     * @param dbInitData
     */
    public static List<JSONObject> buildDbInitSql(JSONObject dbInitData) {
        List<JSONObject> sqlDatas = new ArrayList<JSONObject>();
        JSONArray maps = dbInitData.getJSONArray("maps");
        if (maps == null || maps.isEmpty()) {
            return sqlDatas;
        }
        String tableName = dbInitData.getString("tableName");
        maps.forEach(new Consumer<Object>() {

            @Override
            public void accept(Object obj) {
                JSONObject data = (JSONObject) obj;
                // 列名
                String cols = data.getString("cols");
                // 列值
                String vals = data.getString("vals");
                // 列名与列值的映射
                Map<String, String> map = buildColAndValMap(cols, vals);
                
                // 初始化SQL
                StringBuffer initSql = new StringBuffer();
                initSql.append("INSERT INTO ").append(tableName).append(" ").append(cols).append(" VALUES ").append(vals);
                
                // 检查数据唯一性SQL
                StringBuffer checkSql = new StringBuffer();
                if (map != null && !map.isEmpty()) {
                    checkSql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE 1 = 1");
                    for (Entry<String, String> entry : map.entrySet()) {
                        checkSql.append(" AND ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("");
                    }
                }
                
                // SQL数据
                JSONObject sqlData = new JSONObject();
                sqlData.put("initSql", initSql.toString());
                sqlData.put("checkSql", checkSql.toString());
                sqlDatas.add(sqlData);
            }
        });
        return sqlDatas;
    }
    
    /**
     * 组装列名与列值的映射
     *
     * @param cols
     * @param vals
     * @return
     */
    private static Map<String, String> buildColAndValMap(String cols, String vals) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(cols) || StringUtils.isBlank(vals) ) {
            return map;
        }
        cols = cols.replace("(", "").replace(")", "");
        vals = vals.replace("(", "").replace(")", "");
        String[] colArr = cols.split(",");
        String[] valArr = vals.split(",");
        if (colArr.length != valArr.length) {
            throw new RuntimeException("初始化数据异常，列名与列值不匹配");
        }
        for (int i = 0; i < colArr.length; i++) {
            map.put(colArr[i].trim(), valArr[i].trim());
        }
        return map;
    }
}
