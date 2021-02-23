package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.Notice;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.NoticeService;

/**
 * 公告控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {
    
    @Autowired
    private NoticeService noticeService;
    
    /**
     * 分页查询公告
     *
     * @param params
     * @return
     */
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Notice> page = noticeService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }

    /**
     * 获取公告
     *
     * @param params
     * @return
     */
    @RequestMapping("/get")
    public ResultData get(@RequestBody JSONObject params) {
        if (params.isEmpty()) {
            throw new AppException(StatusCode.ERR_PARAM, "查询参数不能为空");
        }
        Notice notice = noticeService.get(params);
        return ResultData.succ(notice);
    }
}
