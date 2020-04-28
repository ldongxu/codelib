package com.ldongxu.util;


import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 读取配置文件
 *
 * @author guweiqiang
 * @date 2018年9月4日
 */
@Slf4j
public class PropertiesUtil {

    private static PropertiesHelper propertiesHelper;

    static {
        try {
            propertiesHelper = new PropertiesHelper( "");
        } catch (Exception e) {
            log.error("加载properties文件失败：" + "", e);
        }
    }

    public static Optional<String> getString(String key) {
        try {
            return Optional.ofNullable(propertiesHelper.getString(key));
        } catch (Exception e) {
            log.error("get" + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Optional<Integer> getInt(String key) {
        try {
            return Optional.of(propertiesHelper.getInt(key));
        } catch (Exception e) {
            log.error("get " + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Optional<Double> getDouble(String key) {
        try {
            return Optional.of(propertiesHelper.getDouble(key));
        } catch (Exception e) {
            log.error("get " + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Optional<Long> getLong(String key) {
        try {
            return Optional.of(propertiesHelper.getLong(key));
        } catch (Exception e) {
            log.error("get " + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Optional<Float> getFloat(String key) {
        try {
            return Optional.of(propertiesHelper.getFloat(key));
        } catch (Exception e) {
            log.error("get " + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Optional<Boolean> getBoolean(String key) {
        try {
            return Optional.of(propertiesHelper.getBoolean(key));
        } catch (Exception e) {
            log.error("get " + key + " properties config exception", e);
        }
        return Optional.empty();
    }

    public static Set<Object> getAllKey() {
        return propertiesHelper.getAllKey();
    }

    public static Collection<Object> getAllValue() {
        return propertiesHelper.getAllValue();
    }

    public static Map<String, Object> getAllKeyValue() {
        return propertiesHelper.getAllKeyValue();
    }

}
