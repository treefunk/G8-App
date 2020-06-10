package com.myoptimind.g8_app.api;

import com.myoptimind.g8_app.BuildConfig;
import com.myoptimind.g8_app.features.login.AuthService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class G8Api {

    private static Retrofit INSTANCE;

    public static final String BASE_URL = "http://g8.betaprojex.com/api/";


    private static Retrofit create(HttpUrl httpUrl){

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Basic " + BuildConfig.API_KEY).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        if(INSTANCE == null){
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return INSTANCE;
    }

    public static Converter<ResponseBody, ErrorResponse> getConverter(){
        return INSTANCE.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
    }

    // SERVICES

    public static AuthService createAuthService(){
        return create(HttpUrl.parse(BASE_URL)).create(AuthService.class);
    }



}
