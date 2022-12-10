package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("userName") //join
    @Expose
    private String userName;
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("groupName") //join
    @Expose
    private String groupName;
    @SerializedName("userGoal") //join
    @Expose
    private String userGoal;
    @SerializedName("postID")
    @Expose
    private int postID;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("waterNum") // join and count
    @Expose
    private int waterNum;
    @SerializedName("plantID") // join
    @Expose
    private int plantID;
    @SerializedName("checkItem")
    @Expose
    private List<CheckItemData> checkItem;

    public List<CheckItemData> getCheckItemList() {
        return checkItem;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserGoal() {
        return userGoal;
    }

    public int getPostID() {
        return postID;
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getComment() {
        return comment;
    }

    public int getWaterNum() {
        return waterNum;
    }

    public void setWaterNum(int waterNum) {
        this.waterNum = waterNum;
    }

    public int getPlantID() {
        return plantID;
    }
}
