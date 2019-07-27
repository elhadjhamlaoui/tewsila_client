package com.tewsila.client;

public interface Communicator{

     void change2(String date, String time, int integer, String pickup, String destination, Boolean vip, int cost, Boolean found,int red_value,String price,boolean credit,String duration,String key);


     void change(int integer, String pickup, String destination, Boolean vip, int cost, String mody, Boolean found,int red_value,String price,boolean credit,String duration,String key);

     void rating(String comment, int rating, String taxiid,int credit,int cost,boolean credit_bool);
      void book(String date, String time);

}



