package com.liang.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liang.base.R;
import com.liang.base.activity.MainActivity;
import com.liang.base.bean.GoodsBean;
import com.liang.base.util.ArrayUtil;
import com.liang.base.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/5.
 */
public class GoodsListAdapter extends CommonWrapper<RecyclerView.ViewHolder>{

    private List<GoodsBean> data;
    private Fragment fragment;
    private Context mContext;

    public GoodsListAdapter(Fragment fragment, ArrayList<GoodsBean> data){

        if(data == null){
            this.data = new ArrayList<GoodsBean>();
        }else{
            this.data = data;
        }

        this.fragment = fragment;
        this.mContext = fragment.getContext();
    }

    public GoodsListAdapter(Context mContext, ArrayList<GoodsBean> data){
        if(data == null){
            this.data = new ArrayList<GoodsBean>();
        }else{
            this.data = data;
        }
        this.mContext = mContext;
    }

    public GoodsListAdapter(Fragment fragment){
        this(fragment,null);
    }
    public GoodsListAdapter(Context mContext){
        this(mContext,null);
    }



    public void setData(List<GoodsBean> data){
        this.data =data;
        notifyWrapperDataSetChanged();
    }


    public void addAll(List<GoodsBean> data){
        if(data==null){
            data = new ArrayList<GoodsBean>();
        }

        this.data.addAll(data);
        notifyWrapperDataSetChanged();
    }

    public void addData(List<GoodsBean> data){
        if(!ArrayUtil.isEmpty(data)){
            this.data.addAll(data);
        }
        notifyWrapperDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new GoodsListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GoodsBean item = data.get(position);
        if(holder instanceof GoodsListViewHolder) {
            GoodsListViewHolder vh = (GoodsListViewHolder) holder;
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    if (fragment == null) {
                        ((Activity) mContext).startActivity(intent);
                    } else {
                        fragment.startActivity(intent);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class GoodsListViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        public SimpleDraweeView ivPic;

        public GoodsListViewHolder(View itemView) {
            super(itemView);
            this.itemView  = itemView;
            ivPic = (SimpleDraweeView) itemView.findViewById(R.id.ivPic);
            ImageLoader.getInstance(mContext).loadImage(ivPic,null,R.mipmap.default_pic,R.mipmap.default_pic);
        }
    }

}
