package cn.com.xgit.parts.common.util.fastjson;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class FastJsonUtil {
    private FastJsonUtil() {
    }

    public static SerializerPair buildSerializerPair(Class<?> clazz, ObjectSerializer objectSerializer) {
        return new SerializerPair(clazz, objectSerializer);
    }

    public static String toJSONString(Object obj) {
        return toJSONString(obj, SerializeConfig.getGlobalInstance(), SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJSONString(Object obj, SerializerFeature... serializerFeatures) {
        return toJSONString(obj, (SerializeConfig) SerializeConfig.getGlobalInstance(), (SerializeFilter[]) null, serializerFeatures);
    }

    public static String toJSONString(Object obj, SerializeFilter[] filters, SerializerFeature... serializerFeatures) {
        return toJSONString(obj, SerializeConfig.getGlobalInstance(), filters, serializerFeatures);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfigs, SerializeFilter[] filters, SerializerFeature... serializerFeatures) {
        if (null != serializerFeatures && 0 != serializerFeatures.length) {
            boolean flg = false;
            SerializerFeature[] var5 = serializerFeatures;
            int var6 = serializerFeatures.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                SerializerFeature serializerFeature = var5[var7];
                if (serializerFeature.getMask() == SerializerFeature.DisableCircularReferenceDetect.getMask()) {
                    flg = true;
                }
            }

            if (!flg) {
                List<SerializerFeature> featureList = new ArrayList(Arrays.asList(serializerFeatures));
                featureList.add(SerializerFeature.DisableCircularReferenceDetect);
                serializerFeatures = (SerializerFeature[]) featureList.toArray(serializerFeatures);
            }
        } else {
            serializerFeatures = new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect};
        }

        return null == filters ? JSON.toJSONString(obj, serializeConfigs, serializerFeatures) : JSON.toJSONString(obj, serializeConfigs, filters, serializerFeatures);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfigs, SerializerFeature... serializerFeatures) {
        return toJSONString(obj, (SerializeConfig) serializeConfigs, (SerializeFilter[]) null, serializerFeatures);
    }

    public static String toJSONString(Object obj, SerializerPair[] serializerPairs, SerializeFilter[] filters, SerializerFeature... serializerFeatures) {
        SerializeConfig serializeConfig = new SerializeConfig();
        if (null != serializerPairs && 0 != serializerPairs.length) {
            SerializerPair[] var5 = serializerPairs;
            int var6 = serializerPairs.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                SerializerPair serializerPair = var5[var7];
                serializeConfig.put(serializerPair.getClazz(), serializerPair.getObjectSerializer());
            }
        }

        return toJSONString(obj, serializeConfig, filters, serializerFeatures);
    }

    public static String toJSONString(Object obj, SerializerPair[] serializerPairs, SerializerFeature... serializerFeatures) {
        return toJSONString(obj, (SerializerPair[]) serializerPairs, (SerializeFilter[]) null, serializerFeatures);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            return !json.trim().startsWith("{") ? null : JSON.parseObject(json, clazz, new Feature[]{Feature.AllowUnQuotedFieldNames});
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList();
        } else {
            return (List) (!json.trim().startsWith("[") ? new ArrayList() : JSON.parseArray(json, clazz));
        }
    }

    public static <T> List<T> parseList(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList();
        if (null != jsonArray && !jsonArray.isEmpty()) {
            for (int inc = 0; inc < jsonArray.size(); ++inc) {
                list.add(jsonArray.getObject(inc, clazz));
            }

            return list;
        } else {
            return list;
        }
    }

    public static Map<String, Object> parseMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        } else {
            return (Map) (!json.trim().startsWith("{") ? new HashMap() : JSON.parseObject(json, new Feature[]{Feature.AllowUnQuotedFieldNames}));
        }
    }

    public static Map parseMap2(String json) {
        return parseMap(json);
    }

    public static List<Map<String, Object>> parseListMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList();
        } else {
            List<JSONObject> list = parseList(json, JSONObject.class);
            List<Map<String, Object>> result = new ArrayList();
            Iterator var3 = list.iterator();

            while (var3.hasNext()) {
                JSONObject jsonObject = (JSONObject) var3.next();
                result.add(jsonObject);
            }

            return result;
        }
    }

    public static List<Map> parseListMap2(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList();
        } else {
            List<Map<String, Object>> list = parseListMap(json);
            List<Map> result = new ArrayList();
            Iterator var3 = list.iterator();

            while (var3.hasNext()) {
                Map<String, Object> map = (Map) var3.next();
                result.add(map);
            }

            return result;
        }
    }

    public static List<Map<String, String>> parseListMap3(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList();
        } else {
            List<Map<String, Object>> list = parseListMap(json);
            List<Map<String, String>> result = new ArrayList();
            Iterator var3 = list.iterator();

            while (var3.hasNext()) {
                Map<String, Object> map = (Map) var3.next();
                Map<String, String> strMap = new HashMap();
                Iterator var6 = map.keySet().iterator();

                while (var6.hasNext()) {
                    String key = (String) var6.next();
                    if (null != map.get(key)) {
                        strMap.put(key, map.get(key).toString());
                    }
                }

                result.add(strMap);
            }

            return result;
        }
    }
}
