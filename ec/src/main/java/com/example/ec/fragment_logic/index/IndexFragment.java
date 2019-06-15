package com.example.ec.fragment_logic.index;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.core.net.RestClient;
import com.example.core.net.callback.IError;
import com.example.core.net.callback.ISuccess;
import com.example.ec.R;

import com.example.ec.R2;
import com.example.ec.activity.ViewWallpaperActivity;
import com.example.ec.bean.ImageDataBean;
import com.example.ec.bean.ResourceBean;
import com.example.ec.bean.ResultBean;
import com.example.ec.fragment_logic.BaseFragment;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by HP on 2019/3/18.
 */

public class IndexFragment extends BaseFragment {

    @BindView(R2.id.index_title_bar)
    CommonTitleBar commonTitleBar;
    @BindView(R2.id.img_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R2.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private MyHandler myHandler=new MyHandler();
    private IndexRecyclerViewAdapter recyclerViewAdapter;

    private List<ImageDataBean> dataBeanList=new ArrayList<>();
    private List<ImageDataBean> tempList=new ArrayList<>();
    private static final int INIT_RECYCLER=101;

    private static final String TEST_URL="http://service.picasso.adesk.com/v3/homepage/vertical?limit=30&skip=900&adult=false&did=99000829356185&first=0&order=hot";

    //图片开始位置
    private int imgStartPosition=0;
    private int imgCurrentPosition=0;

    @Override
    public Object setLayout() {
        return R.layout.index_fragment;
    }

    @Override
    public void onBindView(Bundle savedInstanceState, View rootView) {
        System.out.println("onCreateView");
        settingTitleBarStyle();
        initImgStartPosition();
        initData(imgStartPosition);
        initRefreshLayout();
    }

    private void settingTitleBarStyle(){
        TextView tv=commonTitleBar.getCenterTextView();
        Paint pt=tv.getPaint();
        pt.setFakeBoldText(true);
        commonTitleBar.setBackgroundResource(R.drawable.status_background_color);

    }




    private void initImgStartPosition(){
        imgStartPosition= 30+(int) (Math.random()*4500);
        if (imgStartPosition%30!=0){
            imgStartPosition=imgStartPosition-imgStartPosition%30;
        }
        imgCurrentPosition=imgStartPosition;
    }

    private void initData(final int startPosition){
        String imgUrlStr="http://service.picasso.adesk.com/v3/homepage/vertical?limit=30&skip="+startPosition+"&adult=false&did=99000829356185&first=0&order=hot";
        System.out.println("start:"+imgUrlStr);
        RestClient.builder()
                .url(imgUrlStr)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson=new Gson();
                        if (response!=null){
                            ResultBean result=gson.fromJson(response,ResultBean.class);
                            ResourceBean resourceBean=result.getRes();
                            List<ImageDataBean> imageDataBeans=resourceBean.getVertical();
                            dataBeanList=imageDataBeans;
                            Message message=Message.obtain();
                            message.what=INIT_RECYCLER;
                            myHandler.sendMessage(message);
                        }
                    }

                })
                //牢记数据请求的时候，用get获取数据，用post请求表单，如果请求错了则会405
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(getActivity(),"加载太快，受不了了...",Toast.LENGTH_SHORT).show();
                        System.out.println("errorCode:"+code);

                    }
                })
                .build()
                .get();
    }


    private void initRefreshLayout() {
        //设置 Header 为 贝塞尔雷达 样式
        //refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲 样式
        //refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        this.refreshLayout.setRefreshHeader(new StoreHouseHeader(getContext()));
        this.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                recyclerViewAdapter.setEnableLoadMore(false);
                initImgStartPosition();
                initData(imgStartPosition);
                refreshLayout.finishRefresh(1000);
                recyclerViewAdapter.setEnableLoadMore(true);
            }
        });

        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);
//        com.scwang.smartrefresh.layout.footer.BallPulseFooter
//        com.scwang.smartrefresh.layout.footer.ClassicsFooter
//        com.scwang.smartrefresh.layout.footer.FalsifyFooter
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //在请求更多数据的时候，添加好间隔的位置数量
                imgCurrentPosition+=30;
                List<ImageDataBean> beanList=addData(imgCurrentPosition);
                refreshLayout.finishLoadMore();
                if (beanList.size()>0){
                    recyclerViewAdapter.addData(beanList);
                }

            }
        });
    }





    private List<ImageDataBean> addData(int currentPosition){
        String imgUrlStr="http://service.picasso.adesk.com/v3/homepage/vertical?limit=30&skip="+currentPosition+"&adult=false&did=99000829356185&first=0&order=hot";
        System.out.println("current:"+imgUrlStr);
        RestClient.builder()
                .url(imgUrlStr)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson=new Gson();
                        if (response!=null){
                            ResultBean result=gson.fromJson(response,ResultBean.class);
                            ResourceBean resourceBean=result.getRes();
                            tempList=resourceBean.getVertical();

                        }
                    }

                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(getActivity(),"请检查网络状况或重试",Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();
        return tempList;
    }

    private void initImgRecycler() {

        //配置适配器
        recyclerViewAdapter = new IndexRecyclerViewAdapter(
                R.layout.index_img_list_item, this, dataBeanList);

        recyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ImageDataBean> tempImgLists = adapter.getData();
                String imgUrl = tempImgLists.get(position).getImg();
                Intent intent = new Intent(getActivity(), ViewWallpaperActivity.class);
                intent.putExtra("imgUrl", imgUrl);
                startActivity(intent);
            }
        });

        //配置recycler
        myRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, LinearLayoutManager.VERTICAL));
        myRecyclerView.setAdapter(recyclerViewAdapter);


    }


    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what==INIT_RECYCLER){
                initImgRecycler();
            }
        }

    }

}



/*
* 被废弃，用smartRefreshLayout取代的代码
*
*
*       //设置加载动画
        //recyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        //recyclerViewAdapter.setLoadMoreView(new CustomLoadMoreView());
        //预加载
        //recyclerViewAdapter.setPreLoadNumber(3);
*
*
* //滑动监听
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

@Override
public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
//                //停止滚动
//                public static final int SCROLL_STATE_IDLE = 0;
//                //正在被外部拖拽,一般为用户正在用手指滚动
//                public static final int SCROLL_STATE_DRAGGING = 1;
//                 //自动滚动开始
//                public static final int SCROLL_STATE_SETTLING = 2;

        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        //如果不设置在滚动结束时的图片加载，那么就会出现到最后一条了下边的数据不加载的情况
        if (newState==RecyclerView.SCROLL_STATE_IDLE){
        System.out.println("列表停了");
        //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
        //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
        int[] spanPosition=new int[((StaggeredGridLayoutManager)layoutManager).getSpanCount()];  //一行多少个数组就多大
        int[] lastVisiblePosition=((StaggeredGridLayoutManager)layoutManager)
        .findLastCompletelyVisibleItemPositions(spanPosition);  //看几个中谁大
        int lastBiggerPosition=Math.max(lastVisiblePosition[0],lastVisiblePosition[1]);
        int sumItem=layoutManager.getItemCount();
        System.out.println("position:"+lastBiggerPosition+"  sum:"+sumItem);
        //如果最大的和最后一项的位置相等，说明到最后了
        //这里提前四个加载后边的，有预加载的作用
        if (lastBiggerPosition==sumItem-1){

        recyclerViewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
@Override
public void onLoadMoreRequested() {
        myRecyclerView.postDelayed(new Runnable() {
@Override
public void run() {

        //在请求更多数据的时候，添加好间隔的位置数量
        imgCurrentPosition+=30;
        List<ImageDataBean> beanList=addData(imgCurrentPosition);
        if (beanList.size()>0){
        recyclerViewAdapter.addData(beanList);
        }
        recyclerViewAdapter.loadMoreComplete();

        }
        },0);
        }
        });
        }
        }
        }

@Override
public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        //如果不能下滑了开始加载
        if (!recyclerView.canScrollVertically(1)){
        recyclerViewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
@Override
public void onLoadMoreRequested() {
        myRecyclerView.postDelayed(new Runnable() {
@Override
public void run() {
        //在请求更多数据的时候，添加好间隔的位置数量
        imgCurrentPosition+=30;
        List<ImageDataBean> beanList=addData(imgCurrentPosition);
        if (beanList.size()>0){
        recyclerViewAdapter.addData(beanList);
        }
        recyclerViewAdapter.loadMoreComplete();

        }
        },0);
        }
        });
        }
        }
        });


*
*
* */
