package com.example.core.util.storage;

import android.content.Context;
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
            File scannerDirectory=new File(localImgUrl);
            if (scannerDirectory.isDirectory()){
                for (File file:scannerDirectory.listFiles()){
                    String path=file.getAbsolutePath();
                    if (path.endsWith(".jpg")||path.endsWith(".jpeg")||path.endsWith(".png")){
                        imgUrlList.add(path);
                    }
                }
            }
        }else {
            Toast.makeText(context,"您的相册还没有收藏壁纸",Toast.LENGTH_SHORT).show();
        }
        return imgUrlList;
    }

}
