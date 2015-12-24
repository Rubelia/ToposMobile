package com.example.laptrinhmobile.toposmobile.Object;

/**
 * Created by LapTrinhMobile on 9/29/2015.
 */
public class ItemMenu {
    private String imgUrl ;
    private String Name;

    public ItemMenu()
    {
        imgUrl = "";
        Name = "";
    }

    //set values;
    public void setImgUrl(String in_imgUrl) {
        this.imgUrl = in_imgUrl;
    }
    public void setName(String in_name) {
        this.Name = in_name;
    }
    //get
    public String getImgUrl() { return this.imgUrl; }
    public String getName() { return this.Name; }
}
