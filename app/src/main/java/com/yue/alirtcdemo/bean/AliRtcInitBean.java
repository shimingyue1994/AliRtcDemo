package com.yue.alirtcdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alivc.rtc.AliRtcEngine;

import static com.alivc.rtc.AliRtcEngine.AliRTCCameraType.AliRTCCameraFront;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoProfile.AliRTCSDK_Video_Profile_Default;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera;

/**
 * @author shimy
 * @create 2019/11/27 8:52
 * @desc rtc 基础参数bean 初始化配置
 */
public class AliRtcInitBean implements Parcelable {

    public String userId;//用户ID
    /*设置本地视频是大的画面 false 本地视频显示在小画面上*/
    public boolean localVideoBig = true;
    /*视频来源 默认AliRtcVideoTrackCamera 从摄像头，另外还有屏幕共享请参看文档*/
    public AliRtcEngine.AliRtcVideoTrack videoTrack = AliRtcVideoTrackCamera;
    /*摄像头类型 除前后置 还有一个usb 设置usb时将不会执行预设置摄像头*/
    public AliRtcEngine.AliRTCCameraType cameraType = AliRTCCameraFront;
    /*设置视频流的规格*/
    public AliRtcEngine.AliRtcVideoProfile videoProfile = AliRTCSDK_Video_Profile_Default;
    /*true表示只有音频发布和订阅；false表示音视频都支持。*/
    public boolean audioOnly = false;
    /*扬声器还是听筒 true为扬声器模式；false为听筒模式。*/
    public boolean isSpeaker = true;
    /*日志级别*/
    public AliRtcEngine.AliRtcLogLevel logLevel = AliRtcEngine.AliRtcLogLevel.AliRtcLogLevelError;


    public AliRtcInitBean(String userId) {
        this.userId = userId;
    }



    protected AliRtcInitBean(Parcel in) {
        localVideoBig = in.readByte() != 0;
        audioOnly = in.readByte() != 0;
        isSpeaker = in.readByte() != 0;
    }

    public static final Creator<AliRtcInitBean> CREATOR = new Creator<AliRtcInitBean>() {
        @Override
        public AliRtcInitBean createFromParcel(Parcel in) {
            return new AliRtcInitBean(in);
        }

        @Override
        public AliRtcInitBean[] newArray(int size) {
            return new AliRtcInitBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (localVideoBig ? 1 : 0));
        dest.writeByte((byte) (audioOnly ? 1 : 0));
        dest.writeByte((byte) (isSpeaker ? 1 : 0));
    }
}
