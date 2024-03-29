package com.example.core.net.interceptors;

import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by HP on 2019/1/19.
 */

public abstract class BaseInterceptor implements Interceptor {


    //下边两个方法是从url里取请求的值
    protected LinkedHashMap<String,String> getUrlParameters(Chain chain){
        final HttpUrl url=chain.request().url();
        int size=url.querySize();
        final LinkedHashMap<String,String> params=new LinkedHashMap<>();
        for (int i=0;i<size;i++){
            params.put(url.queryParameterName(i),url.queryParameterValue(i));
        }
        return params;
    }

    protected String getUrlParamsters(Chain chain,String key){
        final Request request=chain.request();
        return request.url().queryParameter(key);
    }

    protected LinkedHashMap<String,String> getBodyParameters(Chain chain){
        final FormBody formBody= (FormBody) chain.request().body();
        final LinkedHashMap<String,String> params=new LinkedHashMap<>();
        int size=formBody.size();
        for (int i=0;i<size;i++){
            params.put(formBody.name(i),formBody.value(i));
        }
        return params;
    }






}
