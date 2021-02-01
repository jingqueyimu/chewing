package com.jingqueyimu.service.component;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jingqueyimu.config.MyConfig;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.RunEnv;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.constant.SysConstant;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.util.BizUtil;

/**
 * 邮件服务
 *
 * @author zhuangyilian
 */
@Service
public class MailService {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private RedisService redisService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MyConfig config;
    @Value("${spring.mail.username}")
    private String from;
    
    /**
     * 发送简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public boolean sendSimpleMail(String to, String subject, String content) {
        try {
            if (RunEnv.DEV.equals(config.getRunEnv())) {
                return true;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.debug("发送简单邮件成功...");
            return true;
        } catch (Exception e) {
            log.error("发送简单邮件异常...", e);
            return false;
        }
    }
    
    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public boolean sendHtmlMail(String to, String subject, String content) {
        try {
            if (RunEnv.DEV.equals(config.getRunEnv())) {
                return true;
            }
            MimeMessage message = mailSender.createMimeMessage();
            // true: 需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // true: 发送内容为html格式
            helper.setText(content, true);
            mailSender.send(message);
            log.debug("发送html邮件成功...");
            return true;
        } catch (MessagingException e) {
            log.error("发送html邮件异常...", e);
            return false;
        }
    }
    
    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public boolean sendAttachmentsMail(String to, String subject, String content, String filePath) {
        try {
            if (RunEnv.DEV.equals(config.getRunEnv())) {
                return true;
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            FileSystemResource res = new FileSystemResource(new File(filePath));
            // 添加附件(添加多个时,多次调用即可)
            helper.addAttachment(fileName, res);
            mailSender.send(message);
            log.debug("发送带附件的邮件成功...");
            return true;
        } catch (MessagingException e) {
            log.error("发送带附件的邮件异常...", e);
            return false;
        }
    }
    
    /**
     * 发送带静态资源的邮件
     * 添加多个图片使用多条 <img src='cid:" + cid + "' > 和  helper.addInline(cid, res) 来实现
     *
     * @param to
     * @param subject
     * @param content
     * @param filePath
     * @param cid
     */
    public boolean sendInlineResourceMail(String to, String subject, String content, String filePath, String cid) {
        try {
            if (RunEnv.DEV.equals(config.getRunEnv())) {
                return true;
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(filePath));
            helper.addInline(cid, res);
            mailSender.send(message);
            log.debug("发送带静态资源的邮件成功...");
            return true;
        } catch (MessagingException e) {
            log.error("发送带静态资源的邮件异常...", e);
            return false;
        }
    }
    
    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    public void sendEmailCode(String email) {
        // 是否已过发送间隔时间
        Object emailCodeObj = redisService.get(CacheConstant.LAST_EMAIL_CODE_TIME + email);
        if (emailCodeObj != null) {
            throw new AppException(StatusCode.OK_REPEAT, "邮箱验证码已发送");
        }
        // 邮箱验证码
        String emailCode = BizUtil.createVerificationCode();
        // 是否开发环境
        if (RunEnv.DEV.equals(config.getRunEnv())) {
            emailCode = "123456";
        }
        String html = BizUtil.buildEmailCodeHtml(config.getSiteName(), emailCode);
        // 发送邮件
        boolean succ = sendHtmlMail(email, "【" + config.getSiteName() + "】会员注册", html);
        if (!succ) {
            throw new AppException(StatusCode.ERR_THIRD, "邮箱验证码发送失败");
        }
        // 缓存邮箱验证码
        redisService.set(CacheConstant.EMAIL_CODE + email, emailCode, SysConstant.EMAIL_CODE_VALID_PERIOD);
        redisService.set(CacheConstant.LAST_EMAIL_CODE_TIME + email, System.currentTimeMillis(), SysConstant.SEND_EMAIL_CODE_INTERVAL);
    }
    
    /**
     * 校验邮箱验证码
     *
     * @param email
     * @param emailCode
     */
    public void checkEmailCode(String email, String emailCode) {
        Object emailCodeObj = redisService.get(CacheConstant.EMAIL_CODE + email);
        if (emailCodeObj == null) {
            throw new AppException(StatusCode.ERR_BIZ, "邮箱验证码失效，请重新获取");
        }
        if (!emailCodeObj.toString().equals(emailCode)) {
            throw new AppException(StatusCode.ERR_BIZ, "邮箱验证码错误");
        }
        redisService.delete(CacheConstant.EMAIL_CODE + email);
        redisService.delete(CacheConstant.LAST_EMAIL_CODE_TIME + email);
    }
}
