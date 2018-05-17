package com.skinod.tzzo.skinod.datasave;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author：licheng@uzoo.com
 */

public class ContentBean implements Parcelable {
    private String text="";//文本
    private int emotion = 1;//意图
    private int code=0;//功能
    private String viewflag="error";
    private int type=0; // 1.robot 2.men

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.emotion);
        dest.writeInt(this.code);
        dest.writeString(this.viewflag);
    }

    public ContentBean() {
    }

    public ContentBean(int ty,String text) {
        this.text=text;
        this.type=ty;
    }

    protected ContentBean(Parcel in) {
        this.text = in.readString();
        this.emotion = in.readInt();
        this.code = in.readInt();
        this.viewflag = in.readString();
    }

    public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
        @Override
        public ContentBean createFromParcel(Parcel source) {
            return new ContentBean(source);
        }

        @Override
        public ContentBean[] newArray(int size) {
            return new ContentBean[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getViewFlag() {
        return viewflag;
    }

    public void setViewFlag(String viewflag) {
        this.viewflag = viewflag;
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "text='" + text + '\'' +
                ", emotion=" + emotion +
                ", code=" + code +
                ", viewFlag='" + viewflag + '\'' +
                '}';
    }
}
