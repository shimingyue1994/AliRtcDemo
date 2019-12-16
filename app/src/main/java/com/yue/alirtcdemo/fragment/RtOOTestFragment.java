package com.yue.alirtcdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.yue.alirtcdemo.R;
import com.yue.alirtcdemo.bean.AliJoinChannelBean;
import com.yue.alirtcdemo.bean.AliRtcInitBean;
import com.yue.alirtcdemo.databinding.FragmentAliRtcOotestBinding;

import static android.graphics.PixelFormat.TRANSPARENT;

/**
 * @author shimy
 * @create 2019/11/28 9:01
 * @desc 一对一测试
 */
public class RtOOTestFragment extends AliRtcOOBaseFragment {


    private static final String ARG_CHANNELBEAN = "arg_channelbean";
    private AliJoinChannelBean channelBean;


    private FragmentAliRtcOotestBinding mBinding;

    public static RtOOTestFragment newInstance(AliJoinChannelBean channelBean) {
        RtOOTestFragment fragment = new RtOOTestFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ali_rtc_ootest, container, false);
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
        init(new AliRtcInitBean(channelBean.userId), mBinding.sophonSelf, mBinding.sophonRemote, null, null);


//        mBinding.layoutRemote.setOnClickListener(v -> {
//            RelativeLayout.LayoutParams paramsBig = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            RelativeLayout.LayoutParams paramsSmall = new RelativeLayout.LayoutParams(Utils.dip2px(getActivity(), 100), Utils.dip2px(getActivity(), 150));
//
//            mBinding.layoutRemote.setLayoutParams(paramsBig);
//            mBinding.sophonRemote.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//            mBinding.sophonRemote.setZOrderOnTop(false);
//            mBinding.sophonRemote.setZOrderMediaOverlay(false);
//            mBinding.layoutRemote.bringToFront();
//
//            mBinding.layoutSelf.setLayoutParams(paramsSmall);
////            mBinding.sophonSelf.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//            mBinding.sophonSelf.setZOrderOnTop(true);
//            mBinding.sophonSelf.setZOrderMediaOverlay(true);
//            mBinding.layoutSelf.bringToFront();
//
//            mBinding.layoutSelf.setVisibility(View.VISIBLE);
//            mBinding.layoutRemote.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "点击layoutRemote", Toast.LENGTH_SHORT).show();
//        });
//
//        mBinding.layoutSelf.setOnClickListener(v -> {
//            RelativeLayout.LayoutParams paramsBig = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            RelativeLayout.LayoutParams paramsSmall = new RelativeLayout.LayoutParams(Utils.dip2px(getActivity(), 100), Utils.dip2px(getActivity(), 150));
//
//            mBinding.layoutSelf.setLayoutParams(paramsBig);
//            mBinding.sophonSelf.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//            mBinding.sophonSelf.setZOrderOnTop(false);
//            mBinding.sophonSelf.setZOrderMediaOverlay(false);
//            mBinding.layoutSelf.bringToFront();
//
//            mBinding.layoutRemote.setLayoutParams(paramsSmall);
////            mBinding.sophonRemote.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//            mBinding.sophonRemote.setZOrderOnTop(true);
//            mBinding.sophonRemote.setZOrderMediaOverlay(true);
//            mBinding.layoutRemote.bringToFront();
//
//            mBinding.sophonRemote.setVisibility(View.VISIBLE);
//            mBinding.sophonSelf.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "点击layoutSelf", Toast.LENGTH_SHORT).show();
//        });


        mBinding.sophonRemote.setOnClickListener(v -> {
            /*由于sufaceview 的zorder问题需要重新移除添加sufaceview，以重新调整view树的层次，并重新调用setZOrderMediaOverlay等方法使其正确显示emmm*/
            mBinding.layoutRemote.removeAllViews();
            mBinding.layoutSelf.removeAllViews();

            mBinding.sophonRemote.getHolder().setFormat(TRANSPARENT);
            mBinding.sophonRemote.setZOrderMediaOverlay(false);
            mBinding.sophonRemote.setZOrderOnTop(false);
            mBinding.layoutRemote.addView(mBinding.sophonRemote);


            mBinding.sophonSelf.setZOrderOnTop(true);
            mBinding.sophonSelf.setZOrderMediaOverlay(true);
            mBinding.layoutSelf.addView(mBinding.sophonSelf);
        });

        mBinding.sophonSelf.setOnClickListener(v -> {
            mBinding.layoutRemote.removeAllViews();
            mBinding.layoutSelf.removeAllViews();

            mBinding.sophonSelf.getHolder().setFormat(TRANSPARENT);
            mBinding.sophonSelf.setZOrderMediaOverlay(false);
            mBinding.sophonSelf.setZOrderOnTop(false);
            mBinding.layoutRemote.addView(mBinding.sophonSelf);


            mBinding.sophonRemote.setZOrderOnTop(true);
            mBinding.sophonRemote.setZOrderMediaOverlay(true);
            mBinding.layoutSelf.addView(mBinding.sophonRemote);
        });


    }
}
