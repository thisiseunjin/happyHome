package com.example.happy_home;


public class Park implements Comparable<Park>{
    private String name;//주차장 이름
    private String device_code;//장치코드
    private int max_num;//총 공간
    private int current_num;//현재 차량 수
    private int can_num;//남은 공간 수
    private int level;//level

    Park(String name,String device_code,int max_num,int current_num,int can_num,int level){

        this.device_code=device_code;
        this.name=name;
        this.max_num=max_num;
        this.current_num=current_num;
        this.can_num=can_num;
        this.level=level;
    }

    public String getName() {
        return name;
    }

    public String getDevice_code() {
        return device_code;
    }

    public int getMax_num() {
        return max_num;
    }

    public int getCurrent_num() {
        return current_num;
    }

    public int getCan_num() {
        return can_num;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(Park park) {

        //오름차순
        return name.compareTo(park.getName());
    }
}
