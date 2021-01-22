package com.glimmer.enjoy.fund.repository;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ToStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NotNull Type type, Annotation @NotNull [] annotations, @NotNull Retrofit retrofit) {
        if (String.class.equals(type)) {
            return (Converter<ResponseBody, String>) ResponseBody::string;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NotNull Type type, Annotation @NotNull [] parameterAnnotations,
                                                          Annotation @NotNull [] methodAnnotations, @NotNull Retrofit retrofit) {
        if (String.class.equals(type)) {
            return (Converter<String, RequestBody>) value -> RequestBody.create(MEDIA_TYPE, value);
        }
        return null;
    }
}
