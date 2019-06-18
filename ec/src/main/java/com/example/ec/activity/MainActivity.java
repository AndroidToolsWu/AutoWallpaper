package com.example.ec.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.core.util.acitvity.ActivityUtils;
import com.example.ec.R;
import com.example.ec.fragment_logic.setting.SettingFragment;
import com.example.ec.fragment_logic.index.IndexFragment;
import com.example.ec.fragment_logic.my_gallery.MyGalleryFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar mainNavigationBar=null;
    private static int lastSelectedItem=0;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Fragment indexFragment,myGalleryFragment,settingFragment;
    private RxPermissions permissions=new RxPermissions(this);
    private boolean isExit=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityUtils.getInstance().addActivity(this);
        System.out.println("onCreate");


        //判断是否为切换应用后第二次进入，如果是就跳回原来的页面，防止重复新建fragment造成页面重叠
        if (savedInstanceState!=null){
            controlFragmentVisibility(lastSelectedItem);
        }else {
            initBottomNavigationBar();
        }

        //如果有以下权限没有获得则申请
        if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ){

            requestPermissions("android.permission.WRITE_EXTERNAL_STORAGE");
            System.out.println("WRITE_EXTERNAL_STORAGE权限申请失败");
        }
        if ( !permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ){
            requestPermissions("android.permission.READ_EXTERNAL_STORAGE");
            System.out.println("READ_EXTERNAL_STORAGE权限申请失败");
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (isExit){
                ActivityUtils.getInstance().finishAllActivity();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.gc();
                        System.exit(0);
                    }
                },500);
            }else {
                isExit=true;
                Toast.makeText(MainActivity.this,"再按一次退出app",Toast.LENGTH_LONG).show();
                //用一个handler，延迟两秒执行代码，两秒内再退出一次就在isExit=false;之前执行this.finish();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit=false;
                    }
                },2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("InlinedApi")
    private void requestPermissions(String permission){

        permissions.request(permission).subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted){
                            Toast.makeText(MainActivity.this,"权限申请成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"必须通过权限才能使用",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    private void initBottomNavigationBar(){
        mainNavigationBar=findViewById(R.id.main_bottom_navigation_bar);

        //导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
        mainNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mainNavigationBar.setTabSelectedListener(this);
        //1、BACKGROUND_STYLE_DEFAULT：如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC 。如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
        //2、BACKGROUND_STYLE_STATIC：点击无水波纹效果
        //3、BACKGROUND_STYLE_RIPPLE：点击有水波纹效果
        mainNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        //mainNavigationBar.setAnimationDuration(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mainNavigationBar.addItem(new BottomNavigationItem(R.drawable.wallpaper_store_icon,"图库")
                //设置选中时的背景颜色
                .setActiveColor("#029cf5")
                //设置未选中时icon的背景颜色
                .setInActiveColor(R.color.navigation_bar_inactive_black)
                //设置未选中是的颜色资源文件
                //.setActiveColorResource()
                //设置未选中时的图片资源
                //.setInactiveIconResource(R.drawable.index_icon)
            ).addItem(new BottomNavigationItem(R.drawable.my_photo_icon,"我的壁纸")
                .setActiveColor("#029cf5")
                .setInActiveColor(R.color.navigation_bar_inactive_black)

            ).addItem(new BottomNavigationItem(R.drawable.setting_icon,"设置")
                .setActiveColor("#029cf5")
                .setInActiveColor(R.color.navigation_bar_inactive_black)

            );

        mainNavigationBar.setFirstSelectedPosition(lastSelectedItem)
                .initialise();


        //设置默认fragment
        setDefaultFragment();


    }

    private void setDefaultFragment(){
        fragmentManager=getSupportFragmentManager();
        //开启事务
        //这个事务只能在一个fragment.onSaveInstanceState()/.onStart()/.onResume()之前调用
        //只有之前调用，之后创建的fragment才会加入到事务当中，否则就会丢失
        transaction=fragmentManager.beginTransaction();
        indexFragment=new IndexFragment();
        transaction.add(R.id.main_container,indexFragment);
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (indexFragment!=null){
            transaction.hide(indexFragment);
        }
        if (myGalleryFragment!=null){
            transaction.hide(myGalleryFragment);
        }
        if (settingFragment!=null){
            transaction.hide(settingFragment);
        }

    }

    @Override
    public void onTabSelected(int position) {
        //将位置给到最后lastSelectedItem重启时使用
        lastSelectedItem=position;
        controlFragmentVisibility(lastSelectedItem);

    }

    @Override
    public void onTabUnselected(int position) {

    }
    @Override
    public void onTabReselected(int position) {

    }

    private void controlFragmentVisibility(int lastSelectedItem){
        fragmentManager=getSupportFragmentManager();
        //每次commit前都要开启一次
        transaction=fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (lastSelectedItem){
            case 0:
                if (indexFragment==null){
                    indexFragment=new IndexFragment();
                    transaction.add(R.id.main_container,indexFragment);
                }else {
                    transaction.show(indexFragment);
                }

                break;
            case 1:
                if (myGalleryFragment==null){
                    myGalleryFragment=new MyGalleryFragment();
                    transaction.add(R.id.main_container,myGalleryFragment);
                }else {
                    transaction.show(myGalleryFragment);
                }

                break;
            case 2:
                if (settingFragment==null){
                    settingFragment=new SettingFragment();
                    transaction.add(R.id.main_container,settingFragment);
                }else {
                    transaction.show(settingFragment);
                }

                break;
        }
        //提交事务
        transaction.commit();

    }



}
