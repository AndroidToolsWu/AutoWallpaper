package com.example.core.net.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.core.R;
import com.example.core.util.dimen.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by HP on 2019/1/18.
 */

public class LatteLoader {

    //缩放大小
    private static final int LOADER_SIZE_SCALE=8;
    //偏移量
    private static final int LOADER_OFFSET_SCALE=10;

    private static final ArrayList<AppCompatDialog> LOADERS=new ArrayList<>();

    private static final String DEFAULT_LOADER=LoaderStyle.BallPulseIndicator.name();

    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context,String type){
        //dialog背景
        final AppCompatDialog dialog=new AppCompatDialog(context, R.style.dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView=LoadCreator.create(type,context);
        //将avLoadingIndicatorView铺在dialog上
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth= DimenUtil.getScreenWidth();
        int deviceHeight=DimenUtil.getScreenHeight();

        final Window window=dialog.getWindow();
        //设置窗口位置大小
        if (window!=null){
            WindowManager.LayoutParams lp=window.getAttributes();
            lp.width=deviceWidth /LOADER_SIZE_SCALE;
            lp.height=deviceHeight/LOADER_SIZE_SCALE;
            lp.height=lp.height+deviceHeight/LOADER_OFFSET_SCALE;
            lp.gravity= Gravity.CENTER;

        }
        LOADERS.add(dialog);
        //点击外部不可取消
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }

    public static void stopLoading(){
        for (AppCompatDialog dialog:LOADERS){
            if (dialog!=null){
                if (dialog.isShowing()){
                    dialog.cancel();
                }
            }
        }
    }

}
