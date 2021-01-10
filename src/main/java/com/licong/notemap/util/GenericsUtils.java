package com.licong.notemap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型的工具类.
 *
 * @author bifeng.liu
 */
public abstract class GenericsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericsUtils.class);

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     * <p/>
     * 如<code>public BookManager extends GenricManager<Book></code>
     * <p/>
     * 该方法会循环往上级类查找第一个泛型参数，直接找到或者父类为Object，
     * 如果仅仅想找当前类的父类泛型具化类型，使用<code>getSuperClassGenricType(Class, int)</code>
     *
     * @param clazz 要操作的类
     * @return 返回索引所对应的泛型参数类型，如果没有定义则返回<code>Object.class</code>
     * @see #getSuperClassGenericType(Class, int)
     */
    public static Class getSuperClassGenericType(Class clazz) {
        Class type = getSuperClassGenericType(clazz, 0);
        if (type.equals(Object.class)) {
            if (Object.class.equals(clazz.getSuperclass())) return type;
            type = getSuperClassGenericType(clazz.getSuperclass());
        }
        return type;
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     * <p/>
     * 如<code>public BookManager extends GenricManager<Book></code>
     *
     * @param clazz 要操作的类
     * @param index 泛型参数定义的索引，从0开始
     * @return 返回索引所对应的泛型参数类型，如果没有定义则返回<code>Object.class</code>
     */
    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type[] params = getSuperClassGenericTypes(clazz);
        if (index >= params.length || index < 0) {
            LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }


    /**
     * 通过反射,获得定义Class时声明的父类的所有泛型参数的类型.
     * <p/>
     * 如public BookManager extends GenricManager<Book,Person>
     *
     * @param clazz 要操作的类
     * @return 泛型参数类型列表，如果没有则返回空类型数组
     */
    public static Type[] getSuperClassGenericTypes(Class clazz) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return new Type[0];
        }
        return ((ParameterizedType) genType).getActualTypeArguments();
    }

    /**
     * 通过反射,获得Field泛型所有参数的实际类型列表. 如: public Map<String, Buyer> names;
     *
     * @param field 字段
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     */
    public static Type[] getFieldGenericTypes(Field field) {
        Type genericFieldType = field.getGenericType();
        if (!(genericFieldType instanceof ParameterizedType)) {
            LOGGER.warn("Field " + field.getName() + " has not ParameterizedType");
            return new Type[0];
        }
        return ((ParameterizedType) genericFieldType).getActualTypeArguments();
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型. 如: public Map<String, Buyer> names;
     *
     * @param field 字段
     * @param index 泛型参数所在索引,从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     */
    public static Class getFieldGenericType(Field field, int index) {
        Type genericFieldType = field.getGenericType();
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (index >= fieldArgTypes.length || index < 0) {
                LOGGER.warn("Index: " + index + ", Size of Field " + field.getName() + "'s Parameterized Type: " + fieldArgTypes.length);
                return Object.class;
            }
            return (Class) fieldArgTypes[index];
        }
        return Object.class;
    }


}
