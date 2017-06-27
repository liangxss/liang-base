package com.liang.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liang.base.util.CommonUtils;
import com.liang.base.util.LogUtil;
import com.liang.base.view.TitleBar;


/**
 * Created by liang on 2017/3/27.
 */
public abstract class BaseActivity extends FragmentActivity implements
        View.OnClickListener, TitleBar.OnTitleClick {
    protected final String TAG = this.getClass().getSimpleName();
    protected final static String LOADING = "loading";
    protected final static String REFRESH="refresh";
    protected final static String LOAD_MORE="load_more";
    /** 当前Activity渲染的视图View **/
    private View mContextView = null;
    protected ViewGroup mRoot;
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log(TAG + "-->onCreate()");
        try {
            Bundle bundle = getIntent().getExtras();
            initParams(bundle);
            mContextView = LayoutInflater.from(this)
                    .inflate(bindLayout(), null);
            setContentView(mContextView);
            initView(mContextView);
            setOnClickListener();
            initData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewGroup genRootView() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    @Override
    public void setContentView(View view) {
        mRoot = genRootView();
        mTitleBar = new TitleBar(this);
        mRoot.addView(mTitleBar, -1, -2);
        mRoot.addView(view, -1, -1);
        super.setContentView(mRoot);
        mTitleBar.setVisibility(View.GONE);
        mTitleBar.setOnTitleClick(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        final View content = getLayoutInflater().inflate(layoutResID, null);
        setContentView(content);
    }

    public TitleBar getTitleBar(){
        return mTitleBar;
    }
    protected void setTitleString(CharSequence title){
        if(!TextUtils.isEmpty(title)){
            mTitleBar.setTitle(title);
        }
    }

    /**
     * [初始化Bundle参数]
     *
     * @param params
     */
    public abstract void initParams(Bundle params);

    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int bindLayout();

    /**
     * [初始化控件]
     *
     * @param view
     */
    public abstract void initView(final View view);

    /**
     * [业务操作]
     *
     * @param mContext
     */
    public abstract void initData(Context mContext);

    /**
     * [设置监听]
     */
    public abstract void setOnClickListener();

    /** View点击 **/
    public abstract void widgetClick(View v);

    @SuppressWarnings("unchecked")
    public <T extends View> T find(int resId) {
        return (T) super.findViewById(resId);
    }

    /**
     * [简化Toast]
     * @param msg
     */
    protected void showToast(String msg){
        CommonUtils.toast(this,msg);
    }
    /**
     * [日志输出]
     *
     * @param msg
     */
    protected void Log(String msg) {
        LogUtil.d(TAG, msg);
    }

    @Override
    public void onClick(View v) {
        if (fastClick()) {
            widgetClick(v);
        }
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * [防止快速点击]
     *
     * @return
     */
    private boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        return true;
    }

    @Override
    public void onTitleClick() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log(TAG + "--->onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log(TAG + "--->onDestroy()");
    }
}
