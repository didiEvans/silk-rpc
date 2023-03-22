package com.anker.rpc.core.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 属性加载器
 *
 * @author Anker
 */
public class PropertiesLoader {

    private static Properties properties;

    private static final Map<String, String> PROPERTIES_MAP = new HashMap<>();

    public static void loadConfiguration() throws IOException {
        if (properties != null) {
            return;
        }
        properties = new Properties();
        InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(DefaultRpcConfigProperties.DEFAULT_PROPERTIES_FILE);
        properties.load(in);
    }

    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesStr(String key) {
        if (ObjectUtil.isNull(properties) || CharSequenceUtil.isBlank(key)) {
            return null;
        }
        if (!PROPERTIES_MAP.containsKey(key)) {
            String value = properties.getProperty(key);
            PROPERTIES_MAP.put(key, value);
        }
        return PROPERTIES_MAP.get(key) == null ? null : String.valueOf(PROPERTIES_MAP.get(key));
    }

    public static String getPropertiesNotBlank(String key) {
        String val = getPropertiesStr(key);
        if (val == null || val.equals("")) {
            throw new IllegalArgumentException(key + " 配置为空异常");
        }
        return val;
    }

    public static String getPropertiesStrDefault(String key, String defaultVal) {
        String val = getPropertiesStr(key);
        return val == null || val.equals("") ? defaultVal : val;
    }

    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static Integer getPropertiesInteger(String key) {
        if (ObjectUtil.isNull(properties) || CharSequenceUtil.isBlank(key)) {
            return null;
        }
        if (!PROPERTIES_MAP.containsKey(key)) {
            String value = properties.getProperty(key);
            PROPERTIES_MAP.put(key, value);
        }
        return Integer.valueOf(PROPERTIES_MAP.get(key));
    }

    public static Integer getPropertiesIntegerDefault(String key, Integer defaultVal) {
        if (ObjectUtil.isNull(properties) || CharSequenceUtil.isBlank(key)) {
            return defaultVal;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            PROPERTIES_MAP.put(key, String.valueOf(defaultVal));
            return defaultVal;
        }
        if (!PROPERTIES_MAP.containsKey(key)) {
            PROPERTIES_MAP.put(key, value);
        }
        return Integer.valueOf(PROPERTIES_MAP.get(key));
    }

}
