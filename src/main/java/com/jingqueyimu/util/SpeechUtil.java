package com.jingqueyimu.util;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 语音播放工具类
 *
 * @author zhuangyilian
 */
public class SpeechUtil {
    
    /**
     * 文字转语音并播放
     *
     * @param text
     * @param rate 朗读速度 -10~10
     */
    public static void textToSpeech(String text, int rate) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");
            // 运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            // 音量 0-100
            ax.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            ax.setProperty("Rate", new Variant(rate));
            // 执行朗读
            Dispatch.call(spVoice, "Speak", new Variant(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存为语音文件
     *
     * @param text
     * @param rate 朗读速度 -10~10
     * @param storagePath
     * @return
     */
    public static String saveToVoice(String text, int rate, String storagePath) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");
            
            // 运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            
            // 下面是构建文件流把生成语音文件
            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();
            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            // 设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            // 设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            // 调用输出 文件流打开方法,创建一个.mp3文件
            String path = FileUtil.makeDateDirs(storagePath).concat(File.separator).concat(UUID.randomUUID().toString()).concat(".mp3");
            Dispatch.call(spFileStream, "Open", new Variant(path), new Variant(3), new Variant(true));
            // 设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            // 设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            // 设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(rate));
            // 开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(text));

            // 关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        String storagePath = Paths.get("D:/dev-test/file/").toAbsolutePath().toString();
        saveToVoice("订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！订单来啦！", 0, storagePath);
        System.out.println("--- done ----");
    }
}
