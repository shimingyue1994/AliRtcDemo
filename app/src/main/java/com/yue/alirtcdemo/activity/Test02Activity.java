package com.yue.alirtcdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.yue.alirtcdemo.R;
import com.yue.alirtcdemo.databinding.ActivityTest02Binding;
import com.yue.alirtcdemo.weight.ARTCVideoLayoutManager;

public class Test02Activity extends AppCompatActivity {

    private ActivityTest02Binding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil. setContentView(this,R.layout.activity_test02);
        mBinding.fl01.setOnClickListener(v -> {
            mBinding.flContent.bringChildToFront(mBinding.fl01);
        });
        mBinding.fl02.setOnClickListener(v -> {
            mBinding.flContent.bringChildToFront(mBinding.fl02);
        });

        mBinding.rtcvideo.allocCloudVideoView("45464", ARTCVideoLayoutManager.AliRtcVideoTrackCamera);
        mBinding.rtcvideo.allocCloudVideoView("454848", ARTCVideoLayoutManager.AliRtcVideoTrackCamera);
    }
}
