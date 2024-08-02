package com.ldongxu.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author zhengguanglong
 * 数据压缩工具类 *
 */
@Slf4j
public class DataCompressUtil {

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    /**
     * base64 编码
     *
     * @param bytes 传入bytes
     * @return 编码成string类型的base64返回
     */
    public static String base64encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * base64 解码
     *
     * @param string 传入string类型的base64编码
     * @return 解码成byte类型返回
     */
    public static byte[] base64decode(String string) {
        return Base64.getDecoder().decode(string);
    }

    /**
     * gzip 压缩
     *
     * @param data 传入bytes
     * @return 压缩后的byte类型返回
     */
    private static byte[] gzip(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(data);
            gzip.finish();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("GzipUtil error", e);
        }
        return null;
    }

    /**
     * 压缩字符串
     *
     * @param string 需要压缩的字符串
     * @return 压缩后内容 并转base64 返回
     */
    public static String gzip(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        try {
            byte[] zipBytes = gzip(string.getBytes(GZIP_ENCODE_UTF_8));
            if (zipBytes == null) {
                return null;
            }
            return base64encode(zipBytes);
        } catch (Exception e) {
            log.error("GzipUtil error", e);
        }
        return null;
    }

    /**
     * 解压缩字符串
     *
     * @param string base64格式的压缩后字符串
     * @return 解码并解压缩后返回
     */
    public static String unGzip(String string) {
        String result = "";
        if (StringUtils.isBlank(string)) {
            return result;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ungzip = null;
        byte[] bytes = base64decode(string);
        try {
            in = new ByteArrayInputStream(bytes);
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = ungzip.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            result = out.toString(GZIP_ENCODE_UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                if (in != null) {
                    in.close();
                }
                if (ungzip != null) {
                    ungzip.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String str = FileUtils.readFileToString(new File("/Users/liudongxu10/Desktop/test.txt"),"UTF-8");
        String unstr = unGzip(str);
        System.out.println(unstr);
    }
}
