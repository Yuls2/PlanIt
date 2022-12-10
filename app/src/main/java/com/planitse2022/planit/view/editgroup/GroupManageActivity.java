package com.planitse2022.planit.view.editgroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.planitse2022.planit.Adapter.ManageAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.ManageData;
import com.planitse2022.planit.view.OneListActivity;

import java.util.ArrayList;

public class GroupManageActivity extends OneListActivity {
    public static Activity GROUP_MANAGE_ACTIVITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GROUP_MANAGE_ACTIVITY = GroupManageActivity.this;

        setContent("플래닛 관리", R.drawable.ic_icon_setting, "플래닛 관리");

        ArrayList<ManageData> manageList = new ArrayList<>();
        manageList.add(new ManageData("플래닛 정보 관리"));
        manageList.add(new ManageData("플래닛 정보 수정", "플래닛의 이름, 설명, 규칙, 자동 가입 여부를 수정합니다.", R.drawable.ic_icon_planet_sel, groupID, GroupEditActivity.class));
        manageList.add(new ManageData("플래닛 배경 변경", "플래닛 메인 페이지 화분들의 배경을 변경합니다.", R.drawable.ic_icon_image, groupID, SelectGroupBackgroundActivity.class));
        manageList.add(new ManageData("플래닛 삭제", "플래닛을 삭제합니다. 되돌릴 수 없으니 주의하세요!", R.drawable.ic_icon_planet_delete, groupID, GroupDeleteActivity.class));
        manageList.add(new ManageData("멤버 관리"));
        manageList.add(new ManageData("가입 수락", "우리 플래닛에 가입을 신청한 플래너들의 가입을 수락합니다.", R.drawable.ic_icon_user_sel, groupID, GroupRequestListActivity.class));
        manageList.add(new ManageData("멤버 추방", "특정 멤버를 우리 플래닛에서 추방합니다.", R.drawable.ic_icon_member_off, groupID, MemberManageActivity.class));
        manageList.add(new ManageData("최대 멤버 수 설정", "우리 플래닛에 가입할 수 있는 최대 멤버 수를 설정합니다.", R.drawable.ic_icon_member_sel, groupID, EditMemberNumberActivity.class));
        // manageList.add(new ManageData("관리자 임명", "특정 멤버에게 관리자 권한을 부여합니다.", R.drawable.ic_icon_leader, groupID, TestActivity.class));
        ManageAdapter manageAdapter = new ManageAdapter(manageList);
        listList.setAdapter(manageAdapter);

        listList.setLayoutManager(new LinearLayoutManager(this));
    }
}