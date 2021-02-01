package com.jingqueyimu.controller;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.component.CaptchaService;
import com.jingqueyimu.util.SysUtil;
import com.jingqueyimu.util.ResourceUtil;

/**
 * 通用控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {
    
    /**
     * 枚举数据
     */
    private static Map<String, Object> enumData = new HashMap<String, Object>();
    /**
     * 枚举字典路径
     */
    private static String ENUM_DICT_PATH = "classpath*:com/jingqueyimu/model/dict/*.class";
    
    @Autowired
    private ResourceUtil resourceUtil;
    @Autowired
    private CaptchaService captchaService;
    
    /**
     * 获取单个字典
     *
     * @param params
     * @return
     */
    @PostMapping(value="/dict")
    public ResultData dict(@RequestBody JSONObject params) {
        SysUtil.checkParams(params, "name");
        String name = params.getString("name");
        if (enumData == null || enumData.isEmpty()) {
            try {
                enumData = resourceUtil.loadEnumData(ENUM_DICT_PATH);
            } catch (Exception e) {
                log.error("加载枚举数据失败", e);
            }
        }
        Object enumObj = enumData.get(name);
        return ResultData.succ(enumObj);
    }
    
    /**
     * 获取多个字典
     *
     * @param params
     * @return
     */
    @PostMapping(value="/dicts")
    public ResultData dicts(@RequestBody JSONObject params) {
        SysUtil.checkParams(params, "names");
        JSONArray names = params.getJSONArray("names");
        if (enumData == null || enumData.isEmpty()) {
            try {
                enumData = resourceUtil.loadEnumData(ENUM_DICT_PATH);
            } catch (Exception e) {
                log.error("加载枚举数据失败", e);
            }
        }
        Map<String, Object> data = new HashMap<>();
        for (Object nameObj : names) {
            if (nameObj == null) {
                continue;
            }
            data.put(nameObj.toString(), enumData.get(nameObj));
        }
        return ResultData.succ(data);
    }
    
    /**
     * 图形验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            throw new AppException(StatusCode.ERR_PARAM, "token不能为空");
        }
        ServletOutputStream os = null;
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 生成图形验证码
            Image image = captchaService.createCaptcha(token);
            os = response.getOutputStream();
            ImageIO.write((RenderedImage) image, "jpeg", os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
