package com.yue.alirtcdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yue.alirtcdemo.activity.Test01Activity;
import com.yue.alirtcdemo.activity.Test02Activity;
import com.yue.alirtcdemo.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {


    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();

        mBinding.btnTest01.setOnClickListener((view) -> {
            startActivity(new Intent(this, Test01Activity.class));
        });
        mBinding.btnTest02.setOnClickListener(v -> {
            startActivity(new Intent(this, Test02Activity.class));
        });
    }
}
