package com.licong.notemap.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 集合工具类
 *
 * @bifeng.liu
 * @see org.apache.commons.collections.CollectionUtils
 */
@Slf4j
public abstract class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
    /**
     * 新建集合
     * <p/>
     * 部分没有默认构造函数列表，则使用deepClone，但如果列表里面有大数据量时不推荐使用
     *
     * @param collection 集合
     * @return
     */
    public static Collection newInstance(final Collection collection) {
        //用于保证返回的类型与传入的类型一致
        Collection result = null;
        try {
            //有部分没有默认的构造函数，则使用deepClone
            result = collection.getClass().newInstance();
        } catch (Exception e) {
            result.clear();
        }
        return result;
    }

    /**
     * Set对象转换List对象
     * <p/>
     * 如果Set为Null，返回空List对象
     *
     * @param set Set对象
     * @return
     */
    public static final <T> List<T> setAsList(Set<T> set) {
        if (isEmpty(set)) {
            return new ArrayList<T>(0);
        }
        List<T> result = new ArrayList<T>(set.size());
        Iterator<T> it = set.iterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    /**
     * 遍历集合中所有的对象，抽取对象中指定的属性值，拼装成list返回
     * 如：入参
     * collection传[
     * {
     * "id":1,
     * "name":"jack"
     * },
     * {
     * "id":2,
     * "name":"lucy"
     * }
     * ]
     * propertyName传"name"
     * 回参：["jack","lucy"]
     *
     * @param collection
     * @param propertyName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static List getPropertyList(Collection collection, String propertyName) {
        try {
            final List resultList = new ArrayList();
            new CollectionListener(collection, propertyName) {
                @Override
                protected void onGetPropertyValue(Object member, Object propertyValue) {
                    if (propertyValue != null) {
                        resultList.add(propertyValue);
                    }
                }
            }.iterate();
            return resultList;
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将集合转成map，key为指定字段的值，value为该对象
     * collection传[
     * {
     * "id":1,
     * "name":"jack"
     * },
     * {
     * "id":2,
     * "name":"lucy"
     * }
     * ]
     * propertyName传"name"
     * 回参：
     * ["jack":{
     * "id":1,
     * "name":"jack"
     * },
     * "lucy":{
     * "id":2,
     * "name":"lucy"
     * }]
     *
     * @param collection
     * @param propertyName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Map getPropertyMap(Collection collection, String propertyName) {
        try {
            final Map resultMap = new HashMap();
            new CollectionListener(collection, propertyName) {
                @Override
                protected void onGetPropertyValue(Object member, Object propertyValue) {
                    if (propertyValue != null) {
                        resultMap.put(propertyValue, member);
                    }
                }
            }.iterate();
            return resultMap;
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 将集合按照某个字段做分组， key为指定字段的值，value为对象list
     * 如：入参
     * collection传[
     * {
     * "id":1,
     * "name":"jack"
     * },
     * {
     * "id":2,
     * "name":"lucy"
     * },
     * {
     * "id":3,
     * "name":"jack"
     * }
     * ]
     * propertyName传"name"
     * 回参：
     * ["jack":[{
     * "id":1,
     * "name":"jack"
     * },{
     * "id":3,
     * "name":"jack"
     * }],
     * "lucy":[{
     * "id":2,
     * "name":"lucy"
     * }]]
     *
     * @param collection
     * @param propertyName
     * @return Map<Object,List>
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Map groupBy(Collection collection, String propertyName) {
        try {
            final Map<Object, List> resultMap = new HashMap();
            new CollectionListener(collection, propertyName) {
                @Override
                protected void onGetPropertyValue(Object member, Object propertyValue) {
                    if (propertyValue != null) {
                        List list = resultMap.get(propertyValue);
                        if (list == null) {
                            list = new ArrayList();
                            resultMap.put(propertyValue, list);
                        }
                        list.add(member);
                    }
                }
            }.iterate();
            return resultMap;
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 按照字段名为条件，遍历集合
     */
    private static abstract class CollectionListener {
        private Collection collection;
        private String propertyName;
        private Map<Class, Field> class2FieldMap = new HashMap<>();

        public CollectionListener(Collection collection, String propertyName) {
            this.collection = collection;
            this.propertyName = propertyName;
        }

        /**
         * 取值回调
         *
         * @param member
         * @param propertyValue
         */
        protected abstract void onGetPropertyValue(Object member, Object propertyValue);

        public void iterate() throws NoSuchFieldException, IllegalAccessException {
            if (CollectionUtils.isEmpty(collection)) {
                return;
            }
            for (Object member : collection) {
                if (member == null) {
                    continue;
                }
                Object propertyValue;
                if (member instanceof Map) {
                    propertyValue = ((Map) member).get(propertyName);
                } else {
                    Class memberClass = member.getClass();
                    Field field = class2FieldMap.get(memberClass);
                    if (field == null) {
                        field = BeanUtils.getDeclaredField(member, propertyName);
                        class2FieldMap.put(memberClass, field);
                    }
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    propertyValue = field.get(member);
                    field.setAccessible(accessible);
                }
                onGetPropertyValue(member, propertyValue);
            }
        }
    }
}
