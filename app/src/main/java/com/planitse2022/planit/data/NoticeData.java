package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeData {
    @SerializedName("noticeID")
    @Expose
    private int noticeID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("senderID")
    @Expose
    private String senderID;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("targetID")
    @Expose
    private int targrtID;
    @SerializedName("date")
    @Expose
    private String date;

    public int getNoticeID() {
        return noticeID;
    }

    public String getUserID() {
        return userID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public int getTargrtID() {
        return targrtID;
    }

    public String getDate() {
        //2022-12-04 11:22:21
        return date.substring(0,10) + " " + date.substring(11,13)+"시 " + date.substring(14,16)+"분";
    }

    public String getSenderName() {
        return senderName;
    }
}
