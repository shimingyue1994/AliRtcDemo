package com.yue.libalirtc.callback;

import android.util.Log;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;

import org.webrtc.alirtcInterface.ALI_RTC_INTERFACE;

/**
 * @author shimy
 * @create 2019/12/16 9:34
 * @desc 阿里atc的回调监听接口，是我们主动调用sdk的某些方法后的回调
 */
public class SimpleAliRtcEngineEventListener extends AliRtcEngineEventListener {

    private final  String TAG = "EngineEventListener";

    /**
     * 加入房间的回调
     * @param result 结果码:0为加入频道成功，非0为失败。
     */
    @Override
    public void onJoinChannelResult(int result) {
        Log.i(TAG, "onJoinChannelResult : " + result);
    }

    /**
     * 离开房间的回调
     * @param result 结果码 :0为离开频道成功，非0为失败。
     */
    @Override
    public void onLeaveChannelResult(int result) {
        Log.i(TAG, "onLeaveChannelResult : " + result);
    }

    /**
     * 发布音视频流回调。
     * @param result 结果码:0为发布成功，非0为失败。
     * @param publishId publishId 流ID。
     */
    @Override
    public void onPublishResult(int result, String publishId) {
        Log.i(TAG, "onPublishResult : " + result);
    }

    /**
     * 停止发布音视频流回调。
     * @param result 结果码:0为停止发布成功，非0为失败。
     */
    @Override
    public void onUnpublishResult(int result) {
        Log.i(TAG, "onUnpublishResult : " + result);
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
        Log.i(TAG, "onSubscribeResult : " + result);
//        if (result == 0) {
//            updateRemoteDisplay(uid, aliRtcAudioTrack, aliRtcVideoTrack);
//        }
    }

    /**
     * 取消订阅的回调 主动不拉取某个视频
     * @param result 结果码：0表示取消订阅成功，非0表示失败。
     * @param userId userid：用户ID，从App server分配的唯一标示符。
     */
    @Override
    public void onUnsubscribeResult(int result, String userId) {
        Log.i(TAG, "onUnsubscribeResult : " + result);
//        updateRemoteDisplay(userId, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
    }

    /**
     * 网络状态变化的回调
     * @param uid
     * @param upQuality
     * @param downQuality
     */
    @Override
    public void onNetworkQualityChanged(String uid, AliRtcEngine.AliRtcNetworkQuality upQuality, AliRtcEngine.AliRtcNetworkQuality downQuality) {
        Log.i(TAG, "onNetworkQualityChanged : ");
    }

    /**
     * 出现警告的回调
     * @param warn 警告类型
     */
    @Override
    public void onOccurWarning(int warn) {
        Log.i(TAG, "onOccurWarning : " + warn);
    }

    /**
     * 出现错误的回调
     * @param error 错误码
     */
    @Override
    public void onOccurError(int error) {
        Log.i(TAG, "onOccurError : " + error);
        //错误处理
//            processOccurError(error);
    }

    /**
     * 网络连接断开回调。
     */
    @Override
    public void onConnectionLost() {

    }

    /**
     * 重新尝试网络连接回调。
     */
    @Override
    public void onTryToReconnect() {

    }

    /**
     * 网络连接恢复回调。
     */
    @Override
    public void onConnectionRecovery() {

    }

    /**
     * @param oldRole
     * @param newRole
     * 用户角色更新
     */
    @Override
    public void onUpdateRoleNotify(AliRtcEngine.AliRTCSDK_Client_Role oldRole, AliRtcEngine.AliRTCSDK_Client_Role newRole) {

    }
}
