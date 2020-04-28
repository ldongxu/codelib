package com.ldongxu.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author ${author}
 * @since 2019-03-01
 */
@Slf4j
public class PropertiesHelper {
    private Properties pro = null;

    public PropertiesHelper(String path) throws Exception {
        this.pro = this.loadProperty(path);
    }

    public PropertiesHelper(InputStream inputStream) {
        this.pro = new Properties();

        try {
            this.pro.load(inputStream);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public String getString(String key) {
        return this.pro.getProperty(key);
    }

    public Integer getInt(String key) {
        return Integer.valueOf(this.pro.getProperty(key));
    }

    public Double getDouble(String key) {
        return Double.valueOf(this.pro.getProperty(key));
    }

    public Long getLong(String key) throws Exception {
        return Long.valueOf(this.pro.getProperty(key));
    }

    public Float getFloat(String key) throws Exception {
        return Float.valueOf(this.pro.getProperty(key));
    }

    public Boolean getBoolean(String key) throws Exception {
        return Boolean.valueOf(this.pro.getProperty(key));
    }

    public Set<Object> getAllKey() {
        return this.pro.keySet();
    }

    public Collection<Object> getAllValue() {
        return this.pro.values();
    }

    public Map<String, Object> getAllKeyValue() {
        Map<String, Object> mapAll = new HashMap();
        Set<Object> keys = this.getAllKey();
        Iterator it = keys.iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
            mapAll.put(key, this.pro.get(key));
        }

        return mapAll;
    }

    private Properties loadProperty(String filePath) throws Exception {
        FileInputStream fin = null;
        Properties pro = new Properties();

        try {
            fin = new FileInputStream(filePath);
            pro.load(fin);
        } catch (IOException var8) {
            throw var8;
        } finally {
            if (fin != null) {
                fin.close();
            }

        }

        return pro;
    }

}
