package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class findPw extends AppCompatActivity {

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        Button change_pw = (Button)findViewById(R.id.change_pw_bt);

        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_massage();
            }
        });

    }
    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        startActivity(intent);
    }

    public void send_massage(){
        String email = ((EditText)findViewById(R.id.email_pw_text)).getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(findPw.this,"이메일로 발송되었습니다!",Toast.LENGTH_SHORT).show();
                            nextActivity(Login.class);
                        }
                        else{
                            Toast.makeText(findPw.this,email,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}