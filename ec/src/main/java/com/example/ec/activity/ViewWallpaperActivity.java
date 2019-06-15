package com.example.ec.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.core.proxy_activity.ProxyActivity;
import com.example.core.util.img.BitmapAndDrawableUtil;
import com.example.core.util.img.DownloadImageService;
import com.example.core.util.img.ImageDownloadCallBack;
import com.example.ec.R;
import com.example.ec.R2;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2019/6/2.
 */

public class ViewWallpaperActivity extends ProxyActivity{

    @BindView(R2.id.img_layout)
    RelativeLayout relativeLayout;
    @BindView(R2.id.collection_btn)
    Button collectionBtn;
    @BindView(R2.id.photo_view)
    PhotoView photoView;

    private MyHandler myHandler=new MyHandler();
    private static final int DOWNLOAD_SUCCESS_MSG=1100;
    private static final int DOWNLOAD_ERROR_MSG=1101;

    private String imgUrl;


    @OnClick(R2.id.collection_btn)
    void onClickCollectionBtn(){
        if (imgUrl.length()>0){
            onDownloadImg(imgUrl);
        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this,0);
        setContentView(R.layout.view_wallpaper_activity);
        ButterKnife.bind(this);
        initCollectionButton();
        initBackground();
    }



    private void initBackground() {
        Intent intent=getIntent();
        imgUrl=intent.getStringExtra("imgUrl");

        photoView.setScaleType(ImageView.ScaleType.FIT_XY);
        photoView.setScaleLevels(1f,10f,30f);

        Glide.with(this)
                .load(imgUrl)
                .into(photoView);


    }

    private void initCollectionButton(){
        collectionBtn.getBackground().setAlpha(150);
        collectionBtn.setClickable(true);
        collectionBtn.bringToFront();
    }

    private void onDownloadImg(String url){
        DownloadImageService service=new DownloadImageService(this, url, new ImageDownloadCallBack() {
            @Override
            public void onDownloadSuccess(File file) {
            }

            @Override
            public void onDownloadSuccess(Bitmap bitmap) {
                Message message=Message.obtain();
                message.what=DOWNLOAD_SUCCESS_MSG;
                myHandler.sendMessageDelayed(message,500);
            }

            @Override
            public void onDownloadFailed() {
                Message message=Message.obtain();
                message.what=DOWNLOAD_ERROR_MSG;
                myHandler.sendMessageDelayed(message,500);
            }
        });
        //启动图片下载线程
        new Thread(service).start();

    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==DOWNLOAD_SUCCESS_MSG){

                collectionBtn.setTextColor(Color.parseColor("#ddccaa"));
                collectionBtn.setText("已收藏");
                collectionBtn.setClickable(false);

                Toast.makeText(ViewWallpaperActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
            }

            if (msg.what==DOWNLOAD_ERROR_MSG){
                Toast.makeText(ViewWallpaperActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
