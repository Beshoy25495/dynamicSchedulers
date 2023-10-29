package com.bwagih.scheduler.model.common.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

public class CustomObjectMapper extends ObjectMapper {


    private static final CustomObjectMapper instance = new CustomObjectMapper();

    private CustomObjectMapper() {
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        enableDefaultTyping(DefaultTyping.NON_FINAL);
    }

    public CustomObjectMapper(JsonFactory jf) {
        super(jf);
    }

    public CustomObjectMapper(ObjectMapper src) {
        super(src);
    }

    public CustomObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        super(jf, sp, dc);
    }


    @Override
    public ObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
        return super.setVisibility(forMethod, visibility);
    }

    @Override
    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return super.enableDefaultTyping(dti);
    }

    public static CustomObjectMapper getInstance() {
        return instance;
    }
}
