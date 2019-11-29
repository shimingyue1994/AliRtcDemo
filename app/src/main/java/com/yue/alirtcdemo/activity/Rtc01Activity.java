package com.yue.alirtcdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yue.alirtcdemo.R;
import com.yue.alirtcdemo.bean.AliJoinChannelBean;
import com.yue.alirtcdemo.fragment.AliRtcOOBaseFragment;
import com.yue.alirtcdemo.fragment.RtOOTestFragment;

public class Rtc01Activity extends AppCompatActivity {


    private static final String ARG_DATA = "arg_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtc01);
        AliJoinChannelBean bean = getIntent().getParcelableExtra(ARG_DATA);
        AliRtcOOBaseFragment fragment = RtOOTestFragment.newInstance(bean);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commit();
    }

    public static void startThisActivity(Context context, AliJoinChannelBean bean) {
        Intent intent = new Intent(context, Rtc01Activity.class);
        intent.putExtra(ARG_DATA, bean);
        context.startActivity(intent);
    }
}
