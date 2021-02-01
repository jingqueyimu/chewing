package com.jingqueyimu;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingqueyimu.service.component.MailService;

/**
 * 邮件测试
 *
 * @author zhuangyilian
 */
public class MailTest extends BaseTest {
    
    private static final String TO_MAIL = "1010092116@qq.com";
    
    @Autowired
    private MailService mailService;
    
    @Test
    public void testSimpleMail() {
        mailService.sendSimpleMail(TO_MAIL, "test simple mail", "This is a simple mail");
    }
    
    @Test
    public void testHtmlMail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>This is a html email</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail(TO_MAIL, "test html mail", content);
    }
    
    @Test
    public void testAttachmentsMail() {
        String filePath = "D:\\test.txt";
        mailService.sendAttachmentsMail(TO_MAIL, "test attachments mail", "注意查收附件...", filePath);
    }
    
    @Test
    public void testInlineResourceMail() {
        String cid = "pic_test_001";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + cid + "\' ></body></html>";
        String imgPath = "D:\\pic_test.jpg";
        mailService.sendInlineResourceMail(TO_MAIL, "test inline resource mail", content, imgPath, cid);
    }
}
