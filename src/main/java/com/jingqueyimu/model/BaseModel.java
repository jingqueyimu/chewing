package com.jingqueyimu.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * 基类模型
 *
 * @author zhuangyilian
 */
//@MappedSuperclass
public abstract class BaseModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return this.getClass().getName() + "=" + toJsonString();
    }
    
    public String toJsonString() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        return JSONObject.toJSONString(this, serializeConfig, SerializerFeature.SortField, SerializerFeature.WriteMapNullValue);
    }
}