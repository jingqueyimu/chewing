package com.jingqueyimu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 资源工具类
 *
 * @author zhuangyilian
 */
@Component
public class ResourceUtil {
    
    /**
     * 枚举中用于获取枚举信息的方法
     */
    private static final String GET_ENUM_INFO = "getEnumInfo";
    
    /**
     * 属性value
     */
    private static final String VALUE = "value";
    
    /**
     * 资源加载器
     */
    @Autowired
    private ResourceLoader resourceLoader;
    
    /**
     * 获取指定扫描路径下,所有添加了目标注解的方法信息
     *
     * @param classPath 扫描路径
     * @param annotationClass 目标注解类型
     * @return
     * @throws Exception
     */
    public Map<String, Map<String, Object>> getTargetAnnotationMethodInfo(String classPath, Class<?> annotationClass) throws Exception {
        Map<String, Map<String, Object>> resMap = new HashMap<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        // 获取匹配路径的所有资源
        Resource[] resources = resolver.getResources(classPath);
        for (Resource resource : resources) {
            MetadataReader reader = metaReader.getMetadataReader(resource);
            resMap = resolveTargetAnnotationResource(reader, annotationClass, resMap);
        }
        return resMap;
    }

    /**
     * 解析扫描目标注解的资源
     *
     * @param reader
     * @param annotationClass
     * @param resMap
     * @return
     * @throws Exception
     */
    private Map<String, Map<String, Object>> resolveTargetAnnotationResource(MetadataReader reader, Class<?> annotationClass, 
            Map<String, Map<String, Object>> resMap) throws Exception {
        // 指定注解规范名称
        String annotationClassCanonicalName = annotationClass.getCanonicalName();
        // 获取注解元数据
        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        // 获取类中RequestMapping注解的属性
        Map<String, Object> requestMappingAttrs = annotationMetadata.getAnnotationAttributes(RequestMapping.class.getCanonicalName());
        if (requestMappingAttrs == null) {
            return resMap;
        }
        // 获取RequestMapping注解的value
        Object requestMappingValueObj = requestMappingAttrs.get(VALUE);
        if (requestMappingValueObj == null) {
            return resMap;
        }
        String[] parentPaths = (String[]) requestMappingValueObj;
        if (parentPaths.length == 0) {
            return resMap;
        }
        // 获取RequestMapping注解的首个value值(映射的父路径)
        String parentPath = parentPaths[0];

        // 获取当前类中添加了目标注解的方法
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(annotationClassCanonicalName);
        for (MethodMetadata annotatedMethod : annotatedMethods) {
            // 获取当前方法中目标注解的属性
            Map<String, Object> targetAttrs = annotatedMethod.getAnnotationAttributes(annotationClassCanonicalName);
            if (targetAttrs == null) {
                continue;
            }
            // 获取当前方法中xxxMapping注解的属性
            Map<String, Object> mappingAttrs = getMethodMappingPath(annotatedMethod);
            if (mappingAttrs == null) {
                continue;
            }
            String[] childPath = (String[]) mappingAttrs.get(VALUE);
            if (childPath.length == 0) {
                continue;
            }
            String path = parentPath + childPath[0];
            if (resMap.containsKey(path)) {
                throw new RuntimeException("映射关系重复: " + path);
            }
            resMap.put(path, targetAttrs);
        }
        return resMap;
    }

    /**
     * 获取方法映射路径
     *
     * @param annotatedMethod
     * @return
     */
    private Map<String, Object> getMethodMappingPath(MethodMetadata annotatedMethod) {
        Map<String, Object> annotationAttrs = annotatedMethod.getAnnotationAttributes(GetMapping.class.getCanonicalName());
        if (annotationAttrs != null && annotationAttrs.get(VALUE) != null) {
            return annotationAttrs;
        }
        annotationAttrs = annotatedMethod.getAnnotationAttributes(PostMapping.class.getCanonicalName());
        if (annotationAttrs != null && annotationAttrs.get(VALUE) != null) {
            return annotationAttrs;
        }
        annotationAttrs = annotatedMethod.getAnnotationAttributes(DeleteMapping.class.getCanonicalName());
        if (annotationAttrs != null && annotationAttrs.get(VALUE) != null) {
            return annotationAttrs;
        }
        annotationAttrs = annotatedMethod.getAnnotationAttributes(PutMapping.class.getCanonicalName());
        if (annotationAttrs != null && annotationAttrs.get(VALUE) != null) {
            return annotationAttrs;
        }
        annotationAttrs = annotatedMethod.getAnnotationAttributes(RequestMapping.class.getCanonicalName());
        return annotationAttrs;
    }

    /**
     * 加载指定路径下的枚举数据
     *
     * @param classPath
     * @return
     * @throws Exception
     */
    public Map<String, Object> loadEnumData(String classPath) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        // 获取匹配路径的所有资源
        Resource[] resources = resolver.getResources(classPath);
        for (Resource resource : resources) {
            MetadataReader reader = metaReader.getMetadataReader(resource);
            resMap = resolveEnumResource(reader, resMap);
        }
        return resMap;
    }

    /**
     * 解析枚举资源
     *
     * @param reader
     * @param resMap
     * @return
     * @throws Exception
     */
    private Map<String, Object> resolveEnumResource(MetadataReader reader, Map<String, Object> resMap) throws Exception {
        // 获取类元数据
        ClassMetadata classMetadata = reader.getClassMetadata();
        String className = classMetadata.getClassName();
        Class<?> clazz = Class.forName(className);
        // 获取枚举信息的方法,该方法必须自己定义
        Method getEnumInfo = clazz.getMethod(GET_ENUM_INFO);
        if (getEnumInfo == null) {
            return resMap;
        }
        resMap.put(clazz.getSimpleName(), getEnumInfo.invoke(null));
        return resMap;
    }
    
    /**
     * 加载单个键值对配置文件
     *
     * @param classPath
     * @return
     * @throws Exception
     */
    public Map<String, String> loadMapConfig(String classPath) throws Exception {
        Map<String, String> resMap = new HashMap<>();
        Resource resource = resourceLoader.getResource(classPath);
        return resolveMapConfigResource(resource, resMap);
    }
    
    /**
     * 加载多个键值对配置文件
     *
     * @param classPath
     * @return
     * @throws Exception
     */
    public Map<String, String> loadMapConfigs(String classPath) throws Exception {
        Map<String, String> resMap = new HashMap<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources(classPath);
        for (Resource resource : resources) {
            resMap = resolveMapConfigResource(resource, resMap);
        }
        return resMap;
    }
    
    /**
     * 解析键值对配置资源
     *
     * @param resource
     * @param resMap
     * @return
     */
    private Map<String, String> resolveMapConfigResource(Resource resource, Map<String, String> resMap) {
        String data = getTextFromResource(resource, false, true);
        if (data == null || "".equals(data.trim())) {
            return resMap;
        }
        String[] lineDatas = data.split(System.lineSeparator());
        int eqIdx = -1;
        for (String lineData : lineDatas) {
            lineData = lineData.trim();
            eqIdx = lineData.indexOf("=");
            if (eqIdx == -1) {
                continue;
            }
            resMap.put(lineData.substring(0, eqIdx).trim(), lineData.substring(eqIdx + 1).trim());
        }
        return resMap;
    }
    
    /**
     * 加载多个JSON数组配置文件
     *
     * @param classPath
     * @return
     * @throws Exception
     */
    public JSONArray loadJsonArrConfigs(String classPath) throws Exception {
        JSONArray jsonArr = new JSONArray();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources(classPath);
        for (Resource resource : resources) {
            jsonArr = resolveJsonArrConfigResource(resource, jsonArr);
        }
        return jsonArr;
    }
    
    /**
     * 解析JSON数组配置资源
     *
     * @param resource
     * @param jsonArr
     * @return
     */
    private JSONArray resolveJsonArrConfigResource(Resource resource, JSONArray jsonArr) {
        JSONArray arr = JSONArray.parseArray(getTextFromResource(resource, true, true));
        jsonArr.addAll(arr);
        return jsonArr;
    }
    
    /**
     * 从资源中获取包含换行符的文本内容
     *
     * @param resource 资源
     * @param removeLineBreak 是否移除换行符
     * @param removeNoteLine 是否移除注释行
     * @return
     */
    private String getTextFromResource(Resource resource, boolean removeLineBreak, boolean removeNoteLine) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = resource.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuffer data = new StringBuffer();
            String lineData = null;
            boolean isNoteLine = false;
            while((lineData = br.readLine()) != null) {
                if (removeNoteLine) {
                    isNoteLine = lineData.startsWith("#") || lineData.startsWith("//") || lineData.startsWith("--");
                    if (isNoteLine) {
                        continue;
                    }
                }
                data.append(lineData);
                if (!removeLineBreak) {
                    data.append(System.lineSeparator());
                }
            }
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 加载SQL配置文件
     *
     * @param classPath
     * @return
     * @throws Exception
     */
    public List<String> loadSqlConfig(String classPath) throws Exception {
        List<String> resList = new ArrayList<String>();
        Resource resource = resourceLoader.getResource(classPath);
        return resolveSqlConfigResource(resource, resList);
    }
    
    /**
     * 解析SQL配置资源
     *
     * @param resource
     * @param resMap
     * @return
     */
    private List<String> resolveSqlConfigResource(Resource resource, List<String> resList) {
        String data = getTextFromResource(resource, true, true);
        if (data == null || "".equals(data.trim())) {
            return resList;
        }
        String[] sqlDatas = data.split(";");
        for (String sql : sqlDatas) {
            sql = sql.trim();
            resList.add(sql);
        }
        return resList;
    }
}