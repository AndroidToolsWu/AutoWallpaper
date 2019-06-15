package com.example.core.util.img;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.ParcelFileDescriptor;

/**
 * Created by HP on 2019/6/7.
 */

public class ScreenImgUtil {

    //获得锁屏壁纸
    public static Object getLockScreenWallpaper(Context context)
    {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (Build.VERSION.SDK_INT >= 24)
        {
            ParcelFileDescriptor pfd = wallpaperManager.getWallpaperFile(WallpaperManager.FLAG_LOCK);
            if (pfd == null)
                pfd = wallpaperManager.getWallpaperFile(WallpaperManager.FLAG_SYSTEM);
            if (pfd != null)
            {
                final Bitmap result = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());

                try
                {
                    pfd.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return result;
            }
        }
        return wallpaperManager.getDrawable();
    }
}
