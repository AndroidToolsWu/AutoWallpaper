package com.example.ec.ui.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import com.example.ec.fragment_logic.setting.ChangeImgService;

/**
 * Created by HP on 2019/6/4.
 */
//    AlarmManager.ELAPSED_REALTIME：休眠后停止，相对开机时间
//    AlarmManager.ELAPSED_REALTIME_WAKEUP：休眠状态仍可唤醒cpu继续工作，相对开机时间
//    AlarmManager.RTC：同1，但时间相对于绝对时间
//    AlarmManager.RTC_WAKEUP：同2，但时间相对于绝对时间
//    AlarmManager.POWER_OFF_WAKEUP：关机后依旧可用，相对于绝对时间

//        set(int type，long startTime，PendingIntent pi)；//一次性
//        setExact(int type, long triggerAtMillis, PendingIntent operation)//一次性的精确版
//        setRepeating(int type，long startTime，long intervalTime，PendingIntentpi)；//精确重复
//        setInexactRepeating（int type，long startTime，longintervalTime，PendingIntent pi）；//非精确，降低功耗

//            int FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的。
//            int FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent。
//            int FLAG_ONE_SHOT:该PendingIntent只能用一次，在send()方法执行后，自动取消。
//            int FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。

public class AlarmManageUtil  {

    private static final String TAG="AlarmManageUtil";
    private static final Handler handler=new Handler();
    private static Runnable runnable;
    private static int DEFAULT_NUMBER=0;


    //startTime 开始时间设置0  intervalMills间隔时间  设置id   nextIntent传入操作intent
    public static void setAlarm(Context context, final long intervalMills, Intent nextIntent){
        final AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pendingIntent=PendingIntent.getService(context, DEFAULT_NUMBER++ ,nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            if (intervalMills < 60*60*1000){
                instantChangeHandler(alarmManager,intervalMills,pendingIntent);
            }else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),intervalMills,pendingIntent);

            }

        }

    }

    public static void cancelAlarm(Context context,final long intervalMills,Intent nextIntent){
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getService(context,DEFAULT_NUMBER++ ,nextIntent
                ,PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            if (intervalMills < 60*60*1000){
                if (runnable!=null){
                    handler.removeCallbacks(runnable);
                }
            }else {
                alarmManager.cancel(pendingIntent);
            }

        }


    }

    private static void instantChangeHandler(final AlarmManager alarmManager, final long intervalMills, final PendingIntent pendingIntent){
        runnable=new Runnable() {
            @Override
            public void run() {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pendingIntent);
                //其实就是重复调用自己
                handler.postDelayed(this,intervalMills);
            }
        };
        handler.postDelayed(runnable,intervalMills);
    }




}
