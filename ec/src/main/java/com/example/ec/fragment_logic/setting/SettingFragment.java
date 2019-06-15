package com.example.ec.fragment_logic.setting;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.admin.SystemUpdateInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.util.storage.AppPreference;
import com.example.core.util.storage.ScannerImgUtil;
import com.example.ec.R;
import com.example.ec.R2;
import com.example.ec.fragment_logic.BaseFragment;
import com.example.ec.ui.my_menu_item.MyOneListItem;
import com.example.ec.ui.timer.AlarmManageUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.crashreport.CrashReport;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by HP on 2019/3/18.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R2.id.discover_title_bar)
    CommonTitleBar commonTitleBar;
    @BindView(R2.id.wallpaper_switch)
    Switch wallpaperSwitch;
    @BindView(R2.id.setting_time_list_item)
    MyOneListItem mySettingTimeItem;
    @BindView(R2.id.report_question_list_item)
    MyOneListItem myReportQuestionListItem;
    @BindView(R2.id.check_updates_list_item)
    MyOneListItem myCheckUpdateListItem;

    private AlertDialog alertDialog;


    @Override
    public Object setLayout() {
        return R.layout.setting_fragment;
    }

    @Override
    public void onBindView(Bundle savedInstanceState, View rootView) {
        settingTitleBarStyle();
        initDefaultData();
        initListItem();
        initSwitch();

    }

    private void settingTitleBarStyle(){
        TextView tv=commonTitleBar.getCenterTextView();
        Paint pt=tv.getPaint();
        pt.setFakeBoldText(true);
        commonTitleBar.setBackgroundResource(R.drawable.status_background_color);
    }

    private void initDefaultData(){
        //item为5
        //设置默认间隔12小时换一次
        //设置在第一次进入app时
        if (AppPreference.getCustomAppProfile("checkedItem").isEmpty()){
            AppPreference.addCustomAppProfile("checkedItem","5");
        }
        if (AppPreference.getCustomAppProfile("defaultIntervalMills").isEmpty()){
            AppPreference.addCustomAppProfile("defaultIntervalMills","43200000");
        }
    }

    private void initListItem() {

        //设置时间的item
        mySettingTimeItem.initDefaultMode(R.drawable.change_time_icon,"壁纸更换频率");
        mySettingTimeItem.setOnListItemClickListener(new MyOneListItem.OnListItemClickListener() {
            @Override
            public void onListItemClick(View view) {
                showTimeSelectDialog();
            }
        },0);

        myReportQuestionListItem.initDefaultMode(R.drawable.report_question_icon,"问题反馈");

        myCheckUpdateListItem.initDefaultMode(R.drawable.check_update_icon,"版本更新");
        myCheckUpdateListItem.setOnListItemClickListener(new MyOneListItem.OnListItemClickListener() {
            @Override
            public void onListItemClick(View view) {
                Beta.checkUpgrade();
                UpgradeInfo upgradeInfo=Beta.getUpgradeInfo();
                if (upgradeInfo==null){
                    Toast.makeText(getActivity(),"已是最新版本",Toast.LENGTH_SHORT).show();
                }

            }
        },1);

    }

    private void showTimeSelectDialog(){
        final String[] items={"1分钟","30分钟","1小时","6小时","12小时","一天","三天"};
        final AlertDialog.Builder alterBuilder=new AlertDialog.Builder(getContext());
        alterBuilder.setTitle("间隔时间");

        alterBuilder.setSingleChoiceItems(items, Integer.parseInt(AppPreference.getCustomAppProfile("checkedItem")) , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Intent intent=new Intent(getActivity(),ChangeImgService.class);
                switch (position){
                    case 0:
                        AppPreference.addCustomAppProfile("intervalMills","60000");
                        AppPreference.addCustomAppProfile("checkedItem","0");
                        //如果此时switch处于开启状态，则无需重新开switch设置壁纸间隔时间
                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 1:
                        AppPreference.addCustomAppProfile("intervalMills","1800000");
                        AppPreference.addCustomAppProfile("checkedItem","1");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 2:
                        AppPreference.addCustomAppProfile("intervalMills","3600000");
                        AppPreference.addCustomAppProfile("checkedItem","2");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 3:
                        AppPreference.addCustomAppProfile("intervalMills","21600000");
                        AppPreference.addCustomAppProfile("checkedItem","3");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 4:
                        AppPreference.addCustomAppProfile("intervalMills","43200000");
                        AppPreference.addCustomAppProfile("checkedItem","4");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 5:
                        AppPreference.addCustomAppProfile("intervalMills","86400000");
                        AppPreference.addCustomAppProfile("checkedItem","5");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;
                    case 6:
                        AppPreference.addCustomAppProfile("intervalMills","259200000");
                        AppPreference.addCustomAppProfile("checkedItem","6");

                        if (AppPreference.getAppFlag("switchCheck")){
                            String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
                            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
                        }
                        break;

                }
                Toast.makeText(getActivity(),"间隔时间设置为"+items[position],Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        alertDialog=alterBuilder.create();
        alertDialog.show();
    }

    private void initSwitch() {
        //新进入app初始化switch逻辑,如果查找不到则为false
        boolean isCheck=AppPreference.getAppFlag("switchCheck");
        if (isCheck){
            wallpaperSwitch.setChecked(isCheck);
            openChangeWallpaper();
        }else {
            wallpaperSwitch.setChecked(isCheck);
            closeChangeWallpaper();
        }

        wallpaperSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    //CrashReport.testJavaCrash();
                    openChangeWallpaper();
                    Toast.makeText(getContext(),"开启自动换壁纸",Toast.LENGTH_SHORT).show();
                }else {
                    closeChangeWallpaper();
                    Toast.makeText(getContext(),"已停止自动换壁纸",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openChangeWallpaper(){
        Intent intent=new Intent(getActivity(),ChangeImgService.class);
        String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
        String defaultIntervalMillsStr=AppPreference.getCustomAppProfile("defaultIntervalMills");
        if (intervalMillStr.length()<=0){
            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(defaultIntervalMillsStr),intent);
        }else {
            AlarmManageUtil.setAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
        }
        AppPreference.setAppFlag("switchCheck",true);
    }


    private void closeChangeWallpaper(){
        Intent intent=new Intent(getActivity(),ChangeImgService.class);
        String intervalMillStr=AppPreference.getCustomAppProfile("intervalMills");
        String defaultIntervalMillsStr=AppPreference.getCustomAppProfile("defaultIntervalMills");
        if (intervalMillStr.length()<=0){
            AlarmManageUtil.cancelAlarm(getContext(),Long.parseLong(defaultIntervalMillsStr),intent);
        }else {
            AlarmManageUtil.cancelAlarm(getContext(),Long.parseLong(intervalMillStr),intent);
        }

        AppPreference.setAppFlag("switchCheck",false);
    }

}
