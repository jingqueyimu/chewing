package com.jingqueyimu.handler.excel;

import com.alibaba.fastjson.JSONObject;

/**
 * Excel导入处理器接口
 *
 * @author zhuangyilian
 */
public interface IExcelImportHandler {
    
    /**
     * 导入处理
     *
     * @param data
     * @return
     */
    void handle(JSONObject data);
}
