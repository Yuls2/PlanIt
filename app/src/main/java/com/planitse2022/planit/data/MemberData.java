package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("userName") //join
    @Expose
    private String userName;
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("userGoal")
    @Expose
    private String userGoal;
    @SerializedName("postList") //join
    @Expose
    private List<PostData> postList;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("plantID") // join
    @Expose
    private int plantID;
    @SerializedName("isManager")
    @Expose
    private boolean isManager;

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getUserGoal() {
        return userGoal;
    }

    public List<PostData> getPostList() {
        return postList;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getPlantID() {
        return plantID;
    }

    public boolean isManager() {
        return isManager;
    }
}
