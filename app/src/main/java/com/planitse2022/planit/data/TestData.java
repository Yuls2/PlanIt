package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("result")
    @Expose
    private int result;

    public String getName() {
        return name;
    }

    public int getResult() {
        return result;
    }
}
