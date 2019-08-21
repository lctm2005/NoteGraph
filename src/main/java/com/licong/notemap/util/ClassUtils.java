package com.licong.notemap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 类操作工具类
 */
public abstract class ClassUtils extends org.apache.commons.lang3.ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 载入类通用的错误信息，内部用
     */
    private static final String LOADCLASS_ERROR_MESSAGE = "Unable to load class {0}. Initial cause was {1}";

    /**
     * 先取得当前线程的上下文类载入器，如果取得失败，则使用 <code>ClassUtils</code>的类载入器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * 使用无参数的构造函数，创建一个类的实例，使用标准的ClassLoader
     *
     * @param clazz 类
     * @return 对象
     * @throws RuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object newInstance(Class clazz) throws RuntimeException {
        return newInstance(clazz, new Class[0], new Object[0]);
    }

    /**
     * 使用无参数的构造函数，创建一个类的实例，使用标准的ClassLoader
     *
     * @param className 全路径的类名
     * @return 对象
     * @throws RuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object newInstance(String className) throws RuntimeException {
        return newInstance(className, new Class[0], new Object[0]);
    }

    /**
     * 创建一个类的实例，使用标准的ClassLoader
     *
     * @param clazz    类
     * @param argTypes 构造函数的类型
     * @param args     传构造函数的值
     * @return 对象
     * @throws RuntimeException 如果没有创建成功，则抛出异常
     */
    public static <T> T newInstance(Class<T> clazz, Class[] argTypes, Object[] args) throws RuntimeException {
        Object newInstance;
        try {
            Constructor constructor = clazz.getConstructor(argTypes);
            newInstance = constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, clazz.getName(), e.getCause().getMessage()), e.getCause());
        } catch (Exception e) {
            throw new RuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, clazz.getName(), e.getMessage()), e);
        }
        return (T) newInstance;
    }

    /**
     * 创建一个类的实例，使用标准的ClassLoader
     *
     * @param className 全路径的类名
     * @param argTypes  构造函数的类型
     * @param args      传构造函数的值
     * @return 对象
     * @throws RuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object newInstance(String className, Class[] argTypes, Object[] args) throws RuntimeException {
        Class clazz;
        try {
            clazz = getClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, className, e.getMessage()), e);
        }
        return newInstance(clazz, argTypes, args);
    }

    /**
     * 检查所对应的类名是否可以被载入
     *
     * @param className   类名
     * @param classLoader
     * @return
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            getClass(classLoader, className);
            return true;
        } catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }
}
