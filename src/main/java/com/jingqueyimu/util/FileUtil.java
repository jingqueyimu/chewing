package com.jingqueyimu.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 *
 * @author zhuangyilian
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    public static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 文件上传
     *
     * @param is
     * @param storagePath
     * @param fileName
     * @return
     */
    public static String uploadFile(InputStream is, String storagePath, String fileName) {
        try {
            String datePath = getDatePath();
            String storageDatePath = formatStoragePath(storagePath).concat(datePath);
            // 重命名文件
            fileName = renameFile(fileName);
            // 文件存储路径
            String filePath = makeDirs(storageDatePath).concat(File.separator).concat(fileName);
            Path path = Paths.get(filePath).toAbsolutePath();
            Files.copy(is, path);
            return datePath.concat(File.separator).concat(fileName);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }
    
    /**
     * 文件下载
     *
     * @param fileAbsolutePath
     * @param os
     * @return
     */
    public static boolean downloadFile(String fileAbsolutePath, OutputStream os) {
        return downloadFile(new File(fileAbsolutePath), os);
    }
    
    /**
     * 文件下载
     *
     * @param file
     * @param os
     * @return
     */
    public static boolean downloadFile(File file, OutputStream os) {
        if (file == null) {
            throw new RuntimeException("文件不能为空");
        }
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        if (os == null) {
            throw new RuntimeException("输出流不能为空");
        }
        
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return true;
        } catch (Exception e) {
            log.error("下载文件失败", e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error("关闭流失败", e);
            }
        }  
        return false;
    }
    
    /**
     * 创建日期目录
     *
     * @param storagePath
     * @return
     */
    public static String makeDateDirs(String storagePath) {
        return makeDirs(formatStoragePath(storagePath).concat(getDatePath()));
    }
    
    /**
     * 获取日期目录
     *
     * @return
     */
    public static String getDatePath() {
        return File.separator.concat(sdf.format(new Date()).replace("/", File.separator));
    }
    
    /**
     * 创建目录
     *
     * @return
     */
    public static String makeDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
    
    /**
     * 重命名文件
     *
     * @param fileName
     * @return
     */
    public static String renameFile(String fileName) {
        String newFileName = null;
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex > 1) {
            newFileName = fileName.substring(0, lastIndex).concat("-")
                    .concat(RandomStringUtils.randomAlphanumeric(8))
                    .concat(fileName.substring(lastIndex, fileName.length()));
        } else {
            newFileName = fileName.concat("-").concat(RandomStringUtils.randomAlphanumeric(8));
        }
        return newFileName;
    }
    
    /**
     * 获取文件名
     *
     * @param fileUrl
     * @return
     */
    public static String getFileName(String fileUrl) {
        String[] fileUrls = fileUrl.split("[/|\\\\]");
        if (fileUrls.length > 1) {
            return fileUrls[fileUrls.length - 1].trim();
        } else {
            return fileUrl.trim();
        }
    }
    
    /**
     * 获取绝对路径
     *
     * @param storagePath
     * @param filePath
     * @return
     */
    public static String getAbsolutePath(String storagePath, String filePath) {
        return Paths.get(formatStoragePath(storagePath).concat(formatFilePath(filePath))).toAbsolutePath().toString();
    }
    
    /**
     * 格式化存放路径
     *
     * @param storagePath
     * @return
     */
    private static String formatStoragePath(String storagePath) {
        // 不以分隔符结尾
        if (storagePath.endsWith("/") || storagePath.endsWith("\\")) {
            return storagePath.substring(0, storagePath.length() - 1);
        } else {
            return storagePath;
        }
    }
    
    /**
     * 格式化内部文件路径
     *
     * @param filePath
     * @return
     */
    private static String formatFilePath(String filePath) {
        // 以分隔符开头
        if (filePath.startsWith("/") || filePath.startsWith("\\")) {
            return filePath;
        } else {
            return File.separator.concat(filePath.substring(1, filePath.length()));
        }
    }
}
