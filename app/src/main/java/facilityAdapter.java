package com.example.happy_home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class facilityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Facility> facilityList = null;
    RecyclerView recyclerView;
    Context context;
    View view;

    public facilityAdapter(ArrayList<Facility> dataList){
        facilityList= dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.facility_list_item,parent,false);

        return new facilityAdapter.FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((facilityAdapter.FacilityViewHolder)holder).name.setText(facilityList.get(position).getName());

        //image바꾸기
        int level=facilityList.get(position).getLevel();

        //vibration_face.setImageResource(R.drawable.good);
        if(level==1) {//1단계
            ((facilityAdapter.FacilityViewHolder)holder).level.setText("여유");
            ((facilityAdapter.FacilityViewHolder)holder).level_img.setImageResource(R.drawable.green);
        }

        else if(level==2) {//2단계
            ((facilityAdapter.FacilityViewHolder)holder).level.setText("보통");
            ((facilityAdapter.FacilityViewHolder)holder).level_img.setImageResource(R.drawable.orange);
        }
        else {//3단계
            ((facilityAdapter.FacilityViewHolder)holder).level.setText("혼잡");
            ((facilityAdapter.FacilityViewHolder)holder).level_img.setImageResource(R.drawable.red);
        }


        //클릭되면
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position= holder.getChildAdapterPosition(view);
                int position= holder.getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){
                    //주차장이름 보낸다
                    Intent intent = new Intent(context, facility_act.class);
                    intent.putExtra("facility_name",facilityList.get(position).getName());
                    intent.putExtra("device_code",facilityList.get(position).getDevice_code());
                    intent.putExtra("max_num",facilityList.get(position).getMax_num());
                    context.startActivity(intent);
                }
            }
        });
    }

    //리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public class FacilityViewHolder extends RecyclerView.ViewHolder{

        TextView name,level;
        ImageView level_img;

        public FacilityViewHolder(@NonNull View itemView) {

            super(itemView);
            name = (TextView)itemView.findViewById(R.id.facilityName);
            level = (TextView)itemView.findViewById(R.id.facilityLevel);
            level_img=(ImageView)itemView.findViewById(R.id.facility_level_img);
        }
    }
}
