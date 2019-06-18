package com.example.ec.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.net.RestClient;
import com.example.core.net.callback.IError;
import com.example.core.net.callback.IFailure;
import com.example.core.net.callback.ISuccess;
import com.example.core.proxy_activity.ProxyActivity;
import com.example.core.util.acitvity.ActivityUtils;
import com.example.ec.R;
import com.example.ec.R2;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2019/6/17.
 */

public class ReportQuestionActivity extends ProxyActivity{

    @BindView(R2.id.report_question_title_bar)
    CommonTitleBar commonTitleBar;
    @BindView(R2.id.contact_edit_text)
    EditText contactEditText;
    @BindView(R2.id.question_edit_text)
    EditText questionEditText;

    private static String submitQuestionUrl="http://47.95.0.57/AutoWallpaper/ProblemFeedback";

    @OnClick(R2.id.submit_question_btn)
    void onClickSubmitQuestionBtn(){
        String contactStr=contactEditText.getText().toString();
        String questionStr=questionEditText.getText().toString();
        if (questionStr.isEmpty()||questionStr.length()<=0){
            Toast.makeText(this,"问题不能为空",Toast.LENGTH_SHORT).show();
        }else {
           submitQuestion(contactStr,questionStr);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_question_activity);
        ActivityUtils.getInstance().addActivity(this);
        ButterKnife.bind(this);
        settingTitleBarStyle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().removeActivity(this);
    }

    private void settingTitleBarStyle(){
        TextView tv=commonTitleBar.getCenterTextView();
        Paint pt=tv.getPaint();
        pt.setFakeBoldText(true);
        commonTitleBar.setBackgroundResource(R.drawable.status_background_color);
        commonTitleBar.getLeftImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitQuestion(String contactStr,String questionStr){
        RestClient.builder()
                .url(submitQuestionUrl)
                .params("qq",contactStr)
                .params("problem",questionStr)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        if (response.equals("yes")){
                            Toast.makeText(ReportQuestionActivity.this,"问题反馈成功~",Toast.LENGTH_SHORT).show();
                            contactEditText.setText("");
                            questionEditText.setText("");
                        }else if (response.equals("no")){
                            Toast.makeText(ReportQuestionActivity.this,"问题反馈异常，请重试",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(ReportQuestionActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(ReportQuestionActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .post();

    }




}
