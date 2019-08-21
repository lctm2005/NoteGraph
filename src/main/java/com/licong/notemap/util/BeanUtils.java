package com.licong.notemap.util;


import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 *
 * @author bifeng.liu
 * @see org.apache.commons.beanutils.BeanUtils
 */
public abstract class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 循环向上，获取类的所有DeclaredField
     *
     * @param clazz 类
     */
    public static Field[] getDeclaredFields(Class clazz) {
        Field[] fields = new Field[]{};
        for (Class superClass = clazz; superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
            fields = (Field[]) ArrayUtils.addAll(fields, superClass.getDeclaredFields());
        }
        return fields;
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        return getDeclaredField(object.getClass(), propertyName);
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
        Assert.notNull(clazz);
        Assert.hasText(propertyName);
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        throw new NoSuchFieldException("The class[" + clazz.getName() + "] has no such field[" + propertyName + "]");
    }

    /**
     * 暴力获取对象变量值,忽略private,protected修饰符的限制.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LOGGER.info("error has happen", e);
        }
        field.setAccessible(accessible);
        return result;
    }

    /**
     * 暴力设置对象变量值,忽略private,protected修饰符的限制.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static void forceSetProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            LOGGER.info("Error has happen", e);
        }
        field.setAccessible(accessible);
    }

    /**
     * 循环向上，获取类的所有DeclaredMethod
     *
     * @param clazz 类
     */
    public static Method[] getDeclaredMethods(Class clazz) {
        Method[] methods = new Method[]{};
        for (Class superClass = clazz; superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
            methods = ArrayUtils.addAll(methods, superClass.getDeclaredMethods());
        }
        return methods;
    }


    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * <p/>
     * types参数要严格一一对应，无法支持父类和子类的匹配
     *
     * @param object     对象
     * @param methodName 方法名称
     * @param types      参数类型
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */

    public static Method getDeclaredMethod(Object object, String methodName, Class... types) throws NoSuchMethodException {
        Assert.notNull(object);
        Assert.hasText(methodName);
        return getDeclaredMethod(object.getClass(), methodName, types);
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * <p/>
     * types参数要严格一一对应，无法支持父类和子类的匹配
     *
     * @param clazz      类
     * @param methodName 方法名称
     * @param types      参数类型
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */

    public static Method getDeclaredMethod(Class clazz, String methodName, Class... types) throws NoSuchMethodException {
        Assert.notNull(clazz);
        Assert.notNull(methodName);
        for (Class superClass = clazz; superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, types);
            } catch (NoSuchMethodException e) {
                // 方法不在当前类定义,继续向上转型
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The class[").append(clazz.getName()).append("] has no such method[").append(methodName).append("(");
        for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            if (i > 0) {
                sb.append(",");
            }
            sb.append(type.toString());
        }
        sb.append(")]");
        throw new NoSuchMethodException(sb.toString());
    }

    /**
     * 暴力调用对象函数,忽略private,protected修饰符的限制.
     *
     * @param object     对象
     * @param methodName 方法名称
     * @param types      参数类型
     * @param params     参数
     * @return
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */
    public static Object forceInvokeMethod(Object object, String methodName, Class[] types, Object... params) throws NoSuchMethodException {
        Assert.notNull(object);
        Assert.hasText(methodName);
        Method method = getDeclaredMethod(object.getClass(), methodName, types);
        return forceInvokeMethod(object, method, params);
    }

    /**
     * 暴力调用对象函数,忽略private,protected修饰符的限制.
     *
     * @param object     对象
     * @param methodName 方法名称
     * @param params     参数
     * @return
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */
    public static Object forceInvokeMethod(Object object, String methodName, Object... params) throws NoSuchMethodException {
        Assert.notNull(object);
        Assert.hasText(methodName);
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method method = getDeclaredMethod(object.getClass(), methodName, types);
        return forceInvokeMethod(object, method, params);
    }

    /**
     * 暴力调用对象函数,忽略private,protected修饰符的限制.
     *
     * @param object 对象
     * @param method 方法
     * @param params 参数
     * @return
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */
    public static Object forceInvokeMethod(Object object, Method method, Object... params) {
        Assert.notNull(object);
        Assert.notNull(method);
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(object, params);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        } finally {
            method.setAccessible(accessible);
        }
        return result;
    }

    /**
     * 把Map中的值填入到对象中
     * <p/>
     * Map中值必须以xxx.开头，否则将不会被填入，
     * 如果beanName参数没有传入，则使用对象所对应类的名称
     *
     * @param bean
     * @param properties
     * @param beanName
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static void populate(Object bean, Map<String, ? extends Object> properties, String beanName)
            throws IllegalAccessException, InvocationTargetException {
        // 如果bean和properties传入为，则直接返回
        if ((bean == null) || (properties == null)) {
            return;
        }
        if (!StringUtils.hasText(beanName)) {
            beanName = StringUtils.uncapitalize(bean.getClass().getSimpleName());
        }
        String preKey = beanName + ".";
        int len = preKey.length();
        Map<String, Object> filterProps = new HashMap<String, Object>();
        for (Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            String name = entry.getKey();
            if (name == null || !name.startsWith(preKey)) {
                continue;
            }
            name = name.substring(len);
            filterProps.put(name, entry.getValue());
        }
        BeanUtilsBean.getInstance().populate(bean, filterProps);
    }

    /**
     * 把对象中值不为null的栏位转换成Map
     * <p/>
     * 该方法是对对象除静态和短暂的所有栏位进行处理
     *
     * @param bean 对象
     * @return
     */
    public static Map<String, Object> toMap(Object bean) throws IllegalAccessException {
        return toMap(bean, false, false);
    }

    /**
     * 把对象的栏位转换成Map
     * <p/>
     * 当nullable为true时，会把值为null的栏位输出到Map中，
     * 当isPascal为true时，返回的Map中Key会使用Pascal命名方式，即首字母大写
     * <p/>
     * 该方法是对对象除静态和短暂的所有栏位进行处理
     *
     * @param bean     对象
     * @param nullable 是否支持null
     * @param isPascal 返回是否为Pascal命名
     * @return
     */
    public static Map<String, Object> toMap(Object bean, boolean nullable, boolean isPascal) throws IllegalAccessException {
        Assert.notNull(bean);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            for (Class superClass = bean.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
                Field[] fields = superClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    // 获取原来的访问控制权限
                    boolean accessible = field.isAccessible();
                    // 修改访问控制权限
                    field.setAccessible(true);
                    Object value = field.get(bean);
                    // TODO  Collection Map Array 处理
                    // 不处理静态以及短暂的栏位
                    if ((value != null || nullable) && !Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                        String key = isPascal ? StringUtils.capitalize(field.getName()) : field.getName();
                        result.put(key, value);
                    }
                    // 恢复访问控制权限
                    field.setAccessible(accessible);
                }
            }
        } catch (IllegalAccessException ex) {
            LOGGER.warn("The object convert to map happen error", ex);
            throw ex;
        }
        return result;
    }
}
