package com.jingqueyimu.util;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okHttp工具类
 *
 * @author zhuangyilian
 */
public class OkHttpUtil {
    
    private static OkHttpClient client = new OkHttpClient();

    /**
     * GET请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * POST请求-JSON参数
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String postJson(String url, JSONObject params) {
        Response response = null;
        try {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, params.toJSONString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * POST请求-xml参数
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String postXml(String url, JSONObject params) {
        Response response = null;
        try {
            MediaType mediaType = MediaType.parse("application/xml; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, buildXmlParams(params));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 组装xml参数
     *
     * @param params
     * @return
     */
    public static String buildXmlParams(JSONObject params) {
        String xmlStr = "<xml>";
        for (String key : params.keySet()) {
            xmlStr += "<" + key + ">" + params.getString(key) + "</" + key + ">";
        }
        xmlStr += "</xml>";
        return xmlStr;
    }
}
