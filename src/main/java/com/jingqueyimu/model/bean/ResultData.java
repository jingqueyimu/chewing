package com.jingqueyimu.model.bean;

import java.util.Date;

import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.model.BaseModel;

/**
 * 结果数据
 *
 * @author zhuangyilian
 */
public class ResultData extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    private int code;
    
    private String msg;
    
    private Object data;
    
    private Date time = new Date();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public ResultData() {}
    
    public ResultData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    public static ResultData succ() {
        return new ResultData(StatusCode.OK, "成功", null);
    }

    public static ResultData succ(Object data) {
        return new ResultData(StatusCode.OK, "成功", data);
    }
    
    public static ResultData fail() {
        return new ResultData(StatusCode.ERR_SYS, "系统错误", null);
    }
    
    public static ResultData fail(String msg) {
        return new ResultData(StatusCode.ERR_SYS, msg, null);
    }
    
    public static ResultData fail(int code, String msg) {
        return new ResultData(code, msg, null);
    }
}
