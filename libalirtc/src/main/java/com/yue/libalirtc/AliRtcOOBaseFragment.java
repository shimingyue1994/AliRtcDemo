package com.yue.libalirtc;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alivc.rtc.AliRtcAuthInfo;
import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.yue.libalirtc.bean.AliJoinChannelBean;
import com.yue.libalirtc.bean.AliRtcInitBean;
import com.yue.libalirtc.callback.SimpleAliRtcEngineEventListener;
import com.yue.libalirtc.callback.SimpleAliRtcEngineNotify;

import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;
import org.webrtc.sdk.SophonSurfaceView;

import java.util.Random;

import static com.alivc.rtc.AliRtcEngine.AliRTCCameraType.AliRTCCameraBack;
import static com.alivc.rtc.AliRtcEngine.AliRTCCameraType.AliRTCCameraFront;
import static com.alivc.rtc.AliRtcEngine.AliRtcAudioTrack.AliRtcAudioTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoProfile.AliRTCSDK_Video_Profile_Default;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen;

/**
 * @author shimy
 * @create 2019/11/26 15:14
 * @desc --rtc一对一 init()方法的调用并不会推送视频流
 * 频道：会议、房间、频道的总称
 * 订阅：即获取对方视频
 * 发布：即推送自己的音频流到频道（房间）
 */
public class AliRtcOOBaseFragment extends Fragment {

    private final String TAG = "yyAliRtcOOFragment";

    private View mRootView;
    /*大画面*/
    private SophonSurfaceView mViewSelf;
    /*小画面*/
    private SophonSurfaceView mViewRemote;
    /**
     * SDK提供的对音视频通话处理的引擎类 初始化
     */
    private AliRtcEngine mAliRtcEngine;
    /*初始化参数*/
    private AliRtcInitBean mAliRtcBean;

    private SimpleAliRtcEngineEventListener mSimpleEngineEventListener;
    private SimpleAliRtcEngineNotify mSimpleEngineNotify;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_ali_rtc_ootest, container, false);
//        return mRootView;
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewSelf = mRootView.findViewById(R.id.sophon_big);
//        mViewSmall = mRootView.findViewById(R.id.sophon_small);
//        mBtnJoin = mRootView.findViewById(R.id.btn_join);
//        mBtnJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                joinChannel(aliJoinChannelBean, randomName(), true);
//            }
//        });
//        init(new AliRtcInitBean(), );
    }


    /**
     * 只需要在onActivityCreated或onViewCreate里调用此方法 ，然后再适时调用joinChannel并传入参数即可
     *
     * @param rtcAuthBean 初始化bean
     * @param self                      自己的视频view
     * @param remote                    远端的视频view
     * @param aliRtcEngineEventListener 可传null
     * @param simpleAliRtcEngineNotify  可传null
     */
    public void init(AliRtcInitBean rtcAuthBean, SophonSurfaceView self, SophonSurfaceView remote,
                     SimpleAliRtcEngineEventListener aliRtcEngineEventListener, SimpleAliRtcEngineNotify simpleAliRtcEngineNotify) {
        this.mSimpleEngineEventListener = aliRtcEngineEventListener;
        this.mSimpleEngineNotify = simpleAliRtcEngineNotify;
        mViewRemote = remote;
        mViewSelf = self;
        mAliRtcBean = rtcAuthBean;
        initRTCEngine();
        initLocalView();
    }


    /**
     * 初始化rtc引擎 先设置回调
     */
    private void initRTCEngine() {
        // 防止初始化过多
        if (mAliRtcEngine == null) {
            //实例化,必须在主线程进行。
            mAliRtcEngine = AliRtcEngine.getInstance(getContext().getApplicationContext());
            //设置事件的回调监听
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            //设置接受通知事件的回调
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
        }
        leaveChannel();
    }


    /**
     * 设置本地的预览视图的view 此时还未向远端服务器推送音视频流
     */
    @SuppressLint("NewApi")
    private void initLocalView() {
        mViewSelf.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mViewSelf.setZOrderOnTop(true);
        mViewSelf.setZOrderMediaOverlay(true);
        AliRtcEngine.AliVideoCanvas aliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        aliVideoCanvas.view = mViewSelf;
        aliVideoCanvas.renderMode = AliRtcRenderModeAuto;
        /*设置为相机流*/
        mAliRtcEngine.setLocalViewConfig(aliVideoCanvas, mAliRtcBean.videoTrack);
        /*预设值摄像头方向。 还有一个usb摄像头 是无法用于此方法的*/
        if (mAliRtcBean.cameraType == AliRTCCameraFront || mAliRtcBean.cameraType == AliRTCCameraBack)
            mAliRtcEngine.setPreCameraType(mAliRtcBean.cameraType.getCameraType());
        if (mAliRtcEngine.getVideoProfile(mAliRtcBean.videoTrack) != AliRTCSDK_Video_Profile_Default)
            mAliRtcEngine.setVideoProfile(mAliRtcBean.videoProfile, mAliRtcBean.videoTrack);
        /*设置纯音频模式还是音视频模式。返回0代表设置成功，其他代表设置失败。默认为音视频模式（非纯音频），必须在joinChannel之前设置。*/
        mAliRtcEngine.setAudioOnlyMode(mAliRtcBean.audioOnly);
        /*扬声器还是听筒*/
        mAliRtcEngine.enableSpeakerphone(mAliRtcBean.isSpeaker);
        mAliRtcEngine.setLogLevel(mAliRtcBean.logLevel);
        startPreview();
    }

    /**
     * 加入房间 调用初始化方法后 可以根据需要适时加入房间
     *
     * @param bean     加入房间需要从本地服务器获取的参数
     * @param username 用户显示名称 非userid
     * @param autoPub  自动发布
     */
    public void joinChannel(AliJoinChannelBean bean, String username, boolean autoPub) {
        joinChannel(bean, username, autoPub, true);
    }

    /**
     * 加入房间
     *
     * @param bean     加入房间需要从本地服务器获取的参数
     * @param username 用户显示名称
     * @param autoPub  自动发布
     * @param autoSub  自动订阅 一般为true
     */
    public void joinChannel(AliJoinChannelBean bean, String username, boolean autoPub, boolean autoSub) {
        if (mAliRtcEngine == null) {
            return;
        }
        AliRtcAuthInfo userInfo = new AliRtcAuthInfo();
        userInfo.setAppid(bean.appid);
        userInfo.setNonce(bean.nonce);
        userInfo.setTimestamp(bean.timestamp);
        userInfo.setUserId(bean.userId);
        userInfo.setGslb(bean.gslb);
        userInfo.setToken(bean.token);
        userInfo.setConferenceId(bean.channelId);
        /*
         *设置自动发布和订阅，只能在joinChannel之前设置
         *参数1    true表示自动发布；false表示手动发布
         *参数2    true表示自动订阅；false表示手动订阅
         */
        mAliRtcEngine.setAutoPublish(autoPub, autoSub);
        // 加入频道
        mAliRtcEngine.joinChannel(userInfo, username);
    }


    /**
     * 开启本地预览 不将采集数据预览在surfaceview上
     * 可以在初始化后调用此方法 显示相机采集数据
     * 可以在joinchannel前调用
     *
     * @return 0 表示预览成功 其他为失败
     */
    public int startPreview() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.startPreview();
        else
            return -1;
    }

    /**
     * 关闭本地预览
     *
     * @return
     */
    public int stopPreview() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.stopPreview();
        else
            return -1;
    }

    /**
     * @return 切换"前后"摄像头，返回0为切换成功，其他为切换失败。只有前后置摄像头才能切换 usb或无效则不可切换
     */
    public int switchCamera() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.switchCamera();
        else
            return -1;
    }

    /**
     * true表示停止发布视频流；false表示恢复发布。 此方法不能作为初始化时的publish发布视频使用
     *
     * @param mute
     * @return
     */
    public int muteLocalCamera(boolean mute) {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.muteLocalCamera(mute, mAliRtcBean.videoTrack);
        else
            return -1;
    }

    /**
     * @param zoom      zoom变焦的级别。可以通过getCameraZoom获取当前变焦
     * @param flash     是否打开闪光灯。
     * @param autoFocus 是否打开自动对焦。
     * @return 返回0表示设置成功，其他表示设置失败。
     */
    public int setCameraZoom(float zoom, boolean flash, boolean autoFocus) {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.setCameraZoom(zoom, flash, autoFocus);
        else
            return -1;
    }


    /**
     * @param mute true表示停止发布本地音频；false表示恢复发布。
     * @return
     */
    public int muteLocalMic(boolean mute) {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.muteLocalMic(mute);
        else
            return -1;
    }

    /**
     * 开启音频采集
     *
     * @return
     */
    public int startAudioCapture() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.startAudioCapture();
        else
            return -1;
    }

    /**
     * 停止音频采集 与mute 不同的是mute只是不将采集的音频发布到房间共享，而stop则是直接不采集音频了
     *
     * @return
     */
    public int stopAudioCapture() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.stopAudioCapture();
        else
            return -1;
    }

    /**
     * 开始播放音频
     *
     * @return
     */
    public int startAudioPlayer() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.startAudioPlayer();
        else
            return -1;
    }


    /**
     * 停止音频播放
     *
     * @return
     */
    public int stopAudioPlayer() {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.stopAudioPlayer();
        else
            return -1;
    }

    /**
     * 设置是否停止播放远端音频流，
     *
     * @param uid  用户ID，从App server分配的唯一标示符。
     * @param mute true表示停止播放；false表示恢复播放。
     * @return 返回0表示设置成功，-1表示设置失败。
     */
    public int muteRemoteAudioPlaying(String uid, boolean mute) {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.muteRemoteAudioPlaying(uid, mute);
        else
            return -1;
    }

    /**
     * 是否开启扬声器
     *
     * @param enable true 开启 false 听筒模式
     * @return
     */
    public int enableSpeakerphone(boolean enable) {
        if (mAliRtcEngine != null)
            return mAliRtcEngine.enableSpeakerphone(enable);
        else
            return -1;
    }

    /**
     * 离开频道
     */
    public void leaveChannel() {
        if (mAliRtcEngine != null)
            if (mAliRtcEngine.isInCall())
                mAliRtcEngine.leaveChannel();
    }


    /**
     * 发布
     *
     * @param publish 是否允许发布  true 允许 false 不允许 当为false时则取消发布本地的音视频流
     */
    public void publish(boolean publish) {
        publish(publish, publish, publish, publish, AliRtcVideoTrackCamera);
    }

    /**
     * 发布本地的音频视频流
     * 当加入频道时 autopus 设置为false时调用
     *
     * @param audioPublish  true表示允许发布音频流，false表示不允许，取消发布
     * @param cameraPublish true表示允许发布相机流，false表示不允许，取消发布
     * @param screenPublish true表示允许发布屏幕流，false表示不允许，取消发布
     * @param simulcast     true表示允许发布次要视频流；false表示不允许，取消发布
     * @param videoTrack    次要视频流来源  默认为允许发布次要视频流 当前仅支持相机
     */
    public void publish(boolean audioPublish, boolean cameraPublish, boolean screenPublish, boolean simulcast, AliRtcEngine.AliRtcVideoTrack videoTrack) {
        //发布本地流设置
        //true表示允许发布音频流，false表示不允许
        mAliRtcEngine.configLocalAudioPublish(audioPublish);
        //true表示允许发布相机流，false表示不允许
        mAliRtcEngine.configLocalCameraPublish(cameraPublish);
        //true表示允许发布屏幕流，false表示不允许
        mAliRtcEngine.configLocalScreenPublish(screenPublish);
        //true表示允许发布次要视频流；false表示不允许 设置是否允许发布次要视频流。默认为允许发布次要视频流，手动发布时，需要调用publish才能生效。流类型，当前只支持相机流：
        mAliRtcEngine.configLocalSimulcast(simulcast, AliRtcVideoTrackCamera);
        mAliRtcEngine.publish();
    }

    /**
     * @param remoteUserID 远端用户id
     * @param subscribe    true 订阅 false 取消订阅
     * @param master       相机流为大流还是次流
     * @return 返回为0时说明接口执行正常，但是否订阅成功还得看回调结果；返回为非0时，说明接口执行异常中断，订阅失败。
     */
    public int subscribe(String remoteUserID, boolean subscribe, boolean master) {
        return subscribe(remoteUserID, subscribe, subscribe, master, subscribe);
    }

    /**
     * 订阅远端视频流
     * 当 autoSub设置为false时需要调用此方法 手动获取其他用户的流 false则为取消
     *
     * @param remoteUserID 远端用户的id
     * @param remoteAudio  音频
     * @param screenTrack  屏幕共享
     * @param master       相机流 true大流 false 小流
     * @param cameraTrack  相机流 false为取消
     * @return 返回为0时说明接口执行正常，但是否订阅成功还得看回调结果；返回为非0时，说明接口执行异常中断，订阅失败。
     */
    public int subscribe(String remoteUserID, boolean remoteAudio, boolean screenTrack, boolean master, boolean cameraTrack) {
        // 订阅远端音频流
        mAliRtcEngine.configRemoteAudio(remoteUserID, remoteAudio);
        // 订阅远端屏幕流
        mAliRtcEngine.configRemoteScreenTrack(remoteUserID, screenTrack);
        // 订阅远端相机流
        mAliRtcEngine.configRemoteCameraTrack(remoteUserID, master, cameraTrack);
        // 订阅远端用户ID
        return mAliRtcEngine.subscribe(remoteUserID);
    }

    /**
     * 用户操作回调监听(回调接口都在子线程)
     */
    private AliRtcEngineEventListener mEventListener = new AliRtcEngineEventListener() {

        /**
         * 加入房间的回调
         * @param result 结果码:0为加入频道成功，非0为失败。
         */
        @Override
        public void onJoinChannelResult(int result) {
//            Log.i(TAG, "onJoinChannelResult : " + result);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onJoinChannelResult(result);
        }

        /**
         * 离开房间的回调
         * @param result 结果码 :0为离开频道成功，非0为失败。
         */
        @Override
        public void onLeaveChannelResult(int result) {
//            Log.i(TAG, "onLeaveChannelResult : " + result);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onLeaveChannelResult(result);
        }

        /**
         * 发布音视频流回调。
         * @param result 结果码:0为发布成功，非0为失败。
         * @param publishId publishId 流ID。
         */
        @Override
        public void onPublishResult(int result, String publishId) {
//            Log.i(TAG, "onPublishResult : " + result);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onPublishResult(result, publishId);
        }

        /**
         * 停止发布音视频流回调。
         * @param result 结果码:0为停止发布成功，非0为失败。
         */
        @Override
        public void onUnpublishResult(int result) {
//            Log.i(TAG, "onUnpublishResult : " + result);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onUnpublishResult(result);
        }

        /**
         * (拉流)订阅成功的回调 setAutoPublish 订阅项设置为false 主动拉去
         * @param uid userid 用户ID，从App server分配的唯一标示符
         * @param result 结果码 0表示订阅成功，非0表示失败。
         * @param aliRtcVideoTrack 订阅成功的视频流，可以播放次流。
         * @param aliRtcAudioTrack  订阅成功的音频流，可以播放此流。
         */
        @Override
        public void onSubscribeResult(String uid, int result, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,
                                      AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
//            Log.i(TAG, "onSubscribeResult : " + result);
            if (result == 0) {
                updateRemoteDisplay(uid, aliRtcAudioTrack, aliRtcVideoTrack);
            }
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onSubscribeResult(uid, result, aliRtcVideoTrack, aliRtcAudioTrack);
        }

        /**
         * 取消订阅的回调 主动不拉取某个视频
         * @param result 结果码：0表示取消订阅成功，非0表示失败。
         * @param userId userid：用户ID，从App server分配的唯一标示符。
         */
        @Override
        public void onUnsubscribeResult(int result, String userId) {
//            Log.i(TAG, "onUnsubscribeResult : " + result);
            updateRemoteDisplay(userId, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onUnsubscribeResult(result, userId);
        }


        /**
         * 网络状态变化的回调
         */
        @Override
        public void onNetworkQualityChanged(String uid, AliRtcEngine.AliRtcNetworkQuality upQuality, AliRtcEngine.AliRtcNetworkQuality downQuality) {
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onNetworkQualityChanged(uid, upQuality,downQuality);
        }



        /**
         * 出现警告的回调
         * @param warn 警告类型
         */
        @Override
        public void onOccurWarning(int warn) {
//            Log.i(TAG, "onOccurWarning : " + warn);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onOccurWarning(warn);
        }

        /**
         * 出现错误的回调
         * @param error 错误码
         */
        @Override
        public void onOccurError(int error) {
//            Log.i(TAG, "onOccurError : " + error);
            //错误处理
//            processOccurError(error);
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onOccurError(error);
        }

        /**
         * 网络连接断开回调。
         */
        @Override
        public void onConnectionLost() {
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onConnectionLost();
        }

        /**
         * 重新尝试网络连接回调。
         */
        @Override
        public void onTryToReconnect() {
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onTryToReconnect();
        }

        /**
         * 网络连接恢复回调。
         */
        @Override
        public void onConnectionRecovery() {
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onConnectionRecovery();
        }

        /**
         * @param oldRole
         * @param newRole
         * 用户角色更新
         */
        @Override
        public void onUpdateRoleNotify(AliRtcEngine.AliRTCSDK_Client_Role oldRole, AliRtcEngine.AliRTCSDK_Client_Role newRole) {
            if (mSimpleEngineEventListener != null)
                mSimpleEngineEventListener.onUpdateRoleNotify(oldRole, newRole);
        }
    };

    /**
     * SDK事件通知(回调接口都在子线程)
     */
    private AliRtcEngineNotify mEngineNotify = new AliRtcEngineNotify() {
        /**
         * 远端用户停止发布通知，处于OB（observer）状态 停止推送视频 还未退出房间 被动接收
         * @param aliRtcEngine 核心引擎对象
         * @param userId userid 	远端用户ID
         */
        @Override
        public void onRemoteUserUnPublish(AliRtcEngine aliRtcEngine, String userId) {
            Log.i(TAG, "onRemoteUserUnPublish : " + userId);
            updateRemoteDisplay(userId, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onRemoteUserUnPublish(aliRtcEngine, userId);
        }

        /**
         * 远端用户上线通知 进房间 可能还未推送视频  被动通知
         * @param uid userid
         */
        @Override
        public void onRemoteUserOnLineNotify(String uid) {
            Log.i(TAG, "onRemoteUserOnLineNotify : " + uid);
//            addRemoteUser(s);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onRemoteUserOnLineNotify(uid);
        }

        /**
         * 远端用户下线通知 退出了房间
         * @param uid userid
         */
        @Override
        public void onRemoteUserOffLineNotify(String uid) {
            Log.i(TAG, "onRemoteUserOffLineNotify : " + uid);
//            removeRemoteUser(s);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onRemoteUserOffLineNotify(uid);
        }

        /**
         * 远端用户发布音视频流变化通知 在房间中推送音视频流 应该是自动订阅的情况下
         * @param uid userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onRemoteTrackAvailableNotify(String uid, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                                 AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            Log.i(TAG, "onRemoteTrackAvailableNotify : " + uid);
            updateRemoteDisplay(uid, aliRtcAudioTrack, aliRtcVideoTrack);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onRemoteTrackAvailableNotify(uid, aliRtcAudioTrack, aliRtcVideoTrack);
        }

        /**
         * 订阅流回调，可以做UI及数据的更新 应该非自动订阅的情况下
         * @param uid userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onSubscribeChangedNotify(String uid, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                             AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            Log.i(TAG, "onSubscribeChangedNotify : " + uid);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onSubscribeChangedNotify(uid, aliRtcAudioTrack, aliRtcVideoTrack);
        }

        /**
         * 订阅信息
         * @param aliSubscriberInfos 订阅自己这边流的user信息
         * @param count 当前订阅人数
         */
        @Override
        public void onParticipantSubscribeNotify(AliSubscriberInfo[] aliSubscriberInfos, int count) {
            Log.i(TAG, "onParticipantSubscribeNotify : " + count);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onParticipantSubscribeNotify(aliSubscriberInfos, count);
        }

        /**
         * 首帧的接收回调
         * @param callId callId
         * @param streamLabel stream_label
         * @param trackLabel track_label 分为video和audio
         * @param timeCost 时间
         */
        @Override
        public void onFirstFramereceived(String callId, String streamLabel, String trackLabel, int timeCost) {
            Log.i(TAG, "onFirstFramereceived : " + callId);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onFirstFramereceived(callId, streamLabel, trackLabel, timeCost);
        }


        /**
         *首包数据接收成功。
         * @param callId 远端callId 应该是频道id
         * @param streamLabel stream标签
         * @param trackLabel 流的标签。
         * @param timeCost 耗时（单位ms）。
         */
        @Override
        public void onFirstPacketReceived(String callId, String streamLabel, String trackLabel, int timeCost) {
            Log.i(TAG, "onFirstPacketReceived : " + callId);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onFirstPacketReceived(callId, streamLabel, trackLabel, timeCost);
        }

        /**
         *  首帧数据发送成功回调
         * @param callId 远端callid。
         * @param streamLabel stream标签。
         * @param trackLabel 流的标签。
         * @param timeCost 耗时（ms）。
         */
        @Override
        public void onFirstPacketSent(String callId, String streamLabel, String trackLabel, int timeCost) {
            Log.i(TAG, "onFirstPacketSent : " + callId);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onFirstPacketSent(callId, streamLabel, trackLabel, timeCost);
        }

        /**
         * 取消订阅信息回调
         * @param aliParticipantInfos 订阅自己这边流的user信息
         * @param count 当前订阅人数
         */
        @Override
        public void onParticipantUnsubscribeNotify(AliParticipantInfo[] aliParticipantInfos, int count) {
            Log.i(TAG, "onParticipantUnsubscribeNotify : " + count);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onParticipantUnsubscribeNotify(aliParticipantInfos, count);
        }

        /**
         * 被服务器踢出或者频道关闭时回调
         * @param code
         * 消息类型。
         * 1：被服务器踢出。
         * 2：频道关闭。
         * 3：同一个userId在其他端登录，被服务器踢出。
         */
        @Override
        public void onBye(int code) {
            Log.i(TAG, "onBye : " + code);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onBye(code);
        }

        /**
         * 远端用户状态改变通知
         * @param aliStatusInfos 用户状态数组。
         * @param count 数组长度。
         */
        @Override
        public void onParticipantStatusNotify(AliStatusInfo[] aliStatusInfos, int count) {
            Log.i(TAG, "onParticipantStatusNotify : " + count);
            if (mSimpleEngineNotify != null)
                mSimpleEngineNotify.onParticipantStatusNotify(aliStatusInfos, count);
        }
    };


    /**
     * 更新远端视频显示
     *
     * @param uid 远端用户id
     * @param at  音频流
     * @param vt  视频流
     */
    private void updateRemoteDisplay(String uid, AliRtcEngine.AliRtcAudioTrack at, AliRtcEngine.AliRtcVideoTrack vt) {
        getActivity().runOnUiThread(() -> {
            if (null == mAliRtcEngine) {
                return;
            }
            /*获取远端用户信息*/
            AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
            // 如果没有，说明已经退出了或者不存在。则不需要添加，并且删除
            if (remoteUserInfo == null) {
                // remote user exit room
                Log.e(TAG, "updateRemoteDisplay remoteUserInfo = null, uid = " + uid);
                return;
            }
            //获取远端用户的视频画布
            AliRtcEngine.AliVideoCanvas cameraCanvas = remoteUserInfo.getCameraCanvas();
            AliRtcEngine.AliVideoCanvas screenCanvas = remoteUserInfo.getScreenCanvas();
            //视频情况 setRemoteViewConfig方法设置画布并自动在画布内根据设置播放视频流
            if (vt == AliRtcVideoTrackNo) {
                //没有视频流
                cameraCanvas = null;
                screenCanvas = null;
                mViewRemote.setVisibility(View.GONE);
            } else if (vt == AliRtcVideoTrackCamera) {
                mViewRemote.setVisibility(View.VISIBLE);
                //相机流
                screenCanvas = null;
                cameraCanvas = createCanvasIfNull(cameraCanvas);
                //SDK内部提供进行播放的view
                mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
            } else if (vt == AliRtcVideoTrackScreen) {
                mViewRemote.setVisibility(View.VISIBLE);
                //屏幕共享流
                cameraCanvas = null;
                screenCanvas = createCanvasIfNull(screenCanvas);
                //SDK内部提供进行播放的view
                mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
            } else if (vt == AliRtcVideoTrackBoth) {
                mViewRemote.setVisibility(View.VISIBLE);
                //多流 相机和屏幕共享
                cameraCanvas = createCanvasIfNull(cameraCanvas);
                //SDK内部提供进行播放的view
                mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                screenCanvas = createCanvasIfNull(screenCanvas);
                //SDK内部提供进行播放的view
                mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
            } else {
                return;
            }
        });

    }

    /**
     * 如果没有画布则创建一个画布，否则使用原来的画布
     *
     * @param canvas
     * @return
     */
    private AliRtcEngine.AliVideoCanvas createCanvasIfNull(AliRtcEngine.AliVideoCanvas canvas) {
        if (canvas == null || canvas.view == null) {
            //创建canvas，Canvas为SophonSurfaceView或者它的子类
            canvas = new AliRtcEngine.AliVideoCanvas();
            SophonSurfaceView surfaceView = mViewRemote;
            surfaceView.setZOrderOnTop(true);
            surfaceView.setZOrderMediaOverlay(true);
            canvas.view = surfaceView;
            //renderMode提供四种模式：Auto、Stretch、Fill、Crop，建议使用Auto模式。
            canvas.renderMode = AliRtcRenderModeAuto;
        }
        return canvas;
    }

    /**
     * 随机生成用户名
     *
     * @return
     */
    public String randomName() {
        Random rd = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            // 你想生成几个字符
            str.append((char) (Math.random() * 26 + 'a'));
        }
        return str.toString();
    }

    public AliRtcEngine getmAliRtcEngine() {
        return mAliRtcEngine;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAliRtcEngine != null) {
            leaveChannel();
            mAliRtcEngine.destroy();
        }
    }
}
