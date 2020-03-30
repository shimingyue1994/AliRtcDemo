package com.yue.alirtcdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yue.alirtcdemo.R;
import com.yue.alirtcdemo.fragment.RtcMorFragment;
import com.yue.libalirtc.bean.AliJoinChannelBean;

/**
 * @author shimy
 * @create 2019/11/28 15:10
 * @desc 多个画面测试
 */
public class Rtc02Activity extends AppCompatActivity {
    private static final String ARG_DATA = "arg_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtc02);
        AliJoinChannelBean bean = getIntent().getParcelableExtra(ARG_DATA);
        RtcMorFragment fragment = RtcMorFragment.newInstance(bean);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commit();
    }

    public static void startThisActivity(Context context, AliJoinChannelBean bean) {
        Intent intent = new Intent(context, Rtc02Activity.class);
        intent.putExtra(ARG_DATA, bean);
        context.startActivity(intent);
    }
}
