package com.planitse2022.planit.view.group;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.planitse2022.planit.Adapter.PostAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PostData;
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

public class GroupPostFragment extends Fragment {
    private Context context;
    private RecyclerView postList;
    private int groupID;
    private ViewSwitcher listSwitcher;

    private ProgressLayout progressLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_post, container, false);

        groupID = getArguments().getInt("groupID");
        progressLayout = new ProgressLayout(view);
        UserData userData = UserData.getInstance();

        postList = view.findViewById(R.id.list_post);
        listSwitcher = view.findViewById(R.id.switch_list);

        // 리스트에 사용할 어댑터 생성
        PostAdapter postAdapter = new PostAdapter(0);
        // 포스트 리스트 레이아웃 매니저 및 간격 등 설정
        postList.setLayoutManager(new LinearLayoutManager(context));
        postList.setHasFixedSize(true);
        postList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,15), PaddingDecoration.BOTTOM));
        postList.setAdapter(postAdapter);


        progressLayout.addLoading();
        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        // 포스트 리스트 요청
        retrofitAPI.getGroupPostList(groupID).enqueue(new Callback<ResponseData<List<PostData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PostData>>> call, Response<ResponseData<List<PostData>>> response) {
                ResponseData<List<PostData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    List<PostData> data = responseData.getData();
                    postAdapter.addItem(data);
                    postAdapter.notifyDataSetChanged();
                } else {
                    
                }
                // 비어있는 경우 비어있음 표시
                if (postAdapter.getItemCount() > 0) {
                    if (R.id.list_post == listSwitcher.getNextView().getId()) {
                        listSwitcher.showNext();
                    }
                } else if (R.id.lay_empty == listSwitcher.getNextView().getId()) {
                    listSwitcher.showNext();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<PostData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}