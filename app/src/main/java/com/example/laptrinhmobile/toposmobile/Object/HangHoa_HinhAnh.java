package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;

/**
 * Created by LapTrinhMobile on 10/27/2015.
 */
public class HangHoa_HinhAnh {
    private String HangHoaID;
    private byte[] HinhAnh;
    private String Path;

    public HangHoa_HinhAnh(Cursor in) {
        HangHoaID = in.getString(0);
//        Path = in.getString(1);
        HinhAnh = in.getBlob(1);
    }
    public HangHoa_HinhAnh(String hangHoaID, byte[] hinhAnh) {
        HangHoaID = hangHoaID;
//        Path = path;
        HinhAnh = hinhAnh;
    }

    public HangHoa_HinhAnh() {}
    public void setHangHoaID(String hangHoaID) { HangHoaID = hangHoaID; }

    public void setHinhAnh(byte[] hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public byte[] getHinhAnh() {
        return HinhAnh;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getPath() {
        return Path;
    }

    public String getHangHoaID() { return HangHoaID; }

}
