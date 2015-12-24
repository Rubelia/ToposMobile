package com.example.laptrinhmobile.toposmobile.Object;

/**
 * Created by LapTrinhMobile on 10/1/2015.
 */
public class DM_HangHoa {

    private String ID;
    private String TenCaBan;
    public  DM_HangHoa(){
        ID = "";
        TenCaBan = "";
    }
    public DM_HangHoa(String in_ID, String in_TenCaBan) {
        this.ID = in_ID;
        this.TenCaBan = in_TenCaBan;
    }

    //set values
    public void setID(String in_ID) {
        this.ID = in_ID;
    }
    public void setTenCaBan(String in_TenCaBan) {
        this.TenCaBan = in_TenCaBan;
    }
    //get values
    public String getID() {
        return this.ID;
    }
    public String getTenCaBan() {
        return this.TenCaBan;
    }

}
