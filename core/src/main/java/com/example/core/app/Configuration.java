package com.example.core.app;

import java.util.WeakHashMap;

/**
 * Created by HP on 2019/3/18.
 *  创建了一个容器用来存放初始化的参数
 */

public class Configuration {

    private static final WeakHashMap<Object,Object> CONTAINER_CONFIGS=new WeakHashMap<>();

    //使用懒汉模式进行私有构造
    private Configuration(){
        CONTAINER_CONFIGS.put(ConfigKeys.CONFIG_READY.name(),false);
    }

    //公共获得单例方法
    public static Configuration getInstance(){
        return Holder.INSTANCE;
    }

    final WeakHashMap<Object,Object> getContainerConfigs(){
        return CONTAINER_CONFIGS;
    }

    //静态内部类
    private static class Holder{
        private static final Configuration INSTANCE=new Configuration();
    }

    //在application里初始化init后调用configure完成配置
    public final void configure(){
        CONTAINER_CONFIGS.put(ConfigKeys.CONFIG_READY.name(),true);
    }

    //下列方法用于初始化一些app级别的参数

    public final Configuration withApiHost(String host){
        CONTAINER_CONFIGS.put(ConfigKeys.API_HOST.name(),host);
        return this;
    }

    private void checkConfiguration(){
        final boolean isReady= (boolean) CONTAINER_CONFIGS.get(ConfigKeys.CONFIG_READY.name());
        if (!isReady){
            throw new RuntimeException("Configuration is not ready");
        }

    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){
        checkConfiguration();
        return (T) CONTAINER_CONFIGS.get(key);
    }




}
