package com.example.happy_home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class registerDevice extends AppCompatActivity {

    private IntentIntegrator qrScan;

    SharedPreferences sharedPreferences;
    String user_residence,user_dong,user_num;
    Button addSound_bt,addVib_bt;

    String method;
    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        if(method.equals("mypage")){
            finish();
            Intent intent = new Intent(getApplicationContext(), Fragment_mem_main.class);
            intent.putExtra("menu_type",2);
            startActivity(intent);
        }
        else
            super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);

        sharedPreferences=this.getSharedPreferences("my_info", MODE_PRIVATE);
        user_residence = sharedPreferences.getString("residence", "");//사용자 거주지 저장하는 변수
        user_dong = sharedPreferences.getString("dong", "");
        user_num = sharedPreferences.getString("num", "");

        Intent intent=getIntent();
        method = intent.getExtras().getString("How");

        //소리등록
        addSound_bt=(Button) findViewById(R.id.add_sound);
        addSound_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                addSound();
            }
        });

        //진동등록
        addVib_bt=(Button) findViewById(R.id.add_vib);
        addVib_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                addVib();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String device_code="";

        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {

                Toast.makeText(registerDevice.this, "취소!", Toast.LENGTH_SHORT).show();
            }
            else {//qrcode 결과가 있으면

                try {//data를 json으로 변환

                    JSONObject obj = new JSONObject(result.getContents());
                    device_code=obj.getString("device_code");

                    //파이어베이스에 장치 추가
                    DatabaseReference deviceCode_ref = FirebaseDatabase.getInstance().getReference();

                    HashMap<String, Object> new_device = new HashMap();
                    new_device.put("/device/"+"/"+device_code+"/residence/",user_residence);
                    new_device.put("/device/"+"/"+device_code+"/dong/",user_dong);
                    new_device.put("/device/"+"/"+device_code+"/ho/",user_num);
                    deviceCode_ref.updateChildren(new_device);

                    if(device_code.contains("sound")){

                        Task<Void> house_sound = FirebaseDatabase.getInstance()
                                .getReference("/residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/sound").setValue(0);
                        addSound_bt.setText("완료");
                        addSound_bt.setEnabled(false);
                    }
                    else if(device_code.contains("vibration")){

                        Task<Void> house_sound = FirebaseDatabase.getInstance()
                                .getReference("/residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/vibration").setValue(0);
                        addVib_bt.setText("완료");
                        addVib_bt.setEnabled(false);
                    }
                    else{
                        Toast.makeText(registerDevice.this, "잘못스캔하셨습니다!", Toast.LENGTH_SHORT).show();
                    }

                    checkButton();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addSound(){

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("QR코드를 인식해주세요.");
        qrScan.initiateScan();
    }

    public void addVib(){

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("QR코드를 인식해주세요.");
        qrScan.initiateScan();
    }

    public void checkButton(){

        if((!addSound_bt.isEnabled())&&(!addVib_bt.isEnabled())){//둘다 비활성화라면 등록 다 한것!
            //complete_bt.setVisibility(View.VISIBLE);
            Task<Void> complete_device = FirebaseDatabase.getInstance()
                    .getReference("/residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/device").setValue(1);

            Toast.makeText(registerDevice.this, "장치등록을 완료했습니다!", Toast.LENGTH_SHORT).show();
            finish();
            nextActivity(Fragment_mem_main.class);
        }

    }

    public void nextActivity(Class next){

        //다음 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), next);
        startActivity(intent);
    }
}