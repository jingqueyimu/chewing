package com.jingqueyimu.util;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;

/**
 * Dom4J工具类
 *
 * @author zhuangyilian
 */
public class Dom4JUtil {
    
    /**
     * 解析XML为JSON格式
     *
     * @param xml
     * @return
     */
    public static JSONObject xmlToJson(String xml) {
        JSONObject json = new JSONObject();
        try {
            Document document = DocumentHelper.parseText(xml);
            // 根节点
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element childElement = (Element) iterator.next();
                parseElement(childElement, json);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return json;
    }
    
    /**
     * 解析XML元素
     *
     * @param element
     * @param json
     */
    private static void parseElement(Element element, JSONObject json) {
        json.put(element.getName(), element.getData());
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element childElement = (Element) iterator.next();
            parseElement(childElement, json);
        }
    }
}
