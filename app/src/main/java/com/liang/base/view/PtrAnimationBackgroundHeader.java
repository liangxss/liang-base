package com.liang.base.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liang.base.R;
import com.liang.base.util.LogUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrAnimationBackgroundHeader extends FrameLayout implements PtrUIHandler {

    private final static String TAG = PtrAnimationBackgroundHeader.class.getSimpleName();
    private PtrBackgroundView ivBackgroundView;
    private ImageView ivPeopleWalking;
    private IHandler iHandler;
    private AnimationDrawable peopleWalkingAnimation;
    private boolean isBegin;
    public PtrAnimationBackgroundHeader(Context context) {
        this(context,null);

    }

    public PtrAnimationBackgroundHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        LayoutInflater inflater=(LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ptr_animation_layout_header,this,true);
        ivBackgroundView =(PtrBackgroundView) findViewById(R.id.ivBackgroundView);
        ivPeopleWalking =(ImageView) findViewById(R.id.ivPeopleWalking);
        ivPeopleWalking.setBackgroundResource(R.drawable.ptr_header_people_walking_animation);
        peopleWalkingAnimation = (AnimationDrawable) ivPeopleWalking.getBackground();
    }

    private void reset(){
        ivBackgroundView.setVisibility(VISIBLE);
        ivPeopleWalking.setVisibility(GONE);
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        if(iHandler!=null){
            iHandler.onUIReset(frame);
        }
        isBegin = true;
        reset();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if(iHandler!=null){
            iHandler.onUIRefreshPrepare(frame);
        }
        isBegin = true;
        reset();
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        stopAnimations();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        int offsetY = ptrIndicator.getCurrentPosY();
        update(offsetY);


    }

    private void update(int offset){

        int height = getHeight();

        if(offset<height){
            stopAnimations();
            ivBackgroundView.setVisibility(VISIBLE);
            ivPeopleWalking.setVisibility(GONE);
            float progress = (float) offset / (float) height;
            ivBackgroundView.setCurrentProgress(progress);

        }else if(offset>=height){
            ivBackgroundView.setVisibility(GONE);
            if(isBegin){
                isBegin = false;
                ivPeopleWalking.setVisibility(GONE);
                ivPeopleWalking.setVisibility(VISIBLE);
                if(peopleWalkingAnimation !=null&&!peopleWalkingAnimation.isRunning()){
                    peopleWalkingAnimation.start();
                    LogUtil.e(TAG,"peopleWalkingAnimation.start()");
                }
            }else{
                if(peopleWalkingAnimation !=null&&!peopleWalkingAnimation.isRunning()){
                    peopleWalkingAnimation.start();
                }
            }
        }

    }

    private void stopAnimations() {
        if(peopleWalkingAnimation !=null){
            peopleWalkingAnimation.stop();
        }
    }


    public interface IHandler{
        void onUIRefreshPrepare(PtrFrameLayout frame);
        void onUIReset(PtrFrameLayout frame);
    }

    public void setHandler(IHandler iHandler) {
        this.iHandler = iHandler;
    }
}
