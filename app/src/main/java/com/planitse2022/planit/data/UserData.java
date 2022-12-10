package com.planitse2022.planit.data;

import android.os.Build;

import java.util.HashMap;

public class UserData {
    private static UserData instance = new UserData();
    private String userNickName, userID, token;
    private HashMap<Integer, Boolean> manager;

    private UserData() {
        manager = new HashMap<>();
    }

    public static UserData getInstance() {
        return instance;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setManager(int groupID, Boolean isManager) {
        manager.put(groupID, isManager);
    }

    public boolean isManager(int groupID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return manager.getOrDefault(groupID, false);
        }
        else {
            if(manager.containsKey(groupID)) {
                return manager.get(groupID);
            }else {
                return false;
            }
        }
    }
    public void managerInfoClear() {
        manager.clear();
    }
}
