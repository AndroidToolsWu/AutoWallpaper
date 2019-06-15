package com.example.core.net.interceptors;

import android.support.annotation.RawRes;

import com.example.core.util.file.FileUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by HP on 2019/1/19.
 */

public class DebugInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String debug_url, int debug_raw_id) {
        DEBUG_URL = debug_url;
        DEBUG_RAW_ID = debug_raw_id;
    }
    //获得模拟响应
    private Response getResponse(Interceptor.Chain chain, String json){
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type","application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"),json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }
    //
    private Response debugResponse(Interceptor.Chain chain, @RawRes int rawId){
        final String json= FileUtil.getRawFile(rawId);
        return getResponse(chain,json);
    }


    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //将请求的url变成String
        final String url=chain.request().url().toString();
        //如果url里包含我们能返回的url
        if (url.contains(DEBUG_URL)){
            //就返回他想要的数据
            return debugResponse(chain,DEBUG_RAW_ID);
        }
        //否则原样返回拦截到的请求
        return chain.proceed(chain.request());
    }


}
