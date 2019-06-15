package com.example.core.net.download;

import android.os.AsyncTask;


import com.example.core.net.RestCreator;
import com.example.core.net.callback.IError;
import com.example.core.net.callback.IFailure;
import com.example.core.net.callback.IRequest;
import com.example.core.net.callback.ISuccess;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2019/1/19.
 */

public class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS= RestCreator.getParams();
    private final IRequest REQUEST;
    //下边三个是关于文件下载的
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final  String NAME;

    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,IRequest request,
                           String downloadDir,String extension,
                           String name,ISuccess success,
                           IFailure failure,IError error
                           ) {
        this.URL=url;
        this.REQUEST=request;
        this.DOWNLOAD_DIR=downloadDir;
        this.EXTENSION=extension;
        this.NAME=name;
        this.SUCCESS=success;
        this.FAILURE=failure;
        this.ERROR=error;
    }

    public final void handleDownload(){
        if (REQUEST!=null){
            REQUEST.onRequestStart();
        }
        RestCreator.getRestService().download(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            final ResponseBody responseBody=response.body();
                            final SaveFileTask task=new SaveFileTask(REQUEST,SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    DOWNLOAD_DIR,EXTENSION,responseBody,NAME);
                            //这里一定要注意是否下载结束，否则下载不全
                            if (task.isCancelled()){
                                if (REQUEST!=null){
                                    REQUEST.onRequestEnd();
                                }
                            }

                        }else {
                            if (ERROR!=null){
                                ERROR.onError(response.code(),response.message());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE!=null){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
