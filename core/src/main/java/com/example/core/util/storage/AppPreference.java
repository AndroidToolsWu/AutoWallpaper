package com.example.core.util.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.core.app.GlobalContainer;


/**
 * Created by HP on 2019/1/21.
 */

public class AppPreference {
    /**
     * 提示:
     * 【用于Activity内部存储】Activity.getPreferences(int mode)生成 Activity名.xml
     * 【用于数据的保存】PreferenceManager.getDefaultSharedPreferences(contextPackageName)生成 “包名_preferences.xml”
     * 【自定义名称】Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(GlobalContainer.getApplication());

    private static final String APP_PREFERENCES_KEY = "profile";//定义一个key值；

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    public static void setAppProfile(String val) {
        getAppPreference()
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    public static String getAppProfile() {
        return getAppPreference().getString(APP_PREFERENCES_KEY, null);
    }

    // 清除appkey的数据
    public static void removeAppProfile() {
        getAppPreference()
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    //清除SharedPreferences里所有数据
    public static void clearAllAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    /**
     * flag：true：第一次存储数据；fasle：非第一次存储数据
     * 一般用于第一次进入app使用
     *
     */
    public static void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }


    //这里是一般存数据的地方
    public static void addCustomAppProfile(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }

}