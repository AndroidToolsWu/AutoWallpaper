package com.example.core.net;

import android.content.Context;


import com.example.core.net.callback.IError;
import com.example.core.net.callback.IFailure;
import com.example.core.net.callback.IRequest;
import com.example.core.net.callback.ISuccess;
import com.example.core.net.loader.LoaderStyle;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by HP on 2019/1/16.
 */

public class RestClientBuilder {
    private  String mUrl=null;
    private  static Map<String,Object> PARAMS=RestCreator.getParams();
    //下边三个是下载
    private String mDownloadDir=null;
    private String mExtension=null;
    private String mName=null;

    private IRequest mRequest=null;
    private ISuccess mSuccess=null;
    private IFailure mFailure=null;
    private IError mError=null;
    private  RequestBody mBody=null;
    private LoaderStyle mLoadersStyle=null;
    private Context mContext=null;
    private File mFile=null;

    RestClientBuilder(){
    }


    //以下公开方法是建造者模式用来赋值所用
    public final RestClientBuilder url (String url) {
        mUrl=url;
        return this;
    }

    public final RestClientBuilder params(Map<String,Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key,Object value){
        PARAMS.put(key,value);
        return this;
    }


    public final RestClientBuilder file(File file){
        mFile=file;
        return this;
    }
    public final RestClientBuilder file(String file){
        mFile=new File(file);
        return this;
    }
    //关于下载文件
    public final RestClientBuilder dir(String dir){
        this.mDownloadDir=dir;
        return this;
    }
    //关于下载文件
    public final RestClientBuilder extension(String extension){
        this.mExtension=extension;
        return this;
    }

    //关于下载文件
    public final RestClientBuilder name(String name){
        mName=name;
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody=RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest){
        mRequest=iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        mSuccess=iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        mFailure=iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        mError=iError;
        return this;
    }

    //这个方法是设置loadStyle
    public final RestClientBuilder loader(Context context,LoaderStyle loaderStyle){
        this.mContext=context;
        this.mLoadersStyle=loaderStyle;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext=context;
        this.mLoadersStyle=LoaderStyle.BallPulseIndicator;
        return this;
    }

    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mDownloadDir,mExtension,mName,mRequest,mSuccess,mFailure,mError,mBody,mContext,mLoadersStyle,mFile);
    }

}
