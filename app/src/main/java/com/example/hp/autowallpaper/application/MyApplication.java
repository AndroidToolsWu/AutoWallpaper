package com.example.hp.autowallpaper.application;

import android.app.Application;

import com.example.core.app.GlobalContainer;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by HP on 2019/3/18.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化的容器，可以把一些需要app级别的东西放进去
        GlobalContainer.init(this)
                //.withApiHost()
                .configure();

        //统一初始化方法：
//        提示：已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能; init方法会自动检测更新，
//              不需要再手动调用Beta.checkUpgrade(), 如需增加自动检查时机可以使用Beta.checkUpgrade(false,false);
//        参数1：isManual 用户手动点击检查，非用户点击操作请传false
//        参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
        Bugly.init(getApplicationContext(),"52c8dd518f",false);

        //集成腾讯bugly 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        //CrashReport.initCrashReport(getApplicationContext(), "52c8dd518f", true);

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
