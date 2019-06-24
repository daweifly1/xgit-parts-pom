package com.louis.spring.oauth.client.fastjson;

import com.alibaba.fastjson.serializer.ObjectSerializer;

public class SerializerPair {
    private Class<?> clazz;
    private ObjectSerializer objectSerializer;

    public SerializerPair(Class<?> clazz, ObjectSerializer objectSerializer) {
        this.clazz = clazz;
        this.objectSerializer = objectSerializer;
    }

    public SerializerPair() {
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ObjectSerializer getObjectSerializer() {
        return this.objectSerializer;
    }

    public void setObjectSerializer(ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }
}
