package com.example.ec.ui.my_menu_item;
/*
 *  Project Name: NiceTeaShop
 *  Package Name：com.example.me.niceteashop.ViewBlocks
 *  File Name:    MyOneListItem
 *  Creator:      Omar Wu
 *  Created Time: 2018/6/14 14:32
 *  
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ec.R;


public class MyOneListItem extends LinearLayout{


    private ImageView leftIcon,rightIcon;
    private EditText editText;
    private LinearLayout llRoot;
    private TextView leftText,rightText;

    public MyOneListItem(Context context) {
        super(context);
    }

    public MyOneListItem(Context context, AttributeSet attrs) {//该构造函数使其可以在XML布局中使用
        super(context, attrs);
    }

    //设置三个被默认调用的方法

    /**
     * 默认情况下的样子  icon + 文字 + 右箭头 + 下分割线
     *     icon图片
     *    文字内容
     */

    public MyOneListItem initDefaultMode(int iconRes,String textContent){
        initListItemView();
        setLeftIcon(iconRes);
        setTextContent(textContent);
        showEdit(false);
        showArrow(true);
        return this;
    }

    /**
     * 我的页面每一行  icon + 文字 + 右箭头（显示/不显示） + 右箭头左边的文字（显示/不显示）+ 下分割线
     *
     * iconRes     icon图片
     * textContent 文字内容
     */

    public MyOneListItem initMineMode(int iconRes,String textContent,String rightText,boolean arrow){
        initDefaultMode(iconRes,textContent);
        setRightTextContent(rightText);
        showArrow(arrow);
        return this;

    }

    /**
     * icon + 文字 + edit + 下分割线
     *
     */

    public MyOneListItem initEditMode(int iconRes,String textContent,String hintText){
        initDefaultMode(iconRes,textContent);
        showEdit(true);
        setEditTextHint(hintText);
        showArrow(false);
        return this;
    }





    private MyOneListItem initListItemView(){
        LayoutInflater.from(getContext()).inflate(R.layout.my_list_item,this,true);
        llRoot=findViewById(R.id.llRoot);
        leftIcon=findViewById(R.id.list_Item_left_icon);
        rightIcon=findViewById(R.id.list_item_right_icon);
        rightText=findViewById(R.id.list_item_right_text);
        leftText=findViewById(R.id.list_item_left_text);
        editText=findViewById(R.id.list_item_edit);
        return this;
    }

    //设置左icon是否显示
    public MyOneListItem showLeftIcon() {
        leftIcon.setVisibility(VISIBLE);
        return this;
    }
    public MyOneListItem hideLeftIcon() {
        leftIcon.setVisibility(GONE);
        return this;
    }
    //设置是否显示编辑框
    public MyOneListItem showEdit(Boolean edit){
        if (edit){
            editText.setVisibility(VISIBLE);
        }else {
            editText.setVisibility(GONE);
        }
        return this;
    }

    public MyOneListItem setEditTextHint(String hintText){
        editText.setHint(hintText);
        return this;
    }

    //设置右icon是否显示
    public  MyOneListItem showArrow(Boolean arrow){
        if (arrow){
            rightIcon.setVisibility(VISIBLE);
        }else {
            rightIcon.setVisibility(GONE);
        }
        return this;
    }



    /**
     * 设置root的paddingTop 与 PaddingBottom 从而控制整体的行高
     * paddingLeft 与 paddingRight 保持默认 20dp
     */
    public MyOneListItem setPaddingTopBottom(int paddingTop,int paddingBottom){
        llRoot.setPadding(
            20,paddingTop,10,paddingBottom
        );
        return this;
    }

    public MyOneListItem setPaddingLeftRight(int paddingLeft,int paddingRight){
        llRoot.setPadding(
                paddingLeft,20,paddingRight,20
        );
        return this;
    }

    //设置左边的icon
    public MyOneListItem setLeftIcon(int icon){
        leftIcon.setImageResource(icon);
        return this;
    }


    //设置icon大小
    public MyOneListItem setLeftIconSize(int width,int height){
        ViewGroup.LayoutParams layoutParams=leftIcon.getLayoutParams();
        layoutParams.width=width;
        layoutParams.height=height;
        leftIcon.setLayoutParams(layoutParams);
        return this;
    }

    public MyOneListItem setTextContent(String str){
        leftText.setText(str);
        return this;
    }

    public MyOneListItem setRightTextContent(String str){
        rightText.setText(str);
        return this;
    }



    /*
    *
    * 设置item和arrow的点击事件
    * */

    public MyOneListItem setTextContentColor(int color){
        leftText.setTextColor(getResources().getColor(color));
        return this;
    }

    public MyOneListItem setTextContentSize(int textSize){
        leftText.setTextSize(textSize);
        return this;
    }

    public static interface OnListItemClickListener{
        void onListItemClick(View view);
    }

    public static interface OnArrowClickListener{
        void onArrowClick(View view);
    }

    public MyOneListItem setOnListItemClickListener(final OnListItemClickListener onListItemClickListener
            ,final int tag){
        llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                llRoot.setTag(tag);
                onListItemClickListener.onListItemClick(llRoot);
            }
        });
        return this;
    }

    public MyOneListItem setOnArrowClickListener(final OnArrowClickListener onArrowClickListener
            ,final int tag){
        rightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rightIcon.setTag(tag);
                onArrowClickListener.onArrowClick(rightIcon);
            }
        });
        return this;
    }
}
