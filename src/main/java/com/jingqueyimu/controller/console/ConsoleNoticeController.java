package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.Notice;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.NoticeService;
import com.jingqueyimu.util.SysUtil;

/**
 * 公告控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/notice")
public class ConsoleNoticeController extends BaseController {
    
    @Autowired
    private NoticeService noticeService;
    
    /**
     * 分页查询公告
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="分页查询公告", description="系统管理-公告列表-分页查询公告")
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
    @Perm(group="system", name="获取公告", description="系统管理-公告列表-获取公告")
    @RequestMapping("/get")
    public ResultData get(@RequestBody JSONObject params) {
        if (params.isEmpty()) {
            throw new AppException(StatusCode.ERR_PARAM, "查询参数不能为空");
        }
        Notice notice = noticeService.get(params);
        return ResultData.succ(notice);
    }
    
    /**
     * 保存公告
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="保存公告", description="系统管理-公告列表-保存公告")
    @RequestMapping("/save")
    public ResultData save(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "title", "请输入标题");
        SysUtil.checkParam(params, "content", "请输入内容");
        Notice notice = JSONObject.toJavaObject(params, Notice.class);
        notice.setReadCount(0);
        notice = noticeService.save(notice);
        return ResultData.succ(notice);
    }
    
    /**
     * 更新公告
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="更新公告", description="系统管理-公告列表-更新公告")
    @RequestMapping("/update")
    public ResultData update(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "id", "请选择公告");
        params.remove("readCount");
        Notice notice = JSONObject.toJavaObject(params, Notice.class);
        notice = noticeService.updateSelective(notice);
        return ResultData.succ(notice);
    }
}
