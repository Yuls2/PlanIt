package com.planitse2022.planit.view.group;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.Adapter.MemberAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.MemberData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMemberFragment extends Fragment {
    private Context context;
    private RecyclerView memberList;
    private int groupID;

    private ProgressLayout progressLayout;
    private RetrofitAPI retrofitAPI;
    private MemberAdapter memberAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_member, container, false);

        groupID = getArguments().getInt("groupID");
        progressLayout = new ProgressLayout(view);
        progressLayout.setBackgroundColor(R.color.pi_back);
        UserData userData = UserData.getInstance();

        memberList = view.findViewById(R.id.list_member);

        // 리스트에 사용할 어댑터 생성
        memberAdapter = new MemberAdapter();
        // 멤버 리스트 레이아웃 매니저 및 간격 등 설정
        memberList.setLayoutManager(new GridLayoutManager(context, 2));
        memberList.setHasFixedSize(true);
        memberList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,20), PaddingDecoration.RIGHT));
        memberList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,20), PaddingDecoration.BOTTOM));
        memberList.setAdapter(memberAdapter);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        requestMember();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestMember();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void requestMember() {
        progressLayout.addLoading();
        retrofitAPI.getGroupMemberList(groupID).enqueue(new Callback<ResponseData<List<MemberData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MemberData>>> call, Response<ResponseData<List<MemberData>>> response) {
                ResponseData<List<MemberData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    memberAdapter.clear();
                    memberAdapter.addItem(responseData.getData());
                    memberAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<MemberData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}