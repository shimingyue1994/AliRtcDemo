package com.yue.libalirtc.callback;

import android.util.Log;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineNotify;

import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;


/**
 * @author shimy
 * @create 2019/12/16 9:32
 * @desc 阿里rtc的通知回调接口，被动接收一些回调，即我们本地未调用任何sdk方法而被动接口的一些回调
 */
public class SimpleAliRtcEngineNotify extends AliRtcEngineNotify {
    private final  String TAG = "SimpleARENotify";

    /**
     * 远端用户停止发布通知，处于OB（observer）状态 停止推送视频 还未退出房间 被动接收
     * @param aliRtcEngine 核心引擎对象
     * @param userId userid 	远端用户ID
     */
    @Override
    public void onRemoteUserUnPublish(AliRtcEngine aliRtcEngine, String userId) {
        Log.i(TAG, "onRemoteUserUnPublish : " + userId);
//        updateRemoteDisplay(userId, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
    }

    /**
     * 远端用户上线通知 进房间 可能还未推送视频  被动通知
     * @param uid userid
     */
    @Override
    public void onRemoteUserOnLineNotify(String uid) {
        Log.i(TAG, "onRemoteUserOnLineNotify : " + uid);
//            addRemoteUser(s);
    }

    /**
     * 远端用户下线通知 退出了房间
     * @param uid userid
     */
    @Override
    public void onRemoteUserOffLineNotify(String uid) {
        Log.i(TAG, "onRemoteUserOffLineNotify : " + uid);
//            removeRemoteUser(s);
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
//        updateRemoteDisplay(uid, aliRtcAudioTrack, aliRtcVideoTrack);
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
    }

    /**
     * 订阅信息
     * @param aliSubscriberInfos 订阅自己这边流的user信息
     * @param count 当前订阅人数
     */
    @Override
    public void onParticipantSubscribeNotify(AliSubscriberInfo[] aliSubscriberInfos, int count) {
        Log.i(TAG, "onParticipantSubscribeNotify : " + count);
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
    }

    /**
     * 取消订阅信息回调
     * @param aliParticipantInfos 订阅自己这边流的user信息
     * @param count 当前订阅人数
     */
    @Override
    public void onParticipantUnsubscribeNotify(AliParticipantInfo[] aliParticipantInfos, int count) {
        Log.i(TAG, "onParticipantUnsubscribeNotify : " + count);
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
    }

    /**
     * 远端用户状态改变通知
     * @param aliStatusInfos 用户状态数组。
     * @param count 数组长度。
     */
    @Override
    public void onParticipantStatusNotify(AliStatusInfo[] aliStatusInfos, int count) {
        Log.i(TAG, "onParticipantStatusNotify : " + count);
    }
}
