package com.example.happy_home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db_Ref,vibration_Ref;

    SharedPreferences sharedPreferences;

    String user_residence,user_dong,user_num,nick_name;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences=this.getSharedPreferences("my_info", MODE_PRIVATE);

    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //백그라운드에서,,
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String user=sharedPreferences.getString("email","");

        if(user !=null && !user.equals("")) {

            user_residence = sharedPreferences.getString("residence", "");//사용자 거주지 저장하는 변수
            user_dong = sharedPreferences.getString("dong", "");
            user_num = sharedPreferences.getString("num", "");
            nick_name = sharedPreferences.getString("nick_name", "");

            if(nick_name.contains("동")&&nick_name.contains("호")){

                //파이어베이스 참조 주소 설정
                db_Ref = database.getReference("residence/" + user_residence + "/house/" + user_dong + "/" + user_num + "/" + "sound");
                vibration_Ref = database.getReference("residence/" + user_residence + "/house/" + user_dong + "/" + user_num + "/" + "vibration");


                db_Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int db = snapshot.getValue(int.class);

                        //심각한 경우
                        if (db > 2) {
                            sendNotification(1);
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

                        //심각한경우
                        if (vibration > 2) {
                            sendNotification(2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendNotification(int type){

        String msg;
        if(type==1)
            msg="소음";
        else msg="진동";

        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            notificationManager.createNotificationChannel( new NotificationChannel("channel1", "알림", NotificationManager.IMPORTANCE_DEFAULT) );
        }

        //알림 누르면 이동하는 액티비티 설정
        Intent intent=new Intent(this,Fragment_mem_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //알림 설정
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel1")
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setPriority(Notification.PRIORITY_MAX)//팝업알림
                .setSmallIcon(R.drawable.circle_logo)
                //.setContentTitle("소통하는 주거환경")
                .setContentText("!!!"+msg+" 심각!!!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1,notificationBuilder.build());
    }
}