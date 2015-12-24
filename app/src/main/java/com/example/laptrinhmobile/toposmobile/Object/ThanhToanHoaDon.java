package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LapTrinhMobile on 10/12/2015.
 */
public class ThanhToanHoaDon implements Serializable {
    private String HoaDonID, MaHinhThuc, MaNhomThanhToan, MaThe, ChuThe;
    private double ThanhTien, TyGiaNgoaiTe, ThanhTienQuyDoi, TLFee;
    private int DaVanChuyen;
    private int STT;

    public ThanhToanHoaDon() { }

    public ThanhToanHoaDon(Cursor in) {
        HoaDonID = in.getString(0);
        MaHinhThuc = in.getString(1);
        MaNhomThanhToan = in.getString(2);
        MaThe = in.getString(3);
        ChuThe = in.getString(4);
        ThanhTien = in.getDouble(5);
        TyGiaNgoaiTe = in.getDouble(6);
        ThanhTienQuyDoi = in.getDouble(7);
        TLFee = in.getDouble(8);
        DaVanChuyen = Integer.valueOf(in.getString(9));
        STT = in.getInt(10);
    }

    public ThanhToanHoaDon(String hoaDonID,HinhThucThanhToan hinhThucThanhToan, String amount) {
        HoaDonID = hoaDonID;
        MaHinhThuc = hinhThucThanhToan.getMaHinhThuc();
        MaNhomThanhToan = hinhThucThanhToan.getMaNhomThanhToan();
        MaThe = hinhThucThanhToan.getMaThe();
        ChuThe = "NULL";
        ThanhTien = Double.valueOf(amount);
        TyGiaNgoaiTe =  1.00;
        ThanhTienQuyDoi = Double.valueOf(amount);
        TLFee = 0.000;
        DaVanChuyen = 0;
        STT = 0;
    }
    public void setHoaDonID(String in_HoaDonID) { this.HoaDonID = in_HoaDonID; }
    public void setMaHinhThuc(String in_MaHinhThuc) { this.MaHinhThuc = in_MaHinhThuc; }
    public void setMaNhomThanhToan(String in_MaNhomThanhToan) { this.MaNhomThanhToan = in_MaNhomThanhToan; }
    public void setMathe(String in_MaThe) { this.MaThe = in_MaThe; }
    public void setChuThe(String in_ChuThe) { this.ChuThe = in_ChuThe; }

    public void setThanhTien(double in_ThanhTien) { this.ThanhTien = in_ThanhTien; }
    public void setTyGiaNgoaiTe(double in_TyGiaNgoaiTe) { this.TyGiaNgoaiTe = in_TyGiaNgoaiTe; }
    public void setThanhTienQuyDoi(double in_ThanhTienQuyDoi) { this.ThanhTienQuyDoi = in_ThanhTienQuyDoi; }
    public void setTLFee(double in_TLFee) { this.TLFee = in_TLFee; }
    public void setDaVanChuyen(int in_DaVanChuyen) { this.DaVanChuyen = in_DaVanChuyen; }
    public void setSTT(int in_STT) { this.STT = in_STT; }
    //get
    public String getHoaDonID() { return this.HoaDonID; }
    public String getMaHinhThuc() { return this.MaHinhThuc; }
    public String getMaNhomThanhToan() { return this.MaNhomThanhToan; }
    public String getMathe() { return this.MaThe; }
    public String getChuThe() { return this.ChuThe; }

    public double getThanhTien() { return  this.ThanhTien; }
    public double getTyGiaNgoaiTe() { return this.TyGiaNgoaiTe; }
    public double getThanhTienQuyDoi() { return this.ThanhTienQuyDoi; }
    public double getTLFee() { return this.TLFee; }
    public int getDaVanChuyen() { return this.DaVanChuyen; }
    public int getSTT() { return this.STT; }

}
