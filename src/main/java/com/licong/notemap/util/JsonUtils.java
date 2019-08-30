package com.licong.notemap.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.ByteArrayOutputStream;

/**
 * <p>对象操作的工具类</p>
 *
 * @author bifeng.liu
 * @since 2015/2/15.
 */
public class JsonUtils {

    /**
     * Jackson Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 把对象转换成JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(baos, JsonEncoding.UTF8);
            if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                jsonGenerator.useDefaultPrettyPrinter();
            }
            objectMapper.writeValue(jsonGenerator, obj);
            return baos.toString("UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }


    /**
     * JSON字符串转换成对象
     *
     * @param str              json格式字符串
     * @param clazz            对象class
     * @param parameterClasses 对象泛型参数class
     * @return
     */
    public static <T> T fromJson(String str, Class<T> clazz, Class<?>... parameterClasses) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        JavaType javaType = null;
        if (ArrayUtils.isEmpty(parameterClasses)) {
            javaType = objectMapper.getTypeFactory().constructType(clazz);
        } else {
            javaType = objectMapper.getTypeFactory().constructParametrizedType(clazz, clazz, parameterClasses);
        }
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception ex) {
            throw new RuntimeException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * JSON字符串转换成对象
     *
     * @param str           json格式字符串
     * @param typeReference 类型参数
     * @return
     */
    public static <T> T fromJson(String str, final TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (Exception ex) {
            throw new RuntimeException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}
