package com.ricarad.app.dailyanswer.common;

import android.content.Context;
import android.database.Observable;

import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

/**
 * Created by root on 2019-1-13.
 */
//照片选择必须的配置接口 没有其它功能
public interface PostImagePicker {
    @Gallery
        //打开相册选择图片
    Observable<Result> openGallery(Context context);

    //目前不开发拍照功能
    @Camera
        //打开相机拍照
    Observable<Result> openCamera(Context context);
}
