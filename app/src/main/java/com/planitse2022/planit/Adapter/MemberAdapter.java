package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.MemberData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.AnimationDialog;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.PlanitDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.plant.PlantView;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.group.MemberDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {
    private ArrayList<MemberData> list;
    private View view;
    private int mode;

    public MemberAdapter(int mode) {
        this.list = new ArrayList<MemberData>();
        this.mode = mode;
    }

    public MemberAdapter() {
        this(0);
    }

    public void addItem(List<MemberData> list) {
        this.list.addAll(list);
    }

    public void addItem(MemberData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public MemberAdapter.MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_memberitem, parent, false);
        return new MemberAdapter.MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberHolder holder, int position) {
        holder.tvUserName.setText(list.get(position).getUserName());
        if(list.get(position).isManager()) {
            holder.imgManager.setVisibility(View.VISIBLE);
        }
        else {
            holder.imgManager.setVisibility(View.GONE);
        }
        holder.plantView.setPlantByGroupAndUserID(list.get(position).getUserID(), list.get(position).getGroupID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MemberHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        ImageView imgManager;
        PlantView plantView;
        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.member_tv_name);
            imgManager = itemView.findViewById(R.id.member_img_manager);
            plantView = new PlantView(itemView.findViewById(R.id.member_plant_lay));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //?????? ?????? ???????????? ????????????
                        MemberData item = list.get(pos) ;
                        if(mode == 0) { // ?????? ??????
                            Intent in = new Intent(view.getContext(), MemberDetailActivity.class);
                            in.putExtra("memberUserID", item.getUserID());
                            in.putExtra("groupID", item.getGroupID());
                            view.getContext().startActivity(in);
                        }
                        else if(mode == 1) { // ?????? ??????
                            PlanitDialog dialog;
                            if(item.isManager()) {
                                dialog = new OneBtnDialog(view.getContext(), "?????? ??????", "???????????? ???????????? ????????? ??? ????????????!", "????????????...!");
                                dialog.show();
                            }
                            else {
                                dialog = new TwoBtnDialog(view.getContext(), "????????? ?????????????????????????"
                                        , item.getUserName()+"?????? ????????????????????????? ?????? ????????? ??? ????????????!", "?????????!", "??????. " + item.getUserName());
                                dialog.show();

                                ((TwoBtnDialog)dialog).setBtnOnClickListener(2, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //TODO: ?????? ?????? ??????
                                        UserData userData = UserData.getInstance();
                                        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                                        retrofitAPI.deleteMember(item.getUserID(), item.getGroupID()).enqueue(new Callback<ResponseData<Integer>>() {
                                            @Override
                                            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                                ResponseData<Integer> responseData = response.body();
                                                if(responseData.getResult() == 0) {
                                                    //?????? ??????????????? ??????, ????????? ??????
                                                    AnimationDialog dialog = new AnimationDialog(view.getContext(), R.drawable.img_ani_member_out,"?????? ??????"
                                                            , "????????? ????????????, " + item.getUserName(), "??????...?");
                                                    dialog.show();
                                                    list.remove(pos);
                                                    notifyDataSetChanged();
                                                }
                                                else if(responseData.getResult() == 4){
                                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ??????"
                                                            , "????????? ????????????. ???????????? ???????????? ????????????.");
                                                    dialog.show();
                                                }
                                                else if(responseData.getResult() == 10){
                                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "?????? ??????"
                                                            , "???????????? ???????????? ????????? ??? ????????????!", "????????????...!");
                                                    dialog.show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"?????? ??????"
                                                        , "??? ??? ?????? ????????? ??????????????????.");
                                                dialog.show();
                                            }
                                        });
                                        dialog.dissmissDailog();
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

}

