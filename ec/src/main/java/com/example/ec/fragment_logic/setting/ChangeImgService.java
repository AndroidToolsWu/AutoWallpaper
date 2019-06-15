package com.example.ec.fragment_logic.setting;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.core.util.storage.AppPreference;
import com.example.core.util.storage.ScannerImgUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by HP on 2019/6/4.
 */

public class ChangeImgService extends Service {

    private WallpaperManager wallpaperManager;
    private ArrayList<String> localImgUrlList=new ArrayList<>();
    private static int imgPosition=0;
    private static int imgSize=0;

    @Override
    public void onCreate() {
        super.onCreate();
        wallpaperManager=WallpaperManager.getInstance(this);
        localImgUrlList= ScannerImgUtil.scannerStorageImg(this);

    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //每次换壁纸之前进行壁纸的扫描
        localImgUrlList= ScannerImgUtil.scannerStorageImg(this);
        if (localImgUrlList!=null){
            imgSize=localImgUrlList.size();
        }

        //功能：下次设置壁纸是在当前壁纸位置的后一个
        String url=AppPreference.getCustomAppProfile("currentWallpaperUrl");
        if (url.isEmpty()){
            //currentWallpaperUrl为空说明还没有设置过壁纸，从第一个开始设置
            imgPosition=0;
        }else {
            //这里的flag用于判断壁纸是否被删除，如果被删除了，则没有相等的图片，此时从第一张重新开始
            //设置成局部变量，每次会重新变成false
            boolean flag=false;
            for (int i=0;i<imgSize;i++){
                //如果有相等的说明图片没有被删除，
                // 可以执行下一张（这里需要加一个如果是最后一张的判断，以免越界），
                if (localImgUrlList.get(i).equals(url)){
                    flag=true;
                    //说明上一张设置壁纸的是是最后一张
                    if (imgPosition>=imgSize-1){
                        //重置为0
                        imgPosition=0;

                    //不是最后一张,则+1到下一张
                    }else {
                        imgPosition=i+1;
                    }
                }
            }

            //循环完flag=false说明没有相等的，如果有相等的必为true，此时重新开始
            if (!flag){
                imgPosition=0;
            }

        }

        try {

            //流的方式设置壁纸
            File file=new File(localImgUrlList.get(imgPosition));
            FileInputStream fis=new FileInputStream(file);
            wallpaperManager.setStream(fis,null,true,WallpaperManager.FLAG_LOCK|WallpaperManager.FLAG_SYSTEM);
            //设置完壁纸后记录当前位置
            AppPreference.addCustomAppProfile("currentWallpaperUrl",localImgUrlList.get(imgPosition));


        } catch (Exception e) {
            e.printStackTrace();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


//    Bitmap bitmap=Glide.with(this)   bitmap方式设置壁纸
//                               .load(localImgUrlList.get(imgPosition))
//                               .asBitmap()
//                               .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
//                               .get();