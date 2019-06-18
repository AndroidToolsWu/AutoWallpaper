package com.example.core.util.acitvity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2019/5/24.
 */

public class ActivityUtils {

    private ActivityUtils() {
    }

    private static ActivityUtils instance = new ActivityUtils();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public static ActivityUtils getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if ( null!= activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
