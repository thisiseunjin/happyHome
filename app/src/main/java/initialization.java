package com.example.happy_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class initialization extends AppCompatActivity {

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), Fragment_admin_main.class);
        intent.putExtra("menu_type",2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);


        //초기화 버튼
        Button initialization_button=(Button) findViewById(R.id.initialization_bt);
        initialization_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TextView new_codeView=(TextView)findViewById(R.id.new_code);

                //번호, 계정 초기화
                String new_code=initialization();
                new_codeView.setText(new_code);
            }
        });
    }

    public String initialization(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String new_code ="";

        //초기화할 동,호수 가져오기
        String dong = ((EditText) findViewById(R.id.enter_dong)).getText().toString();
        String num = ((EditText) findViewById(R.id.enter_num)).getText().toString();

        if(dong.length()==0&&num.length()==0){
            Toast.makeText(getApplicationContext(), "입력하지 않은 곳이 있습니다.", Toast.LENGTH_SHORT).show();
            return "######";
        }

        else{

            //새로운 인증번호 생성, db에 등록
            int code_tmp= (int)(Math.random()*900000)+100000;//100000~999999
            new_code=Integer.toString(code_tmp);

            SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
            String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수

            DatabaseReference code_Ref = database.getReference("residence/"+user_residence+"/house/"+dong+"/"+num);

            HashMap<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("code", new_code);
            code_Ref.updateChildren(tmp);

            //해당 거주지에 등록된 주민 member에서 삭제
            DatabaseReference delList_Ref = FirebaseDatabase.getInstance()
                    .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/member");

            DatabaseReference memList_Ref = FirebaseDatabase.getInstance()
                    .getReference("/member");

            delList_Ref.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot snapshot){
                    for(DataSnapshot del_ds : snapshot.getChildren()) {

                        String del_phone=del_ds.getKey();//삭제할 폰번

                        memList_Ref.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot Snapshot){
                                for(DataSnapshot mem_ds : Snapshot.getChildren()) {

                                    String deleted_num=mem_ds.getKey();//폰번임

                                    if(deleted_num.equals(del_phone)){

                                        FirebaseDatabase.getInstance()
                                                .getReference("/member/"+deleted_num+"/거주지").setValue(null);
                                        FirebaseDatabase.getInstance()
                                                .getReference("/member/"+deleted_num+"/동").setValue(null);
                                        FirebaseDatabase.getInstance()
                                                .getReference("/member/"+deleted_num+"/호수").setValue(null);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError){

                            }
                        });
                    }
                    //해당 거주지에 등록된 모든 주민 삭제
                    Task<Void> house_mem = FirebaseDatabase.getInstance()
                            .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/member").removeValue();

                    //소음,진동기기 초기화
                    Task<Void> house_device = FirebaseDatabase.getInstance()
                            .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/device").setValue(0);
                    Task<Void> house_sound = FirebaseDatabase.getInstance()
                            .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/sound").setValue(-1);
                    Task<Void> house_vibration = FirebaseDatabase.getInstance()
                            .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/vibration").setValue(-1);

                }
                @Override
                public void onCancelled(DatabaseError databaseError){

                }
            });


            return new_code;
        }
    }
}