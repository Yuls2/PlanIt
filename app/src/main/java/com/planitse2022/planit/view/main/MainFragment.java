package com.planitse2022.planit.view.main;

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

import com.planitse2022.planit.Adapter.ChecklistSummaryAdapter;
import com.planitse2022.planit.Adapter.PostAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.ChecklistData;
import com.planitse2022.planit.data.PostData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private Context context;
    private RecyclerView checklistSummaryList, postList;
    private ChecklistSummaryAdapter checklistSummaryAdapter;
    private PostAdapter postAdapter;
    private RetrofitAPI retrofitAPI;

    private ProgressLayout progressLayout;
    private ViewSwitcher listSwitcher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        progressLayout = new ProgressLayout(view);
        UserData userData = UserData.getInstance();

        checklistSummaryList = view.findViewById(R.id.list_checklist_summary);
        postList = view.findViewById(R.id.list_post);
        listSwitcher = view.findViewById(R.id.switch_list);

        // 리스트에 사용할 어댑터 생성
        checklistSummaryAdapter = new ChecklistSummaryAdapter();
        postAdapter = new PostAdapter(1);
        // 오늘의 할일 리스트는 가로 스크롤 이용
        checklistSummaryList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        checklistSummaryList.setHasFixedSize(true);
        checklistSummaryList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,15), PaddingDecoration.RIGHT));
        checklistSummaryList.setAdapter(checklistSummaryAdapter);
        // 포스트 리스트 레이아웃 매니저 및 간격 등 설정
        postList.setLayoutManager(new LinearLayoutManager(context));
        postList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,15), PaddingDecoration.BOTTOM));
        postList.setAdapter(postAdapter);
        // 유저의 체크리스트 정보 요청하여 체크리스트 업데이트
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        requestCheckList();
        requestPostList();
        // 유저의 그룹들의 포스트 정보 요청하여 포스트 리스트 업데이트

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCheckList();
        requestPostList();
    }

    private void requestPostList() {
        progressLayout.addLoading();
        retrofitAPI.getMainPostList().enqueue(new Callback<ResponseData<List<PostData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PostData>>> call, Response<ResponseData<List<PostData>>> response) {
                ResponseData<List<PostData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    List<PostData> data = responseData.getData();
                    postAdapter.clear();
                    postAdapter.addItem(data);
                    postAdapter.notifyDataSetChanged();
                    if (postAdapter.getItemCount() > 0) {
                        if (R.id.lay_post == listSwitcher.getNextView().getId()) {
                            listSwitcher.showNext();
                        }
                    } else if (R.id.lay_empty == listSwitcher.getNextView().getId()) {
                        listSwitcher.showNext();
                    }
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<PostData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }

    private void requestCheckList() {
        progressLayout.addLoading();
        retrofitAPI.getUserCheckList().enqueue(new Callback<ResponseData<List<ChecklistData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ChecklistData>>> call, Response<ResponseData<List<ChecklistData>>> response) {
                ResponseData<List<ChecklistData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    List<ChecklistData> data = responseData.getData();
                    checklistSummaryAdapter.clear();
                    checklistSummaryAdapter.addItem(data);
                    checklistSummaryAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<ChecklistData>>> call, Throwable t) {
                progressLayout.doneLoading();
                OneBtnDialog dialog = new OneBtnDialog(context, "오류", t.toString());
                dialog.show();
            }
        });
    }
}