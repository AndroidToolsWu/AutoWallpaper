package com.example.ec.activity;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.core.proxy_activity.ProxyActivity;
import com.example.core.util.acitvity.ActivityUtils;
import com.example.core.util.dimen.DimenUtil;
import com.example.core.util.img.BitmapAndDrawableUtil;
import com.example.core.util.storage.AppPreference;
import com.example.ec.R;
import com.example.ec.R2;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2019/6/8.
 */

public class MyGalleryWallpaperActivity extends ProxyActivity {

    @BindView(R2.id.my_gallery_layout)
    RelativeLayout relativeLayout;
    @BindView(R2.id.setting_wallpaper_btn)
    Button settingWallpaperBtn;
    @BindView(R2.id.photo_view)
    PhotoView photoView;


    private String localImgUrl;
    private WallpaperManager wallpaperManager;

    @OnClick(R2.id.setting_wallpaper_btn)
    void onClickSettingWallpaperBtn(){
        settingWallpaper();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this,0);
        setContentView(R.layout.my_gallery_wallpaper_activity);
        ActivityUtils.getInstance().addActivity(this);
        ButterKnife.bind(this);
        //获取图片数据
        Intent intent=getIntent();
        if (intent.getStringExtra("imgUrl").length()>0){
            localImgUrl=intent.getStringExtra("imgUrl");
        }
        initBackground();
        initSettingWallpaperBtn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().removeActivity(this);
    }

    @SuppressLint("NewApi")
    private void settingWallpaper() {
        wallpaperManager=WallpaperManager.getInstance(this);
        //流的方式设置壁纸
        File file=new File(localImgUrl);
        FileInputStream fis= null;
        try {
            fis = new FileInputStream(file);
            wallpaperManager.setStream(fis,null,true,WallpaperManager.FLAG_LOCK|WallpaperManager.FLAG_SYSTEM);
            Toast.makeText(this,"设置壁纸成功",Toast.LENGTH_SHORT).show();
            AppPreference.addCustomAppProfile("currentWallpaperUrl",localImgUrl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initBackground() {
        photoView.setScaleType(ImageView.ScaleType.FIT_XY);
        photoView.setScaleLevels(1f,10f,30f);

        Glide.with(this)
                .load(localImgUrl)
                .into(photoView);


    }

    private void initSettingWallpaperBtn(){
        settingWallpaperBtn.getBackground().setAlpha(150);
        settingWallpaperBtn.setClickable(true);
        settingWallpaperBtn.bringToFront();
    }





}





//    private SimpleTarget target=new SimpleTarget<Bitmap>(){
//        @Override
//        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//            //这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
//            Drawable drawable= BitmapAndDrawableUtil.bitmap2Drawable(resource);
//            relativeLayout.setBackground(drawable);
//        }
//    };

//                .asBitmap()
//                .into(target);