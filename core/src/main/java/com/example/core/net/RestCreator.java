package com.example.core.net;


import com.example.core.app.ConfigKeys;
import com.example.core.app.GlobalContainer;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


/**
 * Created by HP on 2019/1/16.
 */

public class RestCreator {

    private static final class ParamsHolder{
        public static WeakHashMap<String,Object> PARAMS =new WeakHashMap<>();
    }

    public static WeakHashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }


    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE=
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    private static final class RetrofitHolder{
        private static final String BASE_URL= (String) GlobalContainer.getConfigurations()
                .get(ConfigKeys.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT=new Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static final class OKHttpHolder{
        private static  final int TIME_OUT=60;

        /*
        private static final OkHttpClient.Builder BUILDER=new OkHttpClient.Builder();
        //添加拦截器到okhttp
        private static final ArrayList<Interceptor> INTERCEPTORS= (ArrayList<Interceptor>) Latte.getConfiguration(ConfigKeys.INTERCEPTOR);
        //将配置中的拦截器一个一个添加到builder里
        private static OkHttpClient.Builder addInterceptor(){
            if (INTERCEPTORS!=null && !INTERCEPTORS.isEmpty()){
                for (Interceptor interceptor:INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }
        */

        private static final OkHttpClient OK_HTTP_CLIENT=//addInterceptor()
                new OkHttpClient.Builder()
                        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .build();
    }



}
