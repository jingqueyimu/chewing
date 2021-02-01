package com.jingqueyimu.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.annotation.Lock;
import com.jingqueyimu.handler.excel.IExcelImportHandler;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.util.ExcelUtil;

/**
 * 测试控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    
    /**
     * 自定义配置
     *
     * @return
     */
    @RequestMapping(value="/config")
    public ResultData config() {
        return ResultData.succ(config);
    }
    
    /**
     * 分布式锁
     *
     * @param params
     * @return
     */
    @Lock(keys={"params.id"}, waitTime=5, releaseTime=15)
    @PostMapping(value="/lock")
    public ResultData lock(@RequestBody JSONObject params) {
        log.info("===lock===> {}", System.currentTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.succ(System.currentTimeMillis());
    }
    
    /**
     * 分布式锁
     *
     * @param params
     * @return
     */
    @Lock(keys={"key1", "key2"}, waitTime=5, releaseTime=15)
    @RequestMapping(value="/lock2")
    public ResultData lock2(String key1, String key2) {
        log.info("===lock2===> {}", System.currentTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.succ(System.currentTimeMillis());
    }
    
    /**
     * Excel导入
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/importExcel")
    public ResultData importExcel(@RequestParam("file") MultipartFile file) {
        String msg = null;
        try {
            String[] keys = new String[] {"username", "realName", "mobile"};
            msg = ExcelUtil.importExcel(file.getInputStream(), keys, new IExcelImportHandler() {
                
                @Override
                public void handle(JSONObject data) {
                    if (StringUtils.isBlank(data.getString("username"))) {
                        throw new RuntimeException("用户名不能为空");
                    }
                    if (StringUtils.isBlank(data.getString("mobile"))) {
                        throw new RuntimeException("手机号不能为空");
                    }
                    // 业务
                }
            });
            log.info(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.succ(msg);
    }
}
