package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupRequestData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.AnimationDialog;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GroupRequestAdapter extends RecyclerView.Adapter<GroupRequestAdapter.GroupRequestHolder> {
    private ArrayList<GroupRequestData> list;
    private View view;
    private RetrofitAPI retrofitAPI;
    private int selectedPosition;
    UserData userData;

    public GroupRequestAdapter(List<GroupRequestData> list) {
        this.list = new ArrayList<GroupRequestData>(list);
        createRetrofitAPI();
    }
    public GroupRequestAdapter() {
        this.list = new ArrayList<GroupRequestData>();
        createRetrofitAPI();
    }

    private void createRetrofitAPI() {
        userData = UserData.getInstance();
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
    }

    public void addItem(List<GroupRequestData> list) {
        this.list.addAll(list);
    }

    public void addItem(GroupRequestData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public GroupRequestAdapter.GroupRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_group_requestitem, parent, false);
        return new GroupRequestAdapter.GroupRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRequestAdapter.GroupRequestHolder holder, int position) {
        holder.tvUserName.setText(list.get(position).getName());
        holder.tvIntroduction.setText(list.get(position).getIntroduction());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GroupRequestHolder extends RecyclerView.ViewHolder{
        TextView tvUserName, tvIntroduction;
        Button btnReject, btnAccept;
        public GroupRequestHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.grequest_tv_name);
            tvIntroduction = itemView.findViewById(R.id.grequest_tv_introduction);
            btnAccept = itemView.findViewById(R.id.grequest_btn_accept);
            btnReject = itemView.findViewById(R.id.grequest_btn_reject);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        selectedPosition = pos;
                        TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(),"????????? ????????????????"
                                , list.get(pos).getName() + "?????? ?????? ????????? ????????????????", "?????????", "???");
                        dialog.show();
                        dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retrofitAPI.handleGroupRequest(1, list.get(pos).getUserID(), list.get(pos).getGroupID()).enqueue(handleCallback);
                                dialog.dissmissDailog();
                            }
                        });
                    }
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        selectedPosition = pos;
                        TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(),"????????? ????????????????"
                                , list.get(pos).getName() + "?????? ?????? ????????? ????????????????", "?????????", "???");
                        dialog.show();
                        dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retrofitAPI.handleGroupRequest(0, list.get(pos).getUserID(), list.get(pos).getGroupID()).enqueue(handleCallback);
                                dialog.dissmissDailog();
                            }
                        });
                    }
                }
            });
        }
    }

    private retrofit2.Callback<ResponseData<Integer>> handleCallback = new retrofit2.Callback<ResponseData<Integer>>() {
        @Override
        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
            ResponseData<Integer> responseData = response.body();
            if(responseData.getResult() == 0) {
                if(responseData.getData() == 2) {
                    //?????? ??????????????? ??????, ????????? ??????
                    AnimationDialog dialog = new AnimationDialog(view.getContext(), R.drawable.img_ani_member_in,"?????? ?????? ??????"
                            , "?????? ???????????? ????????? ??????????????????!", "??????!");
                    dialog.show();
                }
                else if(responseData.getData() == 1) {
                    //?????? ????????? ?????? ??????
                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ?????? ??????"
                            , "????????? ????????? ??????????????????.", "?????? ?????????!");
                    dialog.show();
                }
                list.remove(selectedPosition);
                notifyDataSetChanged();
            }
            else if(responseData.getResult() == 4){
                OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ?????? ??????"
                        , "????????? ????????????. ???????????? ???????????? ????????????.");
                dialog.show();
            }
            else if(responseData.getResult() == 10){
                OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ?????? ??????"
                        , "????????? ?????? ?????? ?????? ????????????!");
                dialog.show();
            }
        }

        @Override
        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
            OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ?????? ??????"
                    , "??? ??? ?????? ????????? ??????????????????.");
            dialog.show();
        }
    };
}

