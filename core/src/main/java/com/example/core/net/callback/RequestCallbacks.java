package com.example.core.net.callback;

import android.os.Handler;


import com.example.core.net.loader.LatteLoader;
import com.example.core.net.loader.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2019/1/16.
 */

public class RequestCallbacks implements Callback<String> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;

    private static final Handler HANDLER=new Handler();

    public RequestCallbacks(IRequest request, ISuccess success, IFailure failure, IError error,LoaderStyle loaderStyle) {
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
        LOADER_STYLE = loaderStyle;
    }

    //这里是请求的回调，请求封装时将onResponse和onFailure分成四个返回到接口中

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
            if (call.isExecuted()){
                if (SUCCESS!=null){
                    SUCCESS.onSuccess(response.body());
                }
            }
        }else {
            if (ERROR!=null){
                ERROR.onError(response.code(),response.message());
            }
        }
        //停止loading
       stopLoading();


    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE!=null){
            FAILURE.onFailure();
        }
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
        //停止loading
        stopLoading();
    }

    private void stopLoading(){
        if (LOADER_STYLE!=null){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoading();

                }
            },1000);
        }
    }


}
