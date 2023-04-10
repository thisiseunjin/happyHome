package com.example.happy_home;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;


public class Fragment_memHome extends Fragment {

    View v;
    ImageView noise_face,vibration_face;
    TextView nick_view,dB_word,vibration_word;
    ImageButton level_button,level_cancel_button;

    Dialog level;



    public Fragment_memHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_mem_home, container, false);

        //다이얼로그 생성
        level=new Dialog(getContext());
        level.requestWindowFeature(Window.FEATURE_NO_TITLE);
        level.setContentView(R.layout.level_detail_dialog);
        level.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        level_button=(ImageButton)v.findViewById(R.id.home_level_bt);
        level_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //다이얼로그 보여준다
                level.show();

                //닫기 버튼
                level_cancel_button=(ImageButton)level.findViewById(R.id.level_cancel_bt);
                level_cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        level.dismiss(); // 다이얼로그 닫기
                    }
                });
            }
        });

        //각 view
        nick_view=(TextView)v.findViewById(R.id.nickname_H);
        noise_face = (ImageView)v.findViewById(R.id.noise_face);
        vibration_face = (ImageView)v.findViewById(R.id.vib_face);
        dB_word=(TextView)v.findViewById(R.id.noise_word);
        vibration_word=(TextView)v.findViewById(R.id.vib_text);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        change_face();
    }

    public void change_face(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("my_info", MODE_PRIVATE);
        String user_residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수
        String user_dong = sharedPreferences.getString("dong","");
        String user_num = sharedPreferences.getString("num","");
        String nick_name = sharedPreferences.getString("nick_name","");

        DatabaseReference db_Ref = database.getReference("residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/"+"sound");
        DatabaseReference vibration_Ref = database.getReference("residence/"+user_residence+"/house/"+user_dong+"/"+user_num+"/"+"vibration");

        db_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int db = snapshot.getValue(int.class);

                //nickname set
                nick_view.setText(nick_name);

                if (db<=1){
                    noise_face.setImageResource(R.drawable.good);
                    dB_word.setText("좋음");
                }
                else if (db==2){
                    noise_face.setImageResource(R.drawable.soso);
                    dB_word.setText("보통");
                }
                else{
                    noise_face.setImageResource(R.drawable.bad);
                    dB_word.setText("심각");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        vibration_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int vibration = snapshot.getValue(int.class);

                //nickname set
                nick_view.setText(nick_name);

                if (vibration<=1){
                    vibration_face.setImageResource(R.drawable.good);
                    vibration_word.setText("좋음");
                }
                else if (vibration==2){
                    vibration_face.setImageResource(R.drawable.soso);
                    vibration_word.setText("보통");
                }
                else{
                    vibration_face.setImageResource(R.drawable.bad);
                    vibration_word.setText("심각");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}