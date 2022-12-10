package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlantData {
    @SerializedName("plantID")
    @Expose
    private int plantID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("groupID")
    @Expose
    private int groupID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("res1Name")
    @Expose
    private String res1Name;
    @SerializedName("res2Name")
    @Expose
    private String res2Name;
    @SerializedName("isSingle")
    @Expose
    private boolean isSingle;
    @SerializedName("pot")
    @Expose
    private int pot;
    @SerializedName("blueTint")
    @Expose
    private int blueTint;
    @SerializedName("redTint")
    @Expose
    private int redTint;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("growth")
    @Expose
    private int growth;
    @SerializedName("life")
    @Expose
    private int life;

    public int getPlantID() {
        return plantID;
    }

    public String getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getName() {
        return name;
    }

    public String getRes1Name() {
        return res1Name;
    }

    public String getRes2Name() {
        return res2Name;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public int getPot() {
        return pot;
    }

    public int getBlueTint() {
        return blueTint;
    }

    public int getRedTint() {
        return redTint;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getGrowth() {
        return growth;
    }

    public int getLife() {
        return life;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getLevel() {
        if(growth >= 100000) {
            return 5;
        }
        else if(growth >= 50000) {
            return 4;
        }
        else if(growth >= 2000) {
            return 3;
        }
        else if(growth >= 500) {
            return 2;
        }
        else {
            return 1;
        }
    }

    public PlantData() {
        name = "식물";
        res1Name = "img_plant_0";
        isSingle = true;
        pot = 0;
        blueTint = 130;
        redTint = 125;
        growth = 0;
        life = 2;
    }
}
