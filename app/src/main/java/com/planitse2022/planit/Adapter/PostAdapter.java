package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PostData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.AnimationDialog;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.plant.PlantView;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private ArrayList<PostData> list;
    private int type; // 0: 메인의 요약, 1: 그룹내
    private View view;

    public PostAdapter(List<PostData> list) {
        this(list, 1);
    }
    public PostAdapter(List<PostData> list, int type) {
        this.list = new ArrayList<PostData>(list);
        this.type = type;
    }
    public PostAdapter() {
        this(1);
    }
    public PostAdapter(int type) {
        this.list = new ArrayList<PostData>();
        this.type = type;
    }

    public void addItem(List<PostData> list) {
        this.list.addAll(list);
    }

    public void addItem(PostData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_postitem, parent, false);
        return new PostAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {
        holder.tvUserName.setText(list.get(position).getUserName());
        if(type == 1) {
            holder.tvSubName.setText(list.get(position).getGroupName());
        }
        else if(type == 0) {
            holder.tvSubName.setText(list.get(position).getUserGoal());
        }
        else if(type == 2) {
            holder.tvSubName.setText(list.get(position).getDate() + " 작성");
            holder.layWater.setClickable(false);
        }
        holder.tvComment.setText(list.get(position).getComment());
        holder.tvWaterNum.setText(""+list.get(position).getWaterNum());
        Glide.with(view)
                .load(RetrofitClient.getImageurl("post",list.get(position).getImageURL(),"png"))
                .transform(new CenterCrop(),new RoundedCorners(DPConverter.dpToInt(view.getContext(),10)))
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 캐시는 무시 (재작성 된 경우 때문)
                .skipMemoryCache(true)
                .thumbnail(Glide.with(view.getContext()).load(R.drawable.img_ani_progress))
                .into(holder.img);
        holder.listChecklist.setAdapter(new PostChecklistAdapter(list.get(position).getCheckItemList()));
        Log.e("plant", list.get(position).getUserID() + list.get(position).getGroupID());
        holder.plantView.setPlantByGroupAndUserID(list.get(position).getUserID(), list.get(position).getGroupID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        TextView tvUserName, tvSubName, tvComment, tvWaterNum;
        ListView listChecklist;
        LinearLayout layWater;
        ImageView img;
        PlantView plantView;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.post_tv_name);
            tvSubName = itemView.findViewById(R.id.post_tv_subname);
            tvComment = itemView.findViewById(R.id.post_tv_comment);
            tvWaterNum = itemView.findViewById(R.id.post_tv_waternum);
            listChecklist = itemView.findViewById(R.id.post_list_checklist);
            layWater = itemView.findViewById(R.id.post_lay_water);
            img = itemView.findViewById(R.id.post_img);
            plantView = new PlantView(itemView.findViewById(R.id.post_plant));

            layWater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        UserData userData = UserData.getInstance();
                        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                        retrofitAPI.waterPost(list.get(pos).getGroupID(), list.get(pos).getPostID()).enqueue(new Callback<ResponseData<Integer>>() {
                            @Override
                            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                ResponseData<Integer> responseData = response.body();
                                if(responseData.getResult() == 0) {
                                    PostData item = list.get(pos);
                                    AnimationDialog dialog = new AnimationDialog(view.getContext(), R.drawable.img_ani_water, "고마워요!", item.getUserName()+"님의 식물에게 물을 주었어요!", "야호!");
                                    dialog.show();
                                    item.setWaterNum(item.getWaterNum() + 1);
                                    tvWaterNum.setText(""+item.getWaterNum());
                                }
                                else if(responseData.getResult() == 3) {
                                    // 이미 물을 준 경우
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "물주기 실패", "오늘은 이미 물을 준 글이에요!", "앗, 그랬군요!");
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }
    }

}

