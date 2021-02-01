package com.jingqueyimu.handler.excel;

import java.util.List;

/**
 * Excel导出处理器接口
 *
 * @author zhuangyilian
 */
public interface IExcelExportHandler<T> {

    /**
     * 导出处理
     *
     * @param data
     * @return
     */
    List<Object> handle(T data);
}
