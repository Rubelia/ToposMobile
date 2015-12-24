package com.example.laptrinhmobile.toposmobile.Object;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 12/1/2015.
 */
public class Setting_Item {
    private String title ;
    private String img;

    public Setting_Item(String in_name, String in_img) {
        title = in_name;
        img = in_img;
    }
    public Setting_Item() {}

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Setting_Item> getList() {
        ArrayList<Setting_Item> arr = new ArrayList<>();
        arr.add(new Setting_Item("Cài đặt Server","icon_add_product"));
        arr.add(new Setting_Item("Cấu hình","icon_add_product"));
        arr.add(new Setting_Item("Đồng bộ","icon_add_product"));
        arr.add(new Setting_Item("Thông tin đồng bộ","icon_add_product"));
        return arr;
    }
}
