package com.liang.base;

import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.liang.base.util.CrashHandler;

/**
 * Created by liang on 2017/3/27.
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication mApplication;

    public static MyApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
        initFresco();
    }

    private void initFresco() {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this).
                setMaxCacheSize(100 * ByteConstants.MB).build();

        Supplier<MemoryCacheParams> bitmapCacheParamsSupplier = new Supplier<MemoryCacheParams>() {

            @Override
            public MemoryCacheParams get() {
                MemoryCacheParams param = new MemoryCacheParams(
                        20*ByteConstants.MB
                        ,Integer.MAX_VALUE
                        ,20*ByteConstants.MB
                        ,Integer.MAX_VALUE
                        ,Integer.MAX_VALUE);
                return param;
            }
        };
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this,config);
    }
}
