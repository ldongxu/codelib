package com.ldongxu.util.gson;

import com.google.gson.*;
import com.ldongxu.util.DateUtil;
import com.ldongxu.util.gson.serializer.DoubleDefaultNullAdapter;
import com.ldongxu.util.gson.serializer.IntegerDefaultNullSerializer;
import com.ldongxu.util.gson.serializer.LongDefaultNullAdapter;
import org.apache.commons.lang3.StringUtils;


import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by xali on 15/3/20.
 */
public class AppGsonUtil {
    public static final Gson gson;

    private static class DateTimeSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {

        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();

            SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.DEFAULT_DATETIME_PATTERN);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                System.err.println("Failed to parse Date due to:"+e);
                return null;
            }
        }
    }
    public static class LongDateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            } catch (Exception e) {
                return null;
            }
        }
    }


    static {
        GsonBuilder gb = new GsonBuilder();
//        gb.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                return new Date(json.getAsJsonPrimitive().getAsLong());
//            }
//        });
        gb.registerTypeAdapter(Date.class,new DateTimeSerializer());
//        gb.registerTypeAdapter(Date.class,new DateDeserializer());
//        gb.registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();
        gb.registerTypeAdapter(Integer.class,new IntegerDefaultNullSerializer());
        gb.registerTypeAdapter(Double.class,new DoubleDefaultNullAdapter());
        gb.registerTypeAdapter(Long.class,new LongDefaultNullAdapter());
        gb.registerTypeAdapter(Date.class, new LongDateDeserializer()).setDateFormat(DateFormat.LONG);
        gb.setDateFormat(DateFormat.LONG);
        gson = gb.setDateFormat(DateUtil.DEFAULT_DATETIME_PATTERN)
                .create();
    }

    /**
     * 对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        try {
            return gson.fromJson(str, type);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * json转成对象
     * @param json
     * @param type
     * @return
     */
    public static <T> T fromJson(JsonObject json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        return gson.fromJson(str, type);
    }

    /**
     * json对象转成对象
     * @param json
     * @param type
     * @return
     */
    public static <T> T fromJson(JsonObject json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    /**
     * json字符串转成Json对象
     * @param str
     * @return
     */
    public static JsonElement str2Json(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return new JsonParser().parse(str);
        } catch (Exception e) {

        }
        return null;
    }

    public static String valueOfString(JsonObject jsonObject,String memeber){

        if(null != jsonObject && jsonObject.has(memeber)){
            return jsonObject.get(memeber).getAsString();
        }else{
            return null;
        }

    }

    public static Integer valueOfInteger(JsonObject jsonObject,String memeber){

        if(null != jsonObject && jsonObject.has(memeber)){
            return jsonObject.get(memeber).getAsInt();
        }else{
            return null;
        }

    }

    /**
     * 获取通用的json返回样式
     * @param msg
     * @param errcode
     * @param data
     * @return
     */
    public static JsonElement getCommonJson(String msg,String errcode,JsonElement data) {
        JsonObject retJson = new JsonObject();
        retJson.addProperty("msg",msg);
        retJson.addProperty("errcode",errcode);
        if (data != null) {
            retJson.add("data",data);
        }else {
            retJson.addProperty("data","");
        }
        return retJson;
    }
}
