package com.example.ec.fragment_logic.my_gallery;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ec.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2019/6/3.
 */

public class MyGalleryRecyclerAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    private Context context;
    private MyGalleryFragment galleryFragment;
    private int width;
    private List<String> imgUrlList=new ArrayList<>();
    private SparseBooleanArray checkStates=new SparseBooleanArray();
    public boolean showCheckBoxStates;

    private OnItemClickWhenLongPressedStateListener onItemClickWhenLongPressedStateListener;
    public interface OnItemClickWhenLongPressedStateListener{
        void onPressedStateClickItem(View view,int position);
    }
    public void setOnClickWhenLongPressed(OnItemClickWhenLongPressedStateListener onClickWhenLongPressedListener){
        this.onItemClickWhenLongPressedStateListener=onClickWhenLongPressedListener;
    }


    public MyGalleryRecyclerAdapter(MyGalleryFragment galleryFragment, int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        context=galleryFragment.getContext();
        this.galleryFragment=galleryFragment;
        imgUrlList=data;
    }

    //每个item自己的逻辑，整体逻辑操作不应该放在这里
    @Override
    protected void convert(BaseViewHolder helper, String item) {

        width=galleryFragment.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //动态设置图片大小
        int imgWidth=width/3;
        int imgHeight=width/3+170;
        ImageView imageView=helper.getView(R.id.gallery_img_item);
        ViewGroup.LayoutParams params=imageView.getLayoutParams();
        params.height=imgHeight;
        params.width=imgWidth;
        imageView.setLayoutParams(params);
        Glide.with(context)
                .load(item.toString())
                .into(imageView);

        CheckBox checkBox=helper.getView(R.id.check_box_item);
        checkBox.setTag(helper.getAdapterPosition());


    }


}
