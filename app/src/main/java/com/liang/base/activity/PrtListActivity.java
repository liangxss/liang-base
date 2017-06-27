package com.liang.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.liang.base.BaseActivity;
import com.liang.base.MyConstants;
import com.liang.base.R;
import com.liang.base.adapter.GoodsListAdapter;
import com.liang.base.bean.GoodsBean;
import com.liang.base.http.OkHttpClientManager;
import com.liang.base.recycler.wrapper.LoadMoreWrapper;
import com.liang.base.view.PtrDefaultFrameLayout;
import com.liang.base.util.JsonValidator;
import com.liang.base.util.LogUtil;

import java.util.HashMap;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Request;

/**
 * Created by liang on 2017/6/23.
 */
public class PrtListActivity extends BaseActivity {
    private PtrDefaultFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private int page = 0;
    private View loadMoreLayout;
    private GoodsListAdapter adapter;
//    private String url = "http://api.1-blog.com/biz/bizserver/xiaohua/list.do";


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_ptr_list;
    }

    @Override
    public void initView(View view) {
        setTitleString("标题");
        ptrFrameLayout = find(R.id.ptrFrameLayout);
        recyclerView = find(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new GoodsListAdapter(this);
        recyclerView.setAdapter(adapter);
        ptrFrameLayout.buildPtr(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                boolean flag = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int index = manager.findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                boolean indexTop = false;
                if (childAt == null || (index == 0 && childAt.getTop() == 0)) {
                    indexTop = true;
                }

                return indexTop && flag;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        loadMoreLayout = LayoutInflater.from(this).inflate(R.layout.load_layout, recyclerView, false);
        adapter.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {

            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

    }

    private void loadMore() {
        startRequestForGoods();
    }

    private void refresh() {
        page = 0;
        startRequestForGoods();
    }

    @Override
    public void initData(Context mContext) {
        startRequestForGoods();
    }


    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void setOnClickListener() {

    }

    private void startRequestForGoods(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page+"");

        OkHttpClientManager.postAsyn(MyConstants.GOODS, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.i("liang", e.toString());
                ptrFrameLayout.refreshComplete();
            }

            @Override
            public void onResponse(String response) {
                LogUtil.i("liang", response);
                ptrFrameLayout.refreshComplete();
                JsonValidator validator = new JsonValidator();
                boolean isJson = validator.validate(response);
                if (!isJson) {
                    return;
                }
                try {
                    GoodsBean bean = JSON.parseObject(response, GoodsBean.class);
                    if (bean != null) {
                        setData();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData(){
//        if(result == null || result.obj ==null){
//            return;
//        }
//        if(result.obj.page==0){
//            adapter.setLoadMoreView(null);
//        }else{
//            adapter.setLoadMoreView(loadMoreLayout);
//
//        }
//        page++;
//        if(page == 0) {
//            adapter.setData(bean);
//        } else {
//            adapter.addAll(bean);
//        }
    }
}
