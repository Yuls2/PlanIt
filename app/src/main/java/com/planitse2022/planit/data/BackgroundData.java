package com.planitse2022.planit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackgroundData implements Comparable{
    @SerializedName("index")
    @Expose
    private int index;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private int price;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof BackgroundData) {
            int compare = ((BackgroundData) o).getPrice();
            return this.price - compare;
        }
        else {
            return 0;
        }
    }
}
