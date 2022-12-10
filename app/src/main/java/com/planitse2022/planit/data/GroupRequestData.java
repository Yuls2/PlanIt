package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupRequestData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("introduction")
    @Expose
    private String introduction;

    public String getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getName() {
        return userName;
    }

    public String getDate() {
        return date;
    }
}
