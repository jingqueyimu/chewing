package com.jingqueyimu.util;

import java.util.Date;
import java.util.Random;

import com.jingqueyimu.constant.SysConstant;

/**
 * 业务工具类
 *
 * @author zhuangyilian
 */
public class BizUtil {
    
    public static Random random = new Random();
    
    /**
     * 组装业务编号
     *
     * @param bizId
     * @return
     */
    public static String buildBizNo(long bizId) {
        String bizIdStr = String.format("%06d", bizId);
        String randomStr = String.format("%03d", (int) (Math.random() * 1000));
        // 22位
        return System.currentTimeMillis() + bizIdStr + randomStr;
    }
    
    /**
     * 组装队列码
     *
     * @param num
     * @return
     */
    public static String buildQueueCode(int num) {
        Date now = new Date();
        int week = DateUtil.getWeek(now);
        String weekCode = SysConstant.LETTER_LIST[week];
        String queueCode = weekCode + String.format("%03d", num);
        return queueCode;
    }
    
    /**
     * 生成验证码
     *
     * @return
     */
    public static String createVerificationCode() {
        int nextInt = random.nextInt(100000);
        return String.format("%06d", nextInt);
    }
    
    /**
     * 组装邮箱验证码HTML
     *
     * @param signer
     * @param emailCode
     * @return
     */
    public static String buildEmailCodeHtml(String signer, String emailCode) {
        StringBuffer content = new StringBuffer();
        content.append("<html>\r\n");
        content.append("<body>\r\n");
        content.append("<p>【")
            .append(signer)
            .append("】验证码：<font style=\"color:#212529;font-weight:bold\">")
            .append(emailCode)
            .append("</font>，有效期10分钟。如非本人操作，那可能是某位小伙伴手抖了吧！</p>\r\n");
        content.append("</body>\r\n");
        content.append("</html>\r\n");
        return content.toString();
    }
    
    /**
     * 组装短信验证码内容
     *
     * @param signer
     * @param smsCode
     * @return
     */
    public static String buildSmsCodeContent(String signer, String smsCode) {
        StringBuffer content = new StringBuffer();
        content.append("【").append(signer).append("】验证码：").append(smsCode)
            .append("，有效期10分钟。如非本人操作，那可能是某位小伙伴手抖了吧！");
        return content.toString();
    }
}
