package com.example.ec.ui.timer;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by HP on 2018/9/10.
 */

public class TimeCount extends CountDownTimer {

    private Button btn;


    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    public TimeCount(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.btn=button;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
        btn.setText(""+millisUntilFinished/1000);
    }

    @Override
    public void onFinish() {
        btn.setText("重新获取");
        btn.setClickable(true);

    }
}
