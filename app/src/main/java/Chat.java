package com.example.happy_home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat implements Comparable<Chat>{

    private String room_name;//채팅방이름
    private String name;//사람 이름
    private String text;//채팅 내용
    private String long_date;//채팅 날짜,시간
    private String date;//채팅 날짜
    private String time;//채팅 시간
    private int viewType;//채팅방내에서의 위치||왼쪽:0,중간:1,오른쪽:2
    private boolean read;//채팅 읽음 유무

    public String getRoom_name() {
        return room_name;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLong_date() {
        return long_date;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getViewType() {
        return viewType;
    }

    public boolean isRead() {
        return read;
    }

    public Chat(String room_name, String name, String text, String long_date, String date, String time, int viewType, boolean read){

        this.room_name=room_name;
        this.name=name;
        this.text=text;
        this.long_date=long_date;
        this.date=date;
        this.time=time;
        this.viewType=viewType;
        this.read=read;
    }

    @Override
    public int compareTo(Chat chat) {

        Date a,b;

        //date형식으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            a=sdf.parse(long_date);
            b=sdf.parse(chat.long_date);

            //내림차순
            if(a.after(b))
                return -1;
            else return 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
