package com.ldongxu.util.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author liudongxu06
 * @date 2018/7/26
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    /**
     * 将json字符串传入获得相应对象进行操作
     *
     * @param json
     * @param deObj
     * @return
     * @throws Exception
     */
    public static <T> T decodeJson(String json, Class<T> deObj) {
        T obj = null;
        try {
            obj = gson.fromJson(json, deObj);
        } catch (Exception e) {
            logger.error("jsonUtil解析json失败", e);
        }
        return obj;
    }

    /**
     * 将Object转化为json字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String encodeJson(Object obj) {
        String json = null;
        try {
            json = gson.toJson(obj);
        } catch (Exception e) {
            logger.error("jsonUtil解析json失败", e);
        }
        return json;
    }
    
    /**
     * @param json
     * @param clazz
     * @return
     */
    public static ArrayList<?> jsonToArrayList(String json, Class<?> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
    
    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T[] jsonToArray(String json, Class<T[]> clazz) {
        T[] array = gson.fromJson(json, clazz);
        return array;
    }

    public static <T> T fromJson(String json,Class<T> clazz){
        return gson.fromJson(json,clazz);
    }

    public static <T> T fromJson(String json, Type type){
        return gson.fromJson(json,type);
    }

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static Map<String,Object> toMap(String json){
        return gson.fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static <T> Map<String, T> toMap(String json,Class<T> clz) {
        Map<String, JsonObject> map = gson.fromJson(json, new TypeToken<Map<String, JsonObject>>() {
        }.getType());
        Map<String, T> result = new HashMap<>();
        for (String key : map.keySet()) {
            result.put(key, gson.fromJson(map.get(key), clz));
        }
        return result;
    }

    public static <T> List<T> toList(String json,Class<T> clazz) {
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        List<T> list  = new ArrayList<>();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, clazz));
        }
        return list;
    }

    public static JsonElement getAsJsonElement(String json){
        return new JsonParser().parse(json);
    }

    public static JsonWrapper wrapper(String json){
        return new JsonWrapper(json);
    }

    public static JsonWrapper wrapper(JsonElement jsonElement){
        return new JsonWrapper(jsonElement);
    }

    /**
     * 包装扩展{@linkplain JsonElement},添加功能
     */
    public static class JsonWrapper {
        private static JsonParser parser = new JsonParser();
        private JsonElement jsonElement;

        private JsonWrapper(JsonElement jsonElement) {
            this.jsonElement = jsonElement;
        }

        private JsonWrapper(String json){
            try {
                this.jsonElement = parser.parse(json);
            } catch (Exception e) {
                logger.error("解析json字符串出错,输入:{}", json);
                throw new RuntimeException(e);
            }
        }

        /**
         *json数据是数组时根据下标获取
         */
        public Optional<JsonWrapper> get(int index){
            if (jsonElement.isJsonArray()){
                JsonArray array = jsonElement.getAsJsonArray();
                if (index>=0 && index<array.size()){
                    return Optional.of(new JsonWrapper(array.get(index)));
                }
            }
            return Optional.empty();
        }

        /**
         * 说明：当json数据是数组的时候返回长度，否则返回-1
         */
        public int size() {
            if (jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray().size();
            } else {
                return -1;
            }
        }

        /**
         * 说明：通过"key1.key2[0].key3"的方式获取子节点<br/>
         * 特殊的："" 获取自身，"[0]"为warp为数组时可用
         */
        public Optional<JsonElement> get(String path) {
            String[] paths = path.split("\\.");
            JsonElement je = this.jsonElement;
            for (int i = 0; i < paths.length; i++) {
                String p = paths[i];
                if (p.contains("[")) {
                    // 是数组
                    if (je.isJsonObject() || je.isJsonArray()) {
                        String nodeName = p.substring(0, p.indexOf('['));
                        if (!nodeName.isEmpty()) {
                            JsonObject jo = je.getAsJsonObject();
                            if (jo.has(nodeName)) {
                                je = jo.get(nodeName);
                            } else {
                                return Optional.empty();
                            }
                        }
                        if (je.isJsonArray()) {
                            int index = Integer.parseInt(p.substring(p.indexOf('[') + 1, p.indexOf(']')));
                            JsonArray ja = je.getAsJsonArray();
                            if (index >= 0 && index < ja.size()) {
                                je = ja.get(index);
                            } else {
                                return Optional.empty();
                            }
                        } else {
                            return Optional.empty();
                        }
                    } else {
                        return Optional.empty();
                    }
                } else {
                    if (je.isJsonObject()) {
                        if (!p.isEmpty()) {
                            JsonObject jo = je.getAsJsonObject();
                            if (jo.has(p)) {
                                je = jo.get(p);
                            } else {
                                return Optional.empty();
                            }
                        } else {
                            return Optional.empty();
                        }
                    } else if (je.isJsonPrimitive()) {
                        if (!p.isEmpty()) {
                            return Optional.empty();
                        }
                    } else {
                        return Optional.empty();
                    }
                }
            }
            return Optional.ofNullable(je);
        }

        /**
         * 说明：基于{@linkplain JsonWrapper}
         */
        public Optional<JsonWrapper> getAsJsonWraper(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                return Optional.of(new JsonWrapper(je));
            }
            return Optional.empty();
        }

        /**
         * 说明：获取json原始类型String
         */
        public Optional<String> getAsString(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                if (je.isJsonPrimitive()) {
                    return Optional.of(je.getAsString());
                }
            }
            return Optional.empty();
        }

        public Optional<Integer> getAsInt(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                if (je.isJsonPrimitive()) {
                    if (je.getAsString().isEmpty()) {
                        return Optional.empty();
                    }
                    return Optional.of(je.getAsInt());
                }
            }
            return Optional.empty();
        }

        public Optional<Long> getAsLong(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                if (je.isJsonPrimitive()) {
                    if (je.getAsString().isEmpty()) {
                        return Optional.empty();
                    }
                    return Optional.of(je.getAsLong());
                }
            }
            return Optional.empty();
        }

        public Optional<Double> getAsDouble(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                if (je.isJsonPrimitive()) {
                    if (je.getAsString().isEmpty()) {
                        return Optional.empty();
                    }
                    return Optional.of(je.getAsDouble());
                }
            }
            return Optional.empty();
        }

        public Optional<Boolean> getAsBoolean(String path) {
            Optional<JsonElement> opt = this.get(path);
            if (opt.isPresent()) {
                JsonElement je = opt.get();
                if (je.isJsonPrimitive()) {
                    if (je.getAsString().isEmpty()) {
                        return Optional.empty();
                    }
                    return Optional.of(je.getAsBoolean());
                }
            }
            return Optional.empty();
        }
    }

    private JsonUtil() {
    }
}
