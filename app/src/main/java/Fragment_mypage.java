package com.example.happy_home;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_mypage extends Fragment {

    Button logout_button,changePw_button,leave_button;

    public Fragment_mypage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_mypage, container, false);

        //로그아웃
        logout_button=(Button) v.findViewById(R.id.logout_bt);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                logOut();
            }
        });

        //비밀번호 변경
        changePw_button=(Button) v.findViewById(R.id.mypage_pw_bt);
        changePw_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(changePw.class);
            }
        });

        //장치 변경
        Button addDevice_button=(Button) v.findViewById(R.id.addDevice);
        addDevice_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent device_intent= new Intent(getActivity(), registerDevice.class);
                device_intent.putExtra("How","mypage");
                startActivity(device_intent);
            }
        });

        //회원탈퇴
        leave_button=(Button) v.findViewById(R.id.leave_bt);
        leave_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                signOut();
            }
        });

        //내글 모아보기
        Button mypost_button=(Button) v.findViewById(R.id.mypost_bt);
        mypost_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(MyPostList.class);
            }
        });

        Button myreply_button=(Button) v.findViewById(R.id.myreply_bt);
        myreply_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(MyReplyList.class);
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //로그아웃
    public void logOut(){

        //SharedPreference 불러온다
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("my_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                Toast.makeText(getActivity(),"로그아웃되었습니다!",Toast.LENGTH_SHORT).show();
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

    //회원탈퇴
    public void signOut(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //SharedPreference 불러온다
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("my_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("행복한 주거환경");
        builder.setMessage("탈퇴하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //DB에서 없앤다
                String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수
                String dong = sharedPreferences.getString("dong","");
                String num = sharedPreferences.getString("num","");
                String phone_num=sharedPreferences.getString("phone_num","");

                //거주지에서 삭제
                FirebaseDatabase.getInstance()
                        .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/member/"+phone_num).removeValue();

                //member에서 삭제
                FirebaseDatabase.getInstance().getReference("/member/"+phone_num).removeValue();

                user.delete();//파이어베이스에서 탈퇴
                FirebaseAuth.getInstance().signOut();//파이어베이스에서 로그아웃

                //Sharedpreference 초기화
                editor.clear();
                editor.commit();

                Toast.makeText(getActivity(),"탈퇴되었습니다!",Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), next);
        startActivity(intent);
    }
}