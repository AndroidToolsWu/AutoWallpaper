package com.example.core.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.core.app.GlobalContainer;


/**
 * Created by HP on 2019/1/18.
 */

public class DimenUtil {

    public static int getScreenWidth(){
        final Resources resources= GlobalContainer.getApplication().getResources();
        final DisplayMetrics dm=resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        final Resources resources= GlobalContainer.getApplication().getResources();
        final DisplayMetrics dm=resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
