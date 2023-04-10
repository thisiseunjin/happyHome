package com.example.happy_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class initialization_device extends AppCompatActivity {

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
        setContentView(R.layout.activity_initialization_device);

        //초기화 버튼
        Button device_initialization_button=(Button) findViewById(R.id.device_initialization_bt);
        device_initialization_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                device_init();
            }
        });
    }

    public void device_init(){

        //초기화할 동,호수 가져오기
        String dong = ((EditText) findViewById(R.id.device_enter_dong)).getText().toString();
        String num = ((EditText) findViewById(R.id.device_enter_num)).getText().toString();

        if(dong.length()==0&&num.length()==0){
            Toast.makeText(getApplicationContext(), "입력하지 않은 곳이 있습니다.", Toast.LENGTH_SHORT).show();
        }

        else{

            SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
            String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수


            //소음,진동기기 초기화
            Task<Void> house_device = FirebaseDatabase.getInstance()
                    .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/device").setValue(0);
            Task<Void> house_sound = FirebaseDatabase.getInstance()
                    .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/sound").setValue(-1);
            Task<Void> house_vibration = FirebaseDatabase.getInstance()
                    .getReference("/residence/"+user_residence+"/house/"+dong+"/"+num+"/vibration").setValue(-1);

            Toast.makeText(this, "해당세대의 장치가 초기화되었습니다!", Toast.LENGTH_SHORT).show();
            nextActivity(Fragment_admin_main.class);

        }
    }

    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        intent.putExtra("menu_type",2);
        startActivity(intent);
    }
}