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

public class parkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Park> parkList = null;
    RecyclerView recyclerView;
    Context context;
    View view;

    public parkAdapter(ArrayList<Park> dataList){
        parkList= dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.park_list_item,parent,false);

        return new parkAdapter.ParkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((parkAdapter.ParkViewHolder)holder).name.setText(parkList.get(position).getName());

        //image바꾸기
        int level=parkList.get(position).getLevel();

        //vibration_face.setImageResource(R.drawable.good);
        if(level==1) {//1단계
            ((parkAdapter.ParkViewHolder)holder).level.setText("여유");
            ((parkAdapter.ParkViewHolder)holder).level_img.setImageResource(R.drawable.green);
        }

        else if(level==2) {//2단계
            ((parkAdapter.ParkViewHolder)holder).level.setText("혼잡");
            ((parkAdapter.ParkViewHolder)holder).level_img.setImageResource(R.drawable.orange);
        }
        else {//3단계
            ((parkAdapter.ParkViewHolder)holder).level.setText("만차");
            ((parkAdapter.ParkViewHolder)holder).level_img.setImageResource(R.drawable.red);
        }


        //클릭되면
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position= holder.getChildAdapterPosition(view);
                int position= holder.getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){
                    //주차장이름 보낸다
                    Intent intent = new Intent(context, parking.class);
                    intent.putExtra("park_name",parkList.get(position).getName());
                    intent.putExtra("device_code",parkList.get(position).getDevice_code());
                    intent.putExtra("max_num",parkList.get(position).getMax_num());
                    context.startActivity(intent);
                }
            }
        });
    }

    //리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ParkViewHolder extends RecyclerView.ViewHolder{

        TextView name,level;
        ImageView level_img;

        public ParkViewHolder(@NonNull View itemView) {

            super(itemView);
            name = (TextView)itemView.findViewById(R.id.parkName);
            level = (TextView)itemView.findViewById(R.id.parkLevel);
            level_img=(ImageView)itemView.findViewById(R.id.park_level_img);
        }
    }
}
