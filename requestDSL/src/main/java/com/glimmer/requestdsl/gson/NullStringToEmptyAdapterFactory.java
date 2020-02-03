package com.glimmer.requestdsl.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author Glimmer
 * 2018/08/15
 */
public class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return new StringNullAdapter();
    }

}
