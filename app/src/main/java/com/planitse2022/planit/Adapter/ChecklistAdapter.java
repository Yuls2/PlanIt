package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistAdapter extends BaseAdapter {
    private ArrayList<CheckItemData> list;

    public ChecklistAdapter() {
        this.list = new ArrayList<CheckItemData>();
    }
    public ChecklistAdapter(List<CheckItemData> list) {
        this.list = new ArrayList<CheckItemData>(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CheckItemData getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        ViewHolder holder = null;
        if(view == null || ((ViewHolder)view.getTag()).id != list.get(i).getCheckID()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lay_checkbox, viewGroup, false);
            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.chkbox_tv_title);
            holder.checkBox = view.findViewById(R.id.chkbox_chk);
            holder.id = list.get(i).getCheckID();
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvTitle.setText(list.get(i).getTitle());
        holder.checkBox.setChecked(list.get(i).isChecked());
        final CheckBox tempCheckBox = holder.checkBox;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 디비에 업데이트
                list.get(i).setChecked(tempCheckBox.isChecked());
                UserData userData = UserData.getInstance();
                RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                retrofitAPI.updateCheckedOfCheckItem(list.get(i).getCheckID(), tempCheckBox.isChecked()?1:0).enqueue(new Callback<ResponseData<Integer>>() {
                    @Override
                    public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                        ResponseData<Integer> responseData = response.body();
                        if(responseData.getResult() != 0) {
                            Log.e("Checked Update Error", responseData.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    public static class ViewHolder {
        public int id;
        public TextView tvTitle;
        public CheckBox checkBox;
    }
    // 체크리스트 상황을 json 배열 문자열로 추출
    public String getCheckListString() {
        boolean isFirst = true;
        String str = "[";
        for(CheckItemData item: list) {
            if(!isFirst) {
                str += ",";
            }else{
                isFirst = false;
            }
            str += item.toString();
        }

        str += "]";
        return str;
    }

    public boolean isSomeChecked() {
        if(list.isEmpty()) {return false;}
        for(CheckItemData item: list) {
            if(item.isChecked()) {
                return true;
            }
        }
        return false;
    }

    public void addItem(CheckItemData item) {
        list.add(item);
    }

    public void addItem(List<CheckItemData> list) {
        this.list.addAll(list);
    }

    public void clear() {
        this.list.clear();
    }

}
