package com.liang.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.base.R;
import com.liang.base.util.LogUtil;

/**
 * Created by liang on 2017/3/27.
 */
public class TitleBar extends LinearLayout implements View.OnTouchListener {

    private final static String TAG = TitleBar.class.getSimpleName();
    private ImageView btnTitleLeft;//左侧箭头退出
    private FrameLayout centerLayout;//中间区域
    private TextView tvTitleText;//标题
    private ImageView btnTitleRight;//右侧按钮
    private CharSequence mTitleText;//标题文案
    private OnTitleClick onTitleClick;
    private TextView tvTitleLeft;
    private TextView tvTitleRight;


    //leftText
    public TitleBar setLeftText(CharSequence text, int color, OnClickListener onLeftTextListener){
        btnTitleLeft.setVisibility(GONE);
        tvTitleLeft.setVisibility(VISIBLE);
        tvTitleLeft.setText(text);
        tvTitleLeft.setTextColor(color);
        tvTitleLeft.setOnClickListener(onLeftTextListener);
        return this;
    }

    public TitleBar setLeftText(CharSequence text, ColorStateList colorStateList, OnClickListener onLeftTextListener){
        btnTitleLeft.setVisibility(GONE);
        tvTitleLeft.setVisibility(VISIBLE);
        tvTitleLeft.setText(text);
        tvTitleLeft.setTextColor(colorStateList);
        tvTitleLeft.setOnClickListener(onLeftTextListener);
        return this;
    }

    public TitleBar setLeftText(CharSequence text , OnClickListener onLeftTextListener){
        setLeftText(text, Color.parseColor("#333333"),onLeftTextListener);
        return this;
    }

    public TextView getTitleTextLeft(){
        return tvTitleLeft;
    }


    public TitleBar setRightText(CharSequence text, ColorStateList colorStateList , OnClickListener onRightTextListener){
        btnTitleRight.setVisibility(GONE);
        tvTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setText(text);
        tvTitleRight.setTextColor(colorStateList);
        tvTitleRight.setOnClickListener(onRightTextListener);
        return this;
    }

    public TitleBar setRightText(CharSequence text, int color , OnClickListener onRightTextListener){
        btnTitleRight.setVisibility(GONE);
        tvTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setText(text);
        tvTitleRight.setTextColor(color);
        tvTitleRight.setOnClickListener(onRightTextListener);
        return this;
    }

    public TitleBar setRightText(CharSequence text , OnClickListener onRightTextListener){
        setRightText(text, Color.parseColor("#333333"),onRightTextListener);
        return this;
    }


    public TextView getTitleTextRight(){
        return tvTitleRight;
    }

    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitleText =array.getString(R.styleable.TitleBar_titleBarText);
        array.recycle();

        //初始化控件
        LayoutInflater inflater=(LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_bar_layout,this,true);

        btnTitleLeft = (ImageView) findViewById(R.id.btnTitleBarLeft);
        btnTitleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    ((Activity)getContext()).onBackPressed();
                }catch (Exception e){
                    LogUtil.e(TAG,"crash.."+e.getMessage());
                }
            }
        });
        centerLayout = (FrameLayout) findViewById(R.id.titleBarCenterLayout);
        centerLayout.setOnTouchListener(this);
        tvTitleText = (TextView) findViewById(R.id.tvTitleBarText);
        btnTitleRight = (ImageView) findViewById(R.id.btnTitleBarRight);

        tvTitleLeft = (TextView) findViewById(R.id.tvTitleBarLeft);
        tvTitleRight = (TextView) findViewById(R.id.tvTitleBarRight);
    }

    private long firstClick;
    private long lastClick;
    // 计算点击的次数
    private int count;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                } else if (count == 2) {
                    lastClick = System.currentTimeMillis();
                    // 两次点击小于300ms 也就是连续点击
                    if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                        if(null != onTitleClick){
                            onTitleClick.onTitleClick();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setRightTextColor(int rightTextColor) {
        this.tvTitleRight.setTextColor(rightTextColor);
    }


    public interface OnTitleClick {
        void onTitleClick();
    }

    public TitleBar setOnTitleClick(OnTitleClick onTitleClick){
        this.onTitleClick = onTitleClick;
        return this;
    }

    public TitleBar setTitle(CharSequence titleText){
        this.setVisibility(VISIBLE);
        this.mTitleText = titleText;
        if(!TextUtils.isEmpty(mTitleText)){
            tvTitleText.setVisibility(View.VISIBLE);
            tvTitleText.setText(mTitleText);
        }
        return this;
    }

    public TitleBar setTitle(View centerView){
        this.setVisibility(VISIBLE);
        tvTitleText.setVisibility(View.GONE);
        centerLayout.addView(centerView);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) centerView.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        centerView.setLayoutParams(lp);
        return this;
    }

    public TitleBar setOnLeftButtonClickListener(int resId , OnClickListener listener){
        btnTitleLeft.setVisibility(VISIBLE);
        tvTitleLeft.setVisibility(GONE);
        btnTitleLeft.setImageResource(resId);
        btnTitleLeft.setOnClickListener(listener);
        return this;
    }

    public TitleBar setOnRightButtonClickListener(int resId , OnClickListener listener){
        btnTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setVisibility(GONE);
        btnTitleRight.setImageResource(resId);
        btnTitleRight.setOnClickListener(listener);
        return this;
    }

}
