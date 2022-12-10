package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("result")
    @Expose
    private int result;

    public int getResult() {
        return result;
    }

    public String getUserID() {
        return userID;
    }

    public String getNickName() {
        return nickName;
    }
}
