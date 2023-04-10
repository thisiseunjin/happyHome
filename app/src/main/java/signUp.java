package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class signUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference code_Ref=database.getReference("");
    ValueEventListener codeListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //파이어베이스 연동
        mAuth = FirebaseAuth.getInstance();

        //회원가입
        Button signUp_button=(Button) findViewById(R.id.sign_bt);
        signUp_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                check();
            }
        });
    }

    public void check(){//회원가입 메소드

        String email = ((EditText) findViewById(R.id.mail_text)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_text)).getText().toString();
        String phone_num = ((EditText) findViewById(R.id.phonenum_text)).getText().toString();
        String residence = ((EditText) findViewById(R.id.residence_text)).getText().toString();
        String dong = ((EditText) findViewById(R.id.dong_text)).getText().toString();
        String num = ((EditText) findViewById(R.id.num_text)).getText().toString();
        String code = ((EditText) findViewById(R.id.code_text)).getText().toString();

        if(email.length() == 0 || password.length() == 0 || phone_num.length() == 0 || residence.length() == 0 || dong.length() == 0 || num.length() == 0 || code.length() == 0){
            Toast.makeText(getApplicationContext(), "입력하지 않은 곳이 있습니다.", Toast.LENGTH_SHORT).show();
        }
        else {

            //인증번호 확인
            code_Ref = database.getReference("residence/"+residence+"/house/"+dong);
            codeListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()) {

                        if(ds.getKey().equals(num)){

                            //db에 저장된 인증번호 가져온다
                            String db_code=ds.child("code").getValue(String.class);

                            //사용자 입력과 비교한다
                            if(code.equals(db_code)){

                                signup(email,password);

                                //사용자 정보를 Realtime Database에 등록
                                Member mem = new Member(email,phone_num,residence,dong, num);
                                Map<String, Object> mem_val = mem.toMap();
                                HashMap<String, Object> new_mem = new HashMap();

                                DatabaseReference mem_ref = FirebaseDatabase.getInstance().getReference();

                                //1) 관리자인 경우
                                if(dong.equals("관리자")&&num.equals("관리자")){
                                    new_mem.put("/admin/"+phone_num, mem_val);
                                    mem_ref.updateChildren(new_mem);
                                }

                                //2) 주민인 경우
                                else{
                                    new_mem.put("/residence/"+residence+"/house/"+dong+"/"+num+"/member/"+phone_num, 1);
                                    mem_ref.updateChildren(new_mem);

                                    new_mem.put("/member/"+phone_num, mem_val);
                                    mem_ref.updateChildren(new_mem);
                                }

                                nextActivity(Login.class);//로그인 화면으로 이동
                            }
                            else{
                                Toast.makeText(signUp.this,"인증번호를 잘못 입력하셨습니다!",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            code_Ref.addListenerForSingleValueEvent(codeListener);
        }
    }

    public void signup(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {//성공
                    FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(signUp.this,"회원가입 성공!",Toast.LENGTH_SHORT).show();
                }
                else {//실패
                    // If sign in fails, display a message to the user.
                    Toast.makeText(signUp.this,"회원가입 실패!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void nextActivity(Class next){

        code_Ref.removeEventListener(codeListener);

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        startActivity(intent);
    }
}
class Member{
    private String email;
    private String phone_num;
    private String residence;
    private String dong;
    private String num;

    Member(String email, String phone_num, String residence, String dong, String num){
        this.email=email;
        this.phone_num=phone_num;
        this.residence=residence;
        this.dong=dong;
        this.num=num;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("email",email);
        map.put("phone_num",phone_num);
        map.put("거주지", residence);
        map.put("동",dong);
        map.put("호수", num);

        return map;
    }
}