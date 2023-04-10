package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.Iterator;

public class Login extends AppCompatActivity {

    DatabaseReference sound_Ref= FirebaseDatabase.getInstance().getReference("");
    ValueEventListener deviceListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//메인화면

        //파이어베이스 연동
        mAuth = FirebaseAuth.getInstance();

        //로그인
        Button login_button=(Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                login();
            }
        });

        //회원가입
        Button signUp_button=(Button) findViewById(R.id.sign_up_bt);
        signUp_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(signUp.class);
            }
        });

        //비밀번호 재설정
        Button findPw_button=(Button) findViewById(R.id.find_pw_bt);
        findPw_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                nextActivity(findPw.class);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void login(){
        String email= ((EditText) findViewById(R.id.enter_email)).getText().toString();
        String pw= ((EditText) findViewById(R.id.enter_pw)).getText().toString();

        if(email.length()>0&&pw.length()>0){
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

                                        //사용자 정보 가져온다
                                        String email=ds.child("email").getValue(String.class);
                                        String phone_num=ds.child("phone_num").getValue(String.class);
                                        String residence=ds.child("거주지").getValue(String.class);
                                        String dong=ds.child("동").getValue(String.class);
                                        String num=ds.child("호수").getValue(String.class);
                                        nextMainActivity(0,email,pw,phone_num,residence,dong,num);//건물 관리자임을 의미
                                        break;
                                    }
                                }
                                if(type==1){//주민인 경우
                                    DatabaseReference member_Ref = FirebaseDatabase.getInstance().getReference("member");

                                    member_Ref.addListenerForSingleValueEvent(new ValueEventListener(){
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot){
                                            for(DataSnapshot ds : snapshot.getChildren()) {

                                                String mail=ds.child("email").getValue(String.class);

                                                if(mail.equals(email)){

                                                    //사용자 정보 가져온다
                                                    String email=ds.child("email").getValue(String.class);
                                                    String phone_num=ds.child("phone_num").getValue(String.class);
                                                    String residence=ds.child("거주지").getValue(String.class);
                                                    String dong=ds.child("동").getValue(String.class);
                                                    String num=ds.child("호수").getValue(String.class);

                                                    if(residence==null)//이주민인 경우
                                                        nextMainActivity(2,email,pw,phone_num,residence,dong,num);
                                                    else nextMainActivity(1,email,pw,phone_num,residence,dong,num);//주민임을 의미
                                                    break;
                                                }
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
                    else{
                        //실패했음을 의미하는 Toast 생성
                        Toast.makeText(Login.this,"로그인에 실패하였습니다!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void nextMainActivity(int type,String email,String pw,String phone_num,String residence,String dong,String num){

        //사용자 정보 저장하기위해 Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();//Sharedpreference 초기화

        editor.putInt("authority",type);
        editor.putString("email",email);
        editor.putString("pw",pw);
        editor.putString("phone_num",phone_num);
        editor.putString("residence",residence);
        editor.putString("dong",dong);
        editor.putString("num",num);

        editor.commit();

        //Service 실행
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        //사용자 유형별 화면 이동
        if(type==0){
            editor.putString("nick_name","관리자");
            nextActivity(Fragment_admin_main.class);//건물 관리자 화면으로 이동
            editor.putString("MyAuth","admin"  );
        }
        else if(type==1){
            editor.putString("nick_name",dong+"동"+num+"호");
            editor.putString("MyAuth","member"  );

            checkDevice();
        }
        else
            nextActivity(changeResidence.class);//새로운 거주지 등록하는 화면으로 이동

        editor.commit();
    }

    public void checkDevice(){

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수
        String user_dong = sharedPreferences.getString("dong","");
        String user_num = sharedPreferences.getString("num","");

        //device=0이라면 장치가 등록되어있지 않은것
        DatabaseReference sound_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/"+"device");

        deviceListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int device = snapshot.getValue(int.class);

                if(device==0){//등록x
                    Intent device_intent= new Intent(getApplicationContext(), registerDevice.class);
                    device_intent.putExtra("How","Login");
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

        sound_Ref.addValueEventListener(deviceListener);
    }

    public void nextActivity(Class next){

        sound_Ref.removeEventListener(deviceListener);

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        startActivity(intent);
    }
}