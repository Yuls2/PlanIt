package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupData {
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupComment")
    @Expose
    private String groupComment;
    @SerializedName("groupRule")
    @Expose
    private String groupRule;
    @SerializedName("groupCategory")
    @Expose
    private String groupCategory;
    @SerializedName("userGoal") //join
    @Expose
    private String userGoal;
    @SerializedName("maxMemberNum")
    @Expose
    private int maxMemberNum;
    @SerializedName("memberNum")
    @Expose
    private int memberNum;
    @SerializedName("groupScore")
    @Expose
    private int groupScore;
    @SerializedName("isAutoAccept")
    @Expose
    private boolean isAutoAccept;
    @SerializedName("isJoined")
    @Expose
    private boolean isJoined;
    @SerializedName("isManager")
    @Expose
    private boolean isManager;
    @SerializedName("topMemberPlant")
    @Expose
    private List<Integer> topMemberPlant;
    @SerializedName("background")
    @Expose
    private int background;

    public int getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupComment() {
        return groupComment;
    }

    public String getGroupRule() {
        return groupRule;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public String getUserGoal() {
        return userGoal;
    }

    public int getMaxMemberNum() {
        return maxMemberNum;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public int getGroupScore() {
        return groupScore;
    }

    public boolean isAutoAccept() {
        return isAutoAccept;
    }

    public List<Integer> getTopMemberPlant() {
        return topMemberPlant;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }

    public int getBackground() {
        return background;
    }

    public boolean isManager() {
        return isManager;
    }
}
