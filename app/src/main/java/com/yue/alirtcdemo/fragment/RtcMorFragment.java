package com.yue.alirtcdemo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yue.alirtcdemo.R;
import com.google.android.gms.plus.PlusOneButton;
import com.yue.alirtcdemo.bean.AliJoinChannelBean;
import com.yue.alirtcdemo.bean.AliRtcInitBean;
import com.yue.alirtcdemo.databinding.FragmentAliRtcOotestBinding;
import com.yue.alirtcdemo.databinding.FragmentRtcMorBinding;


public class RtcMorFragment extends AliRtcMoreBaseFragment {
    private static final String ARG_CHANNELBEAN = "arg_channelbean";
    private AliJoinChannelBean channelBean;


    private FragmentRtcMorBinding mBinding;

    public static RtcMorFragment newInstance(AliJoinChannelBean channelBean) {
        RtcMorFragment fragment = new RtcMorFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_CHANNELBEAN, channelBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelBean = getArguments().getParcelable(ARG_CHANNELBEAN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rtc_mor, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChannel(channelBean, randomName(), true);
                stopAudioCapture();
                muteLocalMic(true);
                stopAudioPlayer();
            }
        });
        mBinding.btnJy.setOnClickListener(v -> {
            stopAudioCapture();

        });
        AliRtcInitBean bean =  new AliRtcInitBean(channelBean.userId);
        bean.isSpeaker = false;
        init(bean, mBinding.artcvideo);
        startPreview();
    }

}
