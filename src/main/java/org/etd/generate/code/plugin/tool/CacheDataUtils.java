package org.etd.generate.code.plugin.tool;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CacheDataUtils {

    private volatile static CacheDataUtils cacheDataUtils;

    private CacheDataUtils() {

    }

    public static CacheDataUtils getInstance() {
        if (cacheDataUtils == null) {
            synchronized (CacheDataUtils.class) {
                if (cacheDataUtils == null) {
                    cacheDataUtils = new CacheDataUtils();
                }
            }
        }
        return cacheDataUtils;
    }

    private Map<String, Object> cache = Maps.newConcurrentMap();


    public final static String VAR_SPLIT = ":";


    public String genKey(String... keyMembers) {
        return StringUtils.join(keyMembers, VAR_SPLIT).toUpperCase();
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (StringUtils.isEmpty(key) || ObjectUtils.isEmpty(value)) {
            return;
        }
        cache.putIfAbsent(key, value);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @param target
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> target) {
        return (T) cache.get(key);
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public void remove(String key) {
        if (!cache.containsKey(key)) {
            return;
        }
        cache.remove(key);
    }

}
