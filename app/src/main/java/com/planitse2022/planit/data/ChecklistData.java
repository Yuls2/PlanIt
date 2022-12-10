package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChecklistData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("userGoal")
    @Expose
    private String userGoal;
    @SerializedName("checkItem")
    @Expose
    private List<CheckItemData> checkItem;

    public String getUserID() {
        return userID;
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

    public List<CheckItemData> getCheckItemList() {
        return checkItem;
    }

    public CheckItemData getCheckItem(int position) {
        if(position < checkItem.size()) {
            return checkItem.get(position);
        }
        else{
            return null;
        }
    }
}
