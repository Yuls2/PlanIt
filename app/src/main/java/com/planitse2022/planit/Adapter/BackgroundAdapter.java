package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.BackgroundData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.editgroup.SelectGroupBackgroundActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundHolder> {
    private ArrayList<BackgroundData> list;
    private View view;
    private int groupID;

    public BackgroundAdapter(List<BackgroundData> list) {
        this.list = new ArrayList<BackgroundData>(list);
        Collections.sort(this.list);
    }
    public BackgroundAdapter() {
        this.list = new ArrayList<BackgroundData>();
    }

    public void addItem(List<BackgroundData> list) {
        this.list.addAll(list);
        Collections.sort(this.list);
    }

    public void addItem(BackgroundData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    @NonNull
    @Override
    public BackgroundAdapter.BackgroundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_backgrounditem, parent, false);
        return new BackgroundAdapter.BackgroundHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundAdapter.BackgroundHolder holder, int position) {
        Glide.with(view.getContext())
                .load(RetrofitClient.getImageurl("background","groupBackground" + list.get(position).getIndex()))
                .transform(new CenterCrop(),new RoundedCorners(DPConverter.dpToInt(view.getContext(),10)))
                .into(holder.imgBackground);
        holder.tvName.setText(list.get(position).getName());
        holder.tvPrice.setText("요구 플래닛 포인트: " + list.get(position).getPrice() + "점 이상");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BackgroundHolder extends RecyclerView.ViewHolder{
        ImageView imgBackground;
        TextView tvName, tvPrice;
        public BackgroundHolder(@NonNull View itemView) {
            super(itemView);
            imgBackground = itemView.findViewById(R.id.back_img_background);
            tvName = itemView.findViewById(R.id.back_tv_name);
            tvPrice = itemView.findViewById(R.id.back_tv_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        BackgroundData item = list.get(pos);

                        UserData userData = UserData.getInstance();
                        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                        retrofitAPI.updateGroupBackground(item.getIndex(), groupID).enqueue(new Callback<ResponseData<Integer>>() {
                            @Override
                            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                ResponseData<Integer> responseData = response.body();
                                if(responseData.getResult() == 0) {
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"변경 성공"
                                            , "플래닛의 배경이 변경되었습니다.", "야호!");
                                    dialog.show();
                                    ((SelectGroupBackgroundActivity)view.getContext()).requestGroupInfo();
                                }
                                else if(responseData.getResult() == 4){
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"변경 실패"
                                            , "권한이 없습니다. 플래닛의 관리자가 아닙니다.");
                                    dialog.show();
                                }
                                else if(responseData.getResult() == 10){
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"변경 실패"
                                            , "플래닛 포인트가 부족합니다!\n" + responseData.getData() + " 포인트 더 필요합니다.");
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"변경 실패"
                                        , "알 수 없는 오류가 발생했습니다.");
                                dialog.show();
                            }
                        });
                    }
                }
            });
        }
    }
}

