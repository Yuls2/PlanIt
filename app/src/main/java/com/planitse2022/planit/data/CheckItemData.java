package com.planitse2022.planit.data;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckItemData {
    @SerializedName("checkID")
    @Expose
    private int checkID;
    @SerializedName("type")
    @Expose
    private int type; //0: 요일별, 1: 날짜
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("isChecked")
    @Expose
    private boolean isChecked;
    @SerializedName("priority")
    @Expose
    private int priority;


    public int getCheckID() {
        return checkID;
    }

    public int getType() {
        return type;
    }

    public int getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getPriority() {
        return priority;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "{" +
                "\"checkID\":" + checkID + "," +
                "\"title\":\"" + title + "\"," +
                "\"isChecked\":" + isChecked + "," +
                "\"priority\":" + priority +
                '}';
    }

    public boolean isToday(){
        if(type == 0) {
            if (isIncludeDay()) {
                Log.d(title,"is today!");
                return true;
            } else {
                return false;
            }
        }
        else {
            Date today = new Date(); // 오늘 날짜 가져오기(시간 정보 포함)
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //  "yyyy-MM-dd" format으로 설정하기 위한 formatter
            String todayString = formatter.format(today); // Date 타입을 "yyyy-MM-dd"포맷의 String 형태로 변환
            Log.d(title,todayString);

            if (date.equals(todayString)) { // this.date와 today date를 비교
                Log.d(title,"is today!");
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isIncludeDay(int checkDay) {
        // 0~127 해당 요일 위치의 bit가 1이면 포함
        if(day == 0 || day == 127) { return true; } //매일인 경우
        int bound = (int) Math.pow(2,checkDay+1);
        return day%bound >= (bound/2);
    }

    public boolean isIncludeDay() {
        Calendar today = Calendar.getInstance(); // 현재 날짜 가져오기
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK)-1;
        return isIncludeDay(dayOfWeek);
    }

    public String getSelectedDateString() {
        String str = "";
        if(type == 0) {
            if(day == 0 || day == 127) {
                str = "매일";
            }
            else {
                str = "매주";
                String strDays = Integer.toBinaryString(day);
                final String DAYS[] = {"일", "월", "화", "수", "목", "금", "토"};
                int dayCount = 0;
                for(int i = 0; i < strDays.length(); i++) {
                    if(strDays.charAt(strDays.length() - i - 1) == '1') {
                        if(dayCount > 0) str += ",";
                        str += " " + DAYS[i];
                        dayCount++;
                    }
                }
            }
        }
        else {
            String strDate[] = date.split("-");
            str = strDate[0] + "년 " + strDate[1] + "월 " + strDate[2] + "일";
        }
        return str;
    }
}
