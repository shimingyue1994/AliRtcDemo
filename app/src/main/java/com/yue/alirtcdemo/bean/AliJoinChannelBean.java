package com.yue.alirtcdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shimy
 * @create 2019/11/26 17:38
 * @desc 加入房间时需要的参数
 */
public class AliJoinChannelBean implements Parcelable {
    public String appid;//appid
    public String channelId;//频道号
    public String userId;//用户ID
    public String nonce;//随机串
    public long timestamp;//时间戳
    public String token;//令牌
    public String[] gslb;//gslb地址


    public AliJoinChannelBean(String appid,String channelId, String userId, String nonce, long timestamp, String token, String[] gslb) {
        this.appid = appid;
        this.channelId = channelId;
        this.userId = userId;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.token = token;
        this.gslb = gslb;
    }


    protected AliJoinChannelBean(Parcel in) {
        appid = in.readString();
        channelId = in.readString();
        userId = in.readString();
        nonce = in.readString();
        timestamp = in.readLong();
        token = in.readString();
        gslb = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appid);
        dest.writeString(channelId);
        dest.writeString(userId);
        dest.writeString(nonce);
        dest.writeLong(timestamp);
        dest.writeString(token);
        dest.writeStringArray(gslb);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AliJoinChannelBean> CREATOR = new Creator<AliJoinChannelBean>() {
        @Override
        public AliJoinChannelBean createFromParcel(Parcel in) {
            return new AliJoinChannelBean(in);
        }

        @Override
        public AliJoinChannelBean[] newArray(int size) {
            return new AliJoinChannelBean[size];
        }
    };
}
