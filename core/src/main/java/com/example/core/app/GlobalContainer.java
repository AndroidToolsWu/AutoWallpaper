package com.example.core.app;

import android.app.Application;
import android.content.Context;

import java.util.WeakHashMap;

/**
 * Created by HP on 2019/3/18.
 */

public final class GlobalContainer  {

    public static Configuration init(Context context){
        getConfigurations().put(ConfigKeys.APPLICATION_CONTEXT.name(),context.getApplicationContext());
        return Configuration.getInstance();
    }

    //获取configuration里的容器
    public static WeakHashMap<Object,Object> getConfigurations(){
        return Configuration.getInstance().getContainerConfigs();
    }
    //通过key获取configuration里的容器的一个值
    public static Object getConfiguration(Enum<ConfigKeys> keys){
        return Configuration.getInstance().getConfiguration(keys);
    }
    //获得application
    public static Application getApplication(){
        return (Application) getConfigurations().get(ConfigKeys.APPLICATION_CONTEXT.name());
    }

    public static Application getApplicationContext(){
        return Configuration.getInstance().getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

}
