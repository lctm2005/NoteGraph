package com.licong.notemap.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * Properties文件载入工具类
 * <p/>
 * 继承处Spring的<code>PropertiesLoaderUtils</code>,增加了根据文件路径载入、文档合并方法。
 *
 * @author bifeng.liu
 * @see org.springframework.core.io.support.PropertiesLoaderUtils
 */
public abstract class PropertiesLoaderUtils extends
        org.springframework.core.io.support.PropertiesLoaderUtils {

    /**
     * 合并两个Properties文件
     *
     * @param target
     * @param source
     */
    public static void merge(Properties target, Properties source) {
        target.putAll(source);
    }

    /**
     * 把source对应的Properties文件合并到目标文件中
     *
     * @param target
     * @param source
     * @throws java.io.IOException
     */
    public static void merge(Properties target, String source) throws IOException {
        target.putAll(loadProperties(source));
    }

    /**
     * 载入类路径目录下的配置文件
     *
     * @param path 路径
     * @return 配置信息
     * @throws IOException
     */
    public static Properties loadProperties(String path) throws IOException {
        Properties props = new Properties();
        Resource resource = new ClassPathResource(path);
        PropertiesLoaderUtils.fillProperties(props, resource);
        return props;
    }
}
