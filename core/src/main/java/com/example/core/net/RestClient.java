package com.example.core.net;

import android.content.Context;


import com.example.core.net.callback.IError;
import com.example.core.net.callback.IFailure;
import com.example.core.net.callback.IRequest;
import com.example.core.net.callback.ISuccess;
import com.example.core.net.callback.RequestCallbacks;
import com.example.core.net.download.DownloadHandler;
import com.example.core.net.loader.LatteLoader;
import com.example.core.net.loader.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by HP on 2019/1/4.
 */

public class RestClient {
    //用来建立网络请求的Client
    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS=RestCreator.getParams();
    private final IRequest REQUEST;
    //下边三个是关于文件下载的
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final  String NAME;

    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;


    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final File FILE;


    public RestClient(String url,
                      Map<String, Object> params,
                      String downloadDir,
                      String extension,
                      String name,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      Context context,
                      LoaderStyle loaderStyle,
                      File file
                        ) {
        URL = url;
        PARAMS.putAll(params);
        DOWNLOAD_DIR=downloadDir;
        EXTENSION=extension;
        NAME=name;
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
        BODY = body;
        CONTEXT=context;
        LOADER_STYLE=loaderStyle;
        FILE=file;

    }

    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    //以下方法用于 .get .post 传入相应的方法类型，来用下边的request方法发起请求
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        if (BODY== null){
            request(HttpMethod.POST);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }

    }
    public final void put(){
        if (BODY== null){
            request(HttpMethod.PUT);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete(){
        request(HttpMethod.DELETE);
    }

    public final void upload(){
        request(HttpMethod.UPLOAD);
    }

    public final void download(){
        new DownloadHandler(URL,REQUEST,DOWNLOAD_DIR,
                EXTENSION,NAME,SUCCESS,FAILURE,ERROR)
                .handleDownload();
    }

    private void request(HttpMethod method){
        final RestService service=RestCreator.getRestService();
        Call<String> call=null;

        if (REQUEST!=null){
            REQUEST.onRequestStart();
        }

        //这里是在请求的时候出现加载loading
        if (LOADER_STYLE!=null){
            LatteLoader.showLoading(CONTEXT,LOADER_STYLE);
        }

        switch (method){
            case GET:
                call=service.get(URL,PARAMS);
                break;
            case POST:
                call=service.post(URL,PARAMS);
                break;
            case POST_RAW:
                call=service.postRaw(URL,BODY);
                break;
            case PUT:
                call=service.put(URL,PARAMS);
                break;
            case PUT_RAW:
                call=service.putRaw(URL,BODY);
                break;
            case DELETE:
                call=service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody=
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final MultipartBody.Part body=
                        MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                call=RestCreator.getRestService().upload(URL,body);
                break;
            default:
                break;
        }

        if (call!=null){
            //ececute是在主线程执行
            // enqueue是在后台执行，不会影响主线程
            //Looper.prepare();
            call.enqueue(getRequestCallback());
        }

    }

    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }




}
