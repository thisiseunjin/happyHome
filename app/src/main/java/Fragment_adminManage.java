package com.example.happy_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Fragment_adminManage extends Fragment {

    public Fragment_adminManage() {
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
        View v=inflater.inflate(R.layout.fragment_admin_manage, container, false);

        //주차시설
        Button park_button=(Button) v.findViewById(R.id.park_admin_btF);
        park_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(register_parking.class);
            }
        });

        //편의시설
        Button facility_button=(Button) v.findViewById(R.id.facility_admin_btF);
        facility_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(register_facility.class);
            }
        });

        //입주민 초기화
        Button init_button=(Button) v.findViewById(R.id.codeInit_admin_btF);
        init_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(initialization.class);
            }
        });

        //장치 초기화
        Button Deviceinit_button=(Button) v.findViewById(R.id.deviceInit_admin_btF);
        Deviceinit_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(initialization_device.class);
            }
        });

        //로그아웃
        Button logout_button=(Button) v.findViewById(R.id.logout_admin_btF);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                logOut();
            }
        });

        return v;
    }

    //로그아웃
    public void logOut(){

        //SharedPreference 불러온다
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("my_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("행복한 주거환경");
        builder.setMessage("로그아웃하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //파이어베이스에서 로그아웃
                FirebaseAuth.getInstance().signOut();

                //Sharedpreference 초기화
                editor.clear();
                editor.commit();

                Toast.makeText(getContext(),"로그아웃되었습니다!",Toast.LENGTH_SHORT).show();
                nextActivity(Login.class);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }

    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getActivity(), next);//건물 관리자 화면으로 이동
        startActivity(intent);
    }
}