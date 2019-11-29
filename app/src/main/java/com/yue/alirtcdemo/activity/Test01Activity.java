package com.yue.alirtcdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yue.alirtcdemo.R;
import com.yue.alirtcdemo.bean.AliJoinChannelBean;
import com.yue.alirtcdemo.bean.RTCAuthInfo;
import com.yue.alirtcdemo.databinding.ActivityTest01Binding;

import java.util.Random;

public class Test01Activity extends AppCompatActivity {

    /*https://idoc.yy365.cn/ImHandle/getAliConfig?room=&user=*/
    private ActivityTest01Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test01);
        mBinding.btnTest.setOnClickListener(v -> {
            getData();
        });
    }


    private void getData() {
        OkGo.<String>get("https://idoc.yy365.cn/ImHandle/getAliConfig")
                .tag(this)
                .params("room", mBinding.etRoomid.getText().toString())
                .params("user", randomName())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Test01", response.body());
                        RTCAuthInfo info = JSON.parseObject(response.body(), RTCAuthInfo.class);
                        AliJoinChannelBean aliJoinChannelBean = new AliJoinChannelBean(info.data.appid,
                                mBinding.etRoomid.getText().toString(), info.data.userid, info.data.nonce, info.data.timestamp, info.data.token,
                                info.data.gslb);
                        Rtc01Activity.startThisActivity(Test01Activity.this, aliJoinChannelBean);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("Test01", response.code() + "");
                    }
                });
    }

    /**
     * 随机生成用户名
     *
     * @return
     */
    private String randomName() {
        Random rd = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            // 你想生成几个字符
            str.append((char) (Math.random() * 26 + 'a'));
        }
        return str.toString();
    }
}
