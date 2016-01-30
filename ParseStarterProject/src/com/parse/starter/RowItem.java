package com.parse.starter;

import android.widget.ImageView;

/**
 * Created by root on 25/1/16.
 */
public class RowItem {
    private String name;
    private String phone1;
    private String phone2;
    private int imageView;

//    public RowItem(String name,ImageView imageView, String phone1, String phone2) {
    public RowItem(String name,int imageView) {
        this.name = name;
        this.imageView=imageView;
//        this.phone1 = phone1;
//        this.phone2 = phone2;
    }
    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public int getImageView(){return imageView;}
    public void setImageView(int imageView){this.imageView=imageView;}
//    public String getphone2() {
//        return phone2;
//    }
//    public void setphone2(String phone2) {
//        this.phone2 = phone2;
//    }
//    public String getphone1() {
//        return phone1;
//    }
//    public void setphone1(String phone1) {
//        this.phone1 = phone1;
//    }
    @Override
    public String toString() {
        return phone1 + "\n" + phone2;
    }
}
