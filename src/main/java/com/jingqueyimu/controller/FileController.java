package com.jingqueyimu.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.util.FileUtil;

/**
 * 文件控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    
    /**
     * 上传单个文件
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public ResultData upload(@RequestParam("file") MultipartFile file) {
        String filePath = null;
        try {
            filePath = FileUtil.uploadFile(file.getInputStream(), config.getFileStoragePath(), file.getOriginalFilename());
        } catch (Exception e) {
            log.error("上传文件失败: {}", file.getOriginalFilename(), e);
            throw new AppException("上传失败");
        }
        return ResultData.succ(filePath);
    }
    
    /**
     * 上传多个文件
     *
     * @param files
     * @return
     */
    @RequestMapping("/uploads")
    public ResultData uploads(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            throw new AppException("请选择上传文件");
        }
        List<String> filePaths = new ArrayList<String>();
        for (MultipartFile file : files) {
            try {
                String filePath = FileUtil.uploadFile(file.getInputStream(), config.getFileStoragePath(), file.getOriginalFilename());
                if (StringUtils.isNotBlank(filePath)) {
                    filePaths.add(filePath);
                }
            } catch (Exception e) {
                log.error("上传文件失败: {}", file.getOriginalFilename(), e);
            }
        }
        if (filePaths.isEmpty()) {
            throw new AppException("上传文件失败");
        }
        return ResultData.succ(filePaths);
    }
    
    /**
     * 下载文件
     *
     * @param fileUrl
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public ResultData download(@RequestParam("fileUrl") String fileUrl, HttpServletResponse response) {
        try {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(FileUtil.getFileName(fileUrl), "UTF-8"));
            
            String fileAbsolutePath = FileUtil.getAbsolutePath(config.getFileStoragePath(), fileUrl);
            FileUtil.downloadFile(fileAbsolutePath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
            throw new AppException("下载失败");
        }
        return ResultData.succ();
    }
}
