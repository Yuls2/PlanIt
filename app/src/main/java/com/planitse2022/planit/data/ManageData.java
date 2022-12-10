package com.planitse2022.planit.data;

import android.content.Context;
import android.content.Intent;

public class ManageData {
    private String title, comment;
    private int icon, viewType, groupID;
    Class actClass;

    public ManageData(String title, String comment, int icon, int groupID, Class actClass) {
        this.title = title;
        this.comment = comment;
        this.icon = icon;
        this.groupID = groupID;
        this.viewType = 0;
        this.actClass = actClass;
    }

    public ManageData(String title) {
        this.title = title;
        this.viewType = 1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public Intent getIntent(Context context) {
        return new Intent(context, actClass);
    }
}
