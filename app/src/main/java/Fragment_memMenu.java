package com.example.happy_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_memMenu extends Fragment {

    public Fragment_memMenu() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mem_menu, container, false);

        //공지게시판
        Button notice_button=(Button) v.findViewById(R.id.notice_mem_bt);
        notice_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(noticeList.class);
            }
        });

        //소통게시판
        Button comm_button=(Button) v.findViewById(R.id.comm_mem_bt);
        comm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(commuList.class);
            }
        });

        //채팅
        Button chat_mem=(Button) v.findViewById(R.id.chat_mem_bt);
        chat_mem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(Chatting.class);
            }
        });

        //주차시설
        Button park_button=(Button) v.findViewById(R.id.park_mem_bt);
        park_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(parkList.class);
            }
        });

        //편의시설
        Button facility_button=(Button) v.findViewById(R.id.facility_mem_bt);
        facility_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(facilityList.class);
            }
        });

        return v;
    }

    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getActivity(), next);
        startActivity(intent);
    }
}