package com.example.core.net.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by HP on 2019/1/18.
 */

public class LoadCreator {

    //使用WeakHashMap复用loader提升性能
    private static final WeakHashMap<String,Indicator> LOADING_MAP=new WeakHashMap<>();

    //通过create方法创建loader
    static AVLoadingIndicatorView create(String type, Context context){
        final AVLoadingIndicatorView avLoadingIndicatorView=new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type)==null){
            final Indicator indicator=getIndicator(type);
            LOADING_MAP.put(type,indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }


    //方法详解
    /*
    * 1.传入loader的name
    * 2.通过获得包名，name连接字符，获得name的类
    * 3.获得类后，通过newInstance获得实例返回给创建loader的方法
    *
    * */
    private static Indicator getIndicator(String name){
        if (name==null||name.isEmpty()){
            return null;
        }
        final StringBuilder drawableClassName=new StringBuilder();
        if (!name.contains(".")){
            final String defaultPackageName=AVLoadingIndicatorView.class.getPackage().getName();
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");

        }
        drawableClassName.append(name);
        try {
            final Class<?> drawableClass=Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}
