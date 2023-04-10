package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    DatabaseReference sound_Ref= FirebaseDatabase.getInstance().getReference("");
    ValueEventListener deviceListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            int device = snapshot.getValue(int.class);

            if(device==0){//등록x
                //장치등록 화면으로 이동
                Intent device_intent= new Intent(getApplicationContext(), registerDevice.class);
                device_intent.putExtra("How","splashScreen");
                startActivity(device_intent);
            }

            else{
                nextActivity(Fragment_mem_main.class);//주민 화면으로 이동
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //파이어베이스 연동
        mAuth = FirebaseAuth.getInstance();

        //Service 실행
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        //로그인 유지
        login();

    }

    public void login(){

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        String pw=sharedPreferences.getString("pw","");

        //저장된 사용자 정보가 있는 경우
        if(email !=null && !email.equals("")){

            mAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //로그인에 성공한 경우
                    if(task.isSuccessful() == true){

                        FirebaseUser user = mAuth.getCurrentUser();

                        //건물 관리자인지 확인
                        DatabaseReference admin_Ref = FirebaseDatabase.getInstance().getReference("admin");
                        admin_Ref.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot snapshot){

                                int type=1;

                                for(DataSnapshot ds : snapshot.getChildren()) {

                                    String mail=ds.child("email").getValue(String.class);
                                    if(mail.equals(email)){//관리자인 경우

                                        type=0;//관리자 권한으로 변경
                                        nextActivity(Fragment_admin_main.class);
                                        break;
                                    }
                                }
                                //주민인 경우
                                if(type==1){

                                    DatabaseReference member_Ref = FirebaseDatabase.getInstance().getReference("member");
                                    member_Ref.addListenerForSingleValueEvent(new ValueEventListener(){
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot){
                                            int check=1;

                                            for(DataSnapshot ds : snapshot.getChildren()) {

                                                String mail=ds.child("email").getValue(String.class);

                                                if(mail.equals(email)){
                                                    String residence=ds.child("거주지").getValue(String.class);

                                                    if(residence==null)//이주민인 경우
                                                        nextActivity(changeResidence.class);
                                                    else checkDevice();

                                                    check=0;
                                                    break;
                                                }

                                            }
                                            if(check==1){
                                                nextActivity(Login.class);
                                            }

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError){

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError){

                            }
                        });
                    }
                    //로그인에 실패한 경우
                    else{
                        nextActivity(Login.class);
                    }
                }
            });
        }

        //저장된 사용자 정보가 없는 경우
        else{
            nextActivity(Login.class);
        }
    }

    public void checkDevice(){

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수
        String user_dong = sharedPreferences.getString("dong","");
        String user_num = sharedPreferences.getString("num","");

        //device=0이라면 장치가 등록되어있지 않은것
        sound_Ref = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/"+"device");

        sound_Ref.addValueEventListener(deviceListener);
    }

    public void nextActivity(Class next){

        sound_Ref.removeEventListener(deviceListener);

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        startActivity(intent);
    }
}