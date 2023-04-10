package com.example.happy_home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Board implements Comparable<Board>{

   // protected int post_num;
    public String PostKey;
    protected String Time ; //작성시간
    protected String Title; //글의 제목
    protected String Writer;    //글의 작성자 닉네임으로 할꺼얌
    protected String Text; //글의 내용을 저장해 줄 것이다.
    protected String Phone;

    public Board(){}
    public Board(String Time, String Title, String Writer, String Text,String Phone){


        this.Time = Time;
        this.Title = Title;
        this.Writer = Writer;
        this.Text = Text;
        this.Phone = Phone;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("Phone",Phone);
        result.put("Time",Time);
        result.put("Title",Title);
        result.put("Writer",Writer);
        result.put("Text",Text);

        return result;

    }

    public String getPost_Key(){return PostKey;}
    public void setPost_Key(String post_Key){this.PostKey = post_Key;}

    @Override
    public int compareTo(Board board) {

        Date a,b;

        //date형식으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. hh:mm:ss");
        try {
            a=sdf.parse(Time);
            b=sdf.parse(board.Time);

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
