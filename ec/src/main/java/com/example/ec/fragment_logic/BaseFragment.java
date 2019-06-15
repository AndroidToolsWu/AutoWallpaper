package com.example.ec.fragment_logic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by HP on 2019/2/21.
 * 使用了butterknife 这个是其他fragment的父类
 *
 */

public abstract class BaseFragment extends Fragment {


    private Unbinder mUnbinder=null;

    //绑定layout
    public abstract Object setLayout();
    //绑定其他控件
    public abstract void onBindView(Bundle savedInstanceState,View rootView);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=null;
        //如果返回integer类型转换为finflate视图，如果是视图直接转换为视图
        if (setLayout() instanceof  Integer){
            rootView=inflater.inflate((Integer) setLayout(),container,false);
        }else if (setLayout() instanceof View){
            rootView= (View) setLayout();
        }else {
            throw new ClassCastException("setLayout() type must be int or View!");
        }
        if (rootView!=null){
            mUnbinder= ButterKnife.bind(this,rootView);
            onBindView(savedInstanceState,rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder!=null){
            mUnbinder.unbind();
        }
    }
}
