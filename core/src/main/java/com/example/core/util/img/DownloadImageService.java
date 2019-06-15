package com.example.core.util.img;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.core.util.storage.AppPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by HP on 2019/6/3.
 */

public class DownloadImageService implements Runnable {

    private String url;
    private Context context;
    private ImageDownloadCallBack callBack;
    private File currentFile;

    public DownloadImageService(Context context, String url, ImageDownloadCallBack callBack){
        this.context=context;
        this.url=url;
        this.callBack=callBack;
    }

    @Override
    public void run() {
        //File file=null;
        Bitmap bitmap=null;
        try {

//            file=Glide.with(context)
//                    .load(url)
//                    .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
//                    .get();

            bitmap= Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap!=null){
                //在这里执行图片保存的方法
                saveImageToGallery(context,bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            if (file!=null){
//                callBack.onDownloadSuccess(file);
//            }else {
//                callBack.onDownloadFailed();
//            }

            if (bitmap!=null&&currentFile.exists()){
                callBack.onDownloadSuccess(bitmap);
            }else {
                callBack.onDownloadFailed();
            }
        }
    }

    private void saveImageToGallery(Context context,Bitmap bitmap){

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){

            //小米手机必须使用这个,获取系统图片目录
            File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsoluteFile();
            //创建app本地自己的目录
            String dirName="自动壁纸";
            File appDir=new File(file,dirName);

            if (!appDir.exists()){
                appDir.mkdirs();
                //将储存到的文件路径保存下来
                AppPreference.addCustomAppProfile("localImgUrl",appDir.getAbsolutePath());
            }
            //文件名
            String fileName=System.currentTimeMillis()+".jpg";
            currentFile=new File(appDir,fileName);

            FileOutputStream fos=null;
            try {
                fos=new FileOutputStream(currentFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fos!=null){
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    currentFile.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

            //通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                    , Uri.fromFile(new File(currentFile.getPath()))));


        }else {
            Toast.makeText(context,"请检查手机是否开启读写权限",Toast.LENGTH_SHORT).show();
        }

    }



}
