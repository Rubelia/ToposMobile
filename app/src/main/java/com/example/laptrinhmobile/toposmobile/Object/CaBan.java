package com.example.laptrinhmobile.toposmobile.Object;

/**
 * Created by LapTrinhMobile on 10/1/2015.
 */
public class CaBan {
    private String STT;
    private String TenCaBan;

    public  CaBan(){
        STT = "";
        TenCaBan = "";
    }
    public CaBan(String in_STT, String in_TenCaBan) {
        this.STT = in_STT;
        this.TenCaBan = in_TenCaBan;
    }

    //set values
    public void setSTT(String in_STT) {
        this.STT = in_STT;
    }
    public void setTenCaBan(String in_TenCaBan) {
        this.TenCaBan = in_TenCaBan;
    }
    //get values
    public String getSTT() {
        return this.STT;
    }
    public String getTenCaBan() {
        return this.TenCaBan;
    }
}
