package com.liang.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by admin on 2017/3/2.
 */

public class ImageLoader {

    private Context context;
    private static volatile ImageLoader singleInstance;

    private ImageLoader(Context context){
        this.context = context.getApplicationContext();
    }

    public static ImageLoader getInstance(Context context){

        if(singleInstance==null){
            synchronized (ImageLoader.class){
                if(singleInstance==null){
                    singleInstance = new ImageLoader(context);
                }
            }
        }

        return singleInstance;
    }

    public void loadImage(SimpleDraweeView simpleDraweeView, String uri, Drawable placeHolder, Drawable failureHolder, ScalingUtils.ScaleType actualImageScaleType, ScalingUtils.ScaleType placeholderImageScaleType, ScalingUtils.ScaleType failureImageScaleType){

        if(simpleDraweeView == null){
            return;
        }

        GenericDraweeHierarchy hierarchy ;
        try {
            hierarchy = simpleDraweeView.getHierarchy();
            hierarchy.setFadeDuration(300);
            hierarchy.setActualImageScaleType(actualImageScaleType);
            hierarchy.setPlaceholderImage(placeHolder,placeholderImageScaleType);
            hierarchy.setFailureImage(failureHolder,failureImageScaleType);
        } catch (Exception e) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            hierarchy = builder.setFadeDuration(300).
                    setActualImageScaleType(actualImageScaleType).
                    setPlaceholderImage(placeHolder,placeholderImageScaleType).
                    setFailureImage(failureHolder,failureImageScaleType).
                    build();
            simpleDraweeView.setHierarchy(hierarchy);
        }


        simpleDraweeView.setImageURI(uri);
    }

    public void loadImage(SimpleDraweeView simpleDraweeView, String uri, Drawable placeHolder, Drawable failureHolder){
        loadImage(simpleDraweeView,uri,placeHolder,failureHolder, ScalingUtils.ScaleType.FOCUS_CROP, ScalingUtils.ScaleType.FOCUS_CROP, ScalingUtils.ScaleType.FOCUS_CROP);
    }

    public void loadImage(SimpleDraweeView simpleDraweeView, String uri, int placeHolder, int failureHolder, ScalingUtils.ScaleType actualImageScaleType, ScalingUtils.ScaleType placeholderImageScaleType, ScalingUtils.ScaleType failureImageScaleType){

        if(simpleDraweeView == null){
            return;
        }

        GenericDraweeHierarchy hierarchy ;
        try {
            hierarchy = simpleDraweeView.getHierarchy();
            hierarchy.setFadeDuration(300);
            hierarchy.setActualImageScaleType(actualImageScaleType);
            hierarchy.setPlaceholderImage(placeHolder,placeholderImageScaleType);
            hierarchy.setFailureImage(failureHolder,failureImageScaleType);
        } catch (Exception e) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            hierarchy = builder.setFadeDuration(300).
                    setActualImageScaleType(actualImageScaleType).
                    setPlaceholderImage(placeHolder,placeholderImageScaleType).
                    setFailureImage(failureHolder,failureImageScaleType).
                    build();
            simpleDraweeView.setHierarchy(hierarchy);
        }

        simpleDraweeView.setImageURI(uri);

    }

    public void loadImage(SimpleDraweeView simpleDraweeView, String uri, int placeHolder, int failureHolder){
        loadImage(simpleDraweeView,uri,placeHolder,failureHolder, ScalingUtils.ScaleType.FOCUS_CROP, ScalingUtils.ScaleType.FOCUS_CROP, ScalingUtils.ScaleType.FOCUS_CROP);
    }


}
