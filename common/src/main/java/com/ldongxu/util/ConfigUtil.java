package com.ldongxu.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * typesafe.config demo
 * <p>
 * Created by 刘东旭 on 2017/6/7.
 */
public class ConfigUtil {
    private final static Config config;

    static {
        config = ConfigFactory.load();
    }

    public static String getString(String var) {
        return config.getString(var);
    }

    public static Config getConfig() {
        return config;
    }

    /**
     * 获取项目所在路径(包括jar)
     */
    public static String getAppPath(){
        URL url = ConfigUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = URLDecoder.decode(url.getPath(),"utf-8");
            if (path.endsWith(".jar")) path = path.substring(0,path.lastIndexOf("/")+1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }

}
