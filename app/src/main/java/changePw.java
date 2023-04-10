package com.example.happy_home;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;

public class changePw extends AppCompatActivity {

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {//작성중단하냐는 notifi띄우나?

        Intent intent = new Intent(getApplicationContext(), Fragment_mem_main.class);
        intent.putExtra("menu_type",2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        Button change_button = (Button) findViewById(R.id.change_bt);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Changing();
            }
        });
    }

    public void Changing(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String new_pw1 = ((EditText)findViewById(R.id.change_1)).getText().toString();
        String new_pw2 = ((EditText)findViewById(R.id.change_2)).getText().toString();
        //새로운 비밀번호 입력칸 2개 있음 1이랑 2가 같으면 바꿔줄거임

        //FirebaseAuth.getInstance().getCurrentUser

        if(new_pw1.length()==0&&new_pw2.length()==0){
            Toast.makeText(getApplicationContext(), "입력하지 않은 곳이 있습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(new_pw1.equals(new_pw2)){
            user.updatePassword(new_pw1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                nextActivity(Fragment_mem_main.class);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        intent.putExtra("menu_type",2);//마이페이지로
        startActivity(intent);
    }
}