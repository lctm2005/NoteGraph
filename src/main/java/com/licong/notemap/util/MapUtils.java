package com.licong.notemap.util;

import java.util.Iterator;
import java.util.Map;

/**
 * <p></p>
 *
 * @author bifeng.liu
 * @since 2015/2/12.
 */
public abstract class MapUtils extends org.apache.commons.collections.MapUtils {

    /**
     * 对Map对象中的所有元素执行某个执行器
     * <p/>
     * 如果map为空或者执行器为NULL，则不处理。
     *
     * @param map
     * @param closure
     */
    public static void forAllDo(Map map, MapClosure closure) {
        if (!isEmpty(map) && closure != null) {
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                closure.execute(entry);
            }
        }
    }
}
