package com.example.happy_home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_adminMenu extends Fragment {

    public Fragment_adminMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_menu, container, false);

        //공지게시판
        Button notice_button=(Button) v.findViewById(R.id.notice_admin_btF);
        notice_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(noticeList.class);
            }
        });

        //소통게시판
        Button comm_button=(Button) v.findViewById(R.id.comm_admin_btF);
        comm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(commuList.class);
            }
        });

        //주차시설현황
        Button cpark_button=(Button) v.findViewById(R.id.current_park_admin_btF);
        cpark_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(parkList.class);
            }
        });

        //편의시설현황
        Button cfacility_button=(Button) v.findViewById(R.id.current_facility_admin_btF);
        cfacility_button.setOnClickListener(new View.OnClickListener(){
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