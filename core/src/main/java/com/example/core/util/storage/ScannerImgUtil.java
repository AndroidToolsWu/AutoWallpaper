package com.example.core.util.storage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2019/6/4.
 */

public class ScannerImgUtil {

    public static ArrayList<String> scannerStorageImg(Context context){
        ArrayList<String> imgUrlList=new ArrayList<>();
        String localImgUrl=AppPreference.getCustomAppProfile("localImgUrl");
        if (!localImgUrl.isEmpty()){
            imgUrlList=scannerPathImg(localImgUrl);
            return imgUrlList;
        }else {
            //老用户更新后再次进入app，没有路径flag，扫描不到本地图片的情况
            //小米手机必须使用这个,获取系统图片目录
            File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsoluteFile();
            //创建app本地自己的目录
            String dirName="自动壁纸";
            File appDir=new File(file,dirName);
            String dirPath=appDir.getAbsolutePath();
            //扫描系统文件夹，如果存在，读取本地的文件，重新添加CustomAppProfile
            AppPreference.addCustomAppProfile("localImgUrl",dirPath);
            imgUrlList=scannerPathImg(dirPath);
            if (imgUrlList.size()>0){
                return imgUrlList;
            }else {
                Toast.makeText(context,"您的相册还没有收藏壁纸",Toast.LENGTH_SHORT).show();
            }

        }
        return imgUrlList;
    }

    private static ArrayList<String> scannerPathImg(String imgPath){
        ArrayList<String> imgUrlList=new ArrayList<>();
        if (!imgPath.isEmpty()&&imgPath.length()>0){
            File scannerDirectory=new File(imgPath);
            if (scannerDirectory.isDirectory()){
                for (File file:scannerDirectory.listFiles()){
                    String path=file.getAbsolutePath();
                    if (path.endsWith(".jpg")||path.endsWith(".jpeg")||path.endsWith(".png")){
                        imgUrlList.add(path);
                    }
                }
            }
        }
        return imgUrlList;
    }

}
