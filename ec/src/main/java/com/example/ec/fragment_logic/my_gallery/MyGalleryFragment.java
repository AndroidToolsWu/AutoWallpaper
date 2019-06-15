package com.example.ec.fragment_logic.my_gallery;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.core.net.RestClient;
import com.example.core.net.callback.IError;
import com.example.core.net.callback.ISuccess;
import com.example.core.util.storage.AppPreference;
import com.example.core.util.storage.ScannerImgUtil;
import com.example.core.util.vibrate.VibrateHelper;
import com.example.ec.R;
import com.example.ec.R2;
import com.example.ec.activity.IRefreshViewCallback;
import com.example.ec.activity.MyGalleryWallpaperActivity;
import com.example.ec.activity.ViewWallpaperActivity;
import com.example.ec.bean.ImageDataBean;
import com.example.ec.fragment_logic.BaseFragment;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HP on 2019/3/18.
 */

public class MyGalleryFragment extends BaseFragment{

    @BindView(R2.id.shop_cart_title_bar)
    CommonTitleBar commonTitleBar;
    @BindView(R2.id.my_gallery_recycler)
    RecyclerView galleryRecyclerView;
    @BindView(R2.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private MyGalleryRecyclerAdapter recyclerAdapter;
    private List<String> localImgUrlList=new ArrayList<>();
    private SparseArray sparseArray=new SparseArray();
    private SparseBooleanArray checkStates=new SparseBooleanArray();



    @Override
    public Object setLayout() {
        return R.layout.my_gallery_fragment;
    }

    @Override
    public void onBindView(Bundle savedInstanceState, View rootView) {
        settingTitleBarStyle();
        localImgUrlList=ScannerImgUtil.scannerStorageImg(getContext());
        if (!localImgUrlList.isEmpty()){
            initGalleryRecyclerView();
            initRefreshLayout();
        }

    }

    private void initRefreshLayout() {
        //设置 Header 为 贝塞尔雷达 样式
        //refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲 样式
        //refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        this.refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        this.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                localImgUrlList=ScannerImgUtil.scannerStorageImg(getContext());
                recyclerAdapter.setNewData(localImgUrlList);
                recyclerAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(1000);
            }
        });
    }


    private void settingTitleBarStyle(){
        TextView tv=commonTitleBar.getCenterTextView();
        Paint pt=tv.getPaint();
        pt.setFakeBoldText(true);
        commonTitleBar.setBackgroundResource(R.drawable.shop_cart_background_color);
        commonTitleBar.getRightTextView().setVisibility(View.INVISIBLE);
        commonTitleBar.getLeftTextView().setVisibility(View.INVISIBLE);
        //如果点击取消，执行取消状态的操作
        commonTitleBar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //整个列表处于编辑状态
                if (recyclerAdapter.showCheckBoxStates) {
                    invisibleLongPressed(false);
                }
            }
        });

        //点击删除逻辑
        commonTitleBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<sparseArray.size();i++){
                    String imgUrl= (String) sparseArray.valueAt(i);
                    deleteImg(imgUrl);
                }
                Toast.makeText(getActivity(),sparseArray.size()+"项已删除",Toast.LENGTH_SHORT).show();
                //删除后取消状态，隐藏显示text，重新刷新数据和列表
                invisibleLongPressed(false);
                localImgUrlList=ScannerImgUtil.scannerStorageImg(getContext());
                initGalleryRecyclerView();
            }
        });
    }

    private void deleteImg(String url){
        //删除系统中的缩略图
        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver=getContext().getContentResolver();
        String where=MediaStore.Images.Media.DATA+"='"+url+"'";
        contentResolver.delete(uri,where,null);
        //删除SD中图片
        File file=new File(url);
        file.delete();
    }

    private void visibleLongPressed(boolean isVibrate){
        //开启整个列表编辑状态
        int itemCount=recyclerAdapter.getItemCount();
        for (int i=0;i<itemCount;i++){
            CheckBox checkBox= (CheckBox) recyclerAdapter.getViewByPosition(i,R.id.check_box_item);
            checkBox.setChecked(false);
            checkBox.setVisibility(View.VISIBLE);
            checkStates.put(i,false);
        }
        commonTitleBar.getLeftTextView().setVisibility(View.VISIBLE);
        commonTitleBar.getRightTextView().setVisibility(View.VISIBLE);
        if (isVibrate){
            VibrateHelper.Vibrate(getActivity(),300);
        }
        recyclerAdapter.showCheckBoxStates=true;
        sparseArray.clear();
    }

    private void invisibleLongPressed(boolean isVibrate){
        //取消整个列表编辑状态
        int itemCount=recyclerAdapter.getItemCount();
        for (int i=0;i<itemCount;i++){
            CheckBox checkBox= (CheckBox) recyclerAdapter.getViewByPosition(i,R.id.check_box_item);
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
            checkStates.put(i,false);
        }
        commonTitleBar.getLeftTextView().setVisibility(View.INVISIBLE);
        commonTitleBar.getRightTextView().setVisibility(View.INVISIBLE);
        if (isVibrate){
            VibrateHelper.Vibrate(getActivity(),300);
        }
        recyclerAdapter.showCheckBoxStates=false;
        sparseArray.clear();
    }

    private void initGalleryRecyclerView(){
        StaggeredGridLayoutManager gridLayoutManager= new StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL);
        recyclerAdapter=new MyGalleryRecyclerAdapter(this
                ,R.layout.my_gallery_img_item,localImgUrlList);
        recyclerAdapter.bindToRecyclerView(galleryRecyclerView);
        galleryRecyclerView.setLayoutManager(gridLayoutManager);
        galleryRecyclerView.setNestedScrollingEnabled(false);
        galleryRecyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //整个列表处于编辑状态
                if (recyclerAdapter.showCheckBoxStates){
                    //此时点击一个图片就只负责checkBox的选中状态
                    CheckBox checkBox= (CheckBox) recyclerAdapter.getViewByPosition(position,R.id.check_box_item);
                    boolean states=checkStates.get(position,false);
                    if (states){
                        checkBox.setChecked(false);
                        checkStates.put(position,false);
                        //去掉sparseArray要删除图片的url
                        sparseArray.delete(position);
                    }else {
                        checkBox.setChecked(true);
                        checkStates.put(position,true);
                        //添加sparseArray要删除图片的url
                        sparseArray.append(position,localImgUrlList.get(position));
                    }


                }else {
                    Intent intent=new Intent(getActivity(), MyGalleryWallpaperActivity.class);
                    intent.putExtra("imgUrl", localImgUrlList.get(position));
                    startActivity(intent);
                }


            }
        });

        recyclerAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                //整个列表处于编辑状态
                if (recyclerAdapter.showCheckBoxStates){
                    //不显示
                    invisibleLongPressed(true);
                    //不显示的时候要清除sparseArray的内容
                }else {
                    //显示
                    visibleLongPressed(true);
                }

                return true;

            }
        });

    }


}
