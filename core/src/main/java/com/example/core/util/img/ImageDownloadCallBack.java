package com.example.core.util.img;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by HP on 2019/6/3.
 */

public interface ImageDownloadCallBack {

    void onDownloadSuccess(File file);
    void onDownloadSuccess(Bitmap bitmap);

    void onDownloadFailed();
}
