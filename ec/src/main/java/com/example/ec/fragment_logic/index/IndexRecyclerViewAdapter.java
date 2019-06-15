package com.example.ec.fragment_logic.index;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.ui.round_image_view.RoundImageView;
import com.example.ec.R;
import com.example.ec.bean.ImageDataBean;

import java.util.List;
import java.util.Random;

/**
 * Created by HP on 2019/6/1.
 */

public class IndexRecyclerViewAdapter extends BaseQuickAdapter<ImageDataBean,BaseViewHolder>{

    private IndexFragment indexFragment;
    private Context context;
    private int width;

    public IndexRecyclerViewAdapter(int layoutResId,IndexFragment indexFragment, @Nullable List<ImageDataBean> data) {
        super(layoutResId, data);
        this.indexFragment=indexFragment;
        context=indexFragment.getContext();

    }

    @Override
    protected void convert(BaseViewHolder helper, ImageDataBean item) {
        width=indexFragment.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //动态设置图片大小
        int imgWidth=width/2;
        int imgHeight= (int) (width/2+100 +Math.random()*200);
        ImageView imageView=helper.getView(R.id.recycler_item_img);
        ViewGroup.LayoutParams params=imageView.getLayoutParams();
        params.height=imgHeight;
        params.width=imgWidth;
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load(item.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


    }



}
