package com.planitse2022.planit.util;
/* package 설정해주기 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 저장해 둘 데이터 값
 * 각 데이터의 KEY 값은 ID / PASSWORD / NOTICE_ID
 * id (String) : 아이디
 * password (String) : 비밀번호
 * noticeId (int) : NoticeID
 */

public class AutoLoginManager {
    private static final String PREFERENCES_NAME = "preferences";
    private static final String ID_KEY = "ID";
    private static final String PW_KEY = "PASSWORD";
    private static final String NOTICE_KEY = "NOTICE_ID";
    private static final String FIRST_KEY = "ISFIRST";

    private static boolean isAutoLogin = false;
    private static String userId;
    private static String userPassword;
    private static int noticeId;
    private static int isFirst;

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 저장되어있는 LoginInfo가 있는지
     * @return isAutoLogin
     */
    public static boolean IsAutoLogin() {
        return isAutoLogin;
    }

    /**
     * 자동로그인 설정 변경
     * @param autoLogin
     */
    public static void setIsAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }

    /**
     * 저장된 LoginInfo 삭제
     * @param context
     */
    public static void clearPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putInt(FIRST_KEY, 0);
        editor.apply();

        setIsAutoLogin(false);
    }

    /**
     * 자동 로그인 설정 시 실행해서 LoginInfo 저장
     * @param context
     * @param id
     * @param password
     */
    public static void setLoginInfo(Context context, String id, String password) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ID_KEY, id);
        editor.putString(PW_KEY, password);
        editor.putInt(FIRST_KEY, 0);

        editor.apply();

        setIsAutoLogin(true);
    }

    public static void setNoticeId(Context context, int notice) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NOTICE_KEY, notice);

        editor.apply();
    }

    public static void setIsFirst(Context context, int isFirst) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(FIRST_KEY, isFirst);

        editor.apply();
    }

    /**
     * 저장되어있는 loginInfo 값 가져오기
     * @param context
     * @return loginInfo
     */
    public static LoginInfo getLoginInfo(Context context) {
        SharedPreferences prefs = getPreferences(context);
        LoginInfo loginInfo = new LoginInfo();

        userId = prefs.getString(ID_KEY, "");
        userPassword = prefs.getString(PW_KEY, "");
        noticeId = prefs.getInt(NOTICE_KEY, 0);
        isFirst = prefs.getInt(FIRST_KEY,1);

        loginInfo.id = userId;
        loginInfo.password = userPassword;
        loginInfo.noticeId = noticeId;
        loginInfo.isFirst = isFirst == 1;

        return loginInfo;
    }
}