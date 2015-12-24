package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;
import android.util.Log;

import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LapTrinhMobile on 10/9/2015.
 */
public class HoaDon implements Serializable {
    private static final String LOCATION = "HoaDon";
    private String HoaDonID, MaHD, STT, MaNV, MaQuay, MaHDGoc, MaTheKHTT;
    private Date NgayHD, NgayBatDau, NgayKetThuc;
    private int GioBatDau, GioKetThuc, CaBan, LoaiHoaDon, DaIn, DaCapNhatTon, DaVanChuyen, TrungTamTMThuPhi;
    private double TriGiaBan, TienCK, ThanhTienBan, TienPhuThu, TienTraKhach;


    public HoaDon() {
        MaHDGoc = "";
    }
    public HoaDon(Cursor in) {
        HoaDonID = in.getString(0);
        MaHD = in.getString(1);
        STT = in.getString(2);
        MaNV = in.getString(3);
        MaQuay = in.getString(4);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        Date dateNgayHD = new Date();
        try {
            dateNgayHD = format.parse(in.getString(5));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NgayHD = dateNgayHD;
        Date dateNgayBatDau = new Date();
        try {
            dateNgayBatDau = format.parse(in.getString(6));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NgayBatDau = dateNgayBatDau;
        GioBatDau = in.getInt(7);
        Date dateNgayKetThuc = new Date();
        try {
            dateNgayKetThuc = format.parse(in.getString(8));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NgayKetThuc = dateNgayKetThuc;
        GioKetThuc = in.getInt(9);
        CaBan = in.getInt(10);
        DaIn = Integer.valueOf(in.getString(11));
        MaHDGoc = in.getString(12);
        MaTheKHTT = in.getString(13);
        TriGiaBan = in.getLong(14);
        TienCK = in.getLong(15);
        ThanhTienBan = in.getLong(16);
        TienPhuThu = in.getLong(17);
        LoaiHoaDon = in.getInt(18);
        TienTraKhach = in.getLong(19);
        DaCapNhatTon = Integer.valueOf(in.getString(20));
        DaVanChuyen = Integer.valueOf(in.getString(21));
        TrungTamTMThuPhi = Integer.valueOf(in.getString(22));
    }
    //set values
    public void setHoaDonID(String in_HoaDonID) {
        this.HoaDonID = in_HoaDonID;
    }

    public void setMaHD(String in_MaHD) { this.MaHD = in_MaHD; }
    public void setSTT(String in_STT) {
        this.STT = in_STT;
    }
    public void setMaNV(String in_MaNV) {
        this.MaNV = in_MaNV;
    }
    public void setMaQuay(String in_MaQuay) {
        this.MaQuay = in_MaQuay;
    }
    public void setMaHDGoc(String in_MaHDGoc) {
        this.MaHDGoc = in_MaHDGoc;
    }
    public void setMaTheKHTT(String in_MaTheKHTT) {
        this.MaTheKHTT = in_MaTheKHTT;
    }

    public void setNgayHD(Date in_NgayHD) { this.NgayHD = in_NgayHD; }

    public void setNgayBatDau(Date in_NgayBatDau) {
        this.NgayBatDau = in_NgayBatDau;
    }
    public void setNgayKetThuc(Date in_NgayKetThuc) {
        this.NgayKetThuc = in_NgayKetThuc;
    }

    public void setGioBatDau(int in_GioBatDau) {
        this.GioBatDau = in_GioBatDau;
    }
    public void setGioKetThuc(int in_GioKetThuc) {
        this.GioKetThuc = in_GioKetThuc;
    }
    public void setCaBan(int in_CaBan) {
        this.CaBan = in_CaBan;
    }
    public void setLoaiHoaDon(int in_LoaiHoaDon) {
        this.LoaiHoaDon = in_LoaiHoaDon;
    }

    public void setDaIn(int in_DaIn) { this.DaIn = in_DaIn; }
    public void setDaCapNhatTon(int in_DaCapNhatTon) { this.DaCapNhatTon = in_DaCapNhatTon; }
    public void setDaVanChuyen(int in_DaVanChuyen) { this.DaVanChuyen = in_DaVanChuyen; }
    public void setTrungTamTMThuPhi(int in_TrungTamTMThuPhi) { this.TrungTamTMThuPhi = in_TrungTamTMThuPhi; }
    //private double TriGiaBan, TienCK, ThanhTienBan, TienPhuThu, TienTraKhach;
    public void setTriGiaBan(double in_TriGiaBan) { this.TriGiaBan = in_TriGiaBan; }
    public void setTienCK(double in_TienCK) { this.TienCK = in_TienCK; }
    public void setThanhTienBan(double in_ThanhTienBan) { this.ThanhTienBan = in_ThanhTienBan; }
    public void setTienPhuThu(double in_TienPhuThu) { this.TienPhuThu = in_TienPhuThu; }
    public void setTienTraKhach(double in_TienTraKhach) { this.TienTraKhach = in_TienTraKhach; }

    //get
    public String getHoaDonID() {
        return this.HoaDonID;
    }
    public String getMaHD() {
        return this.MaHD;
    }
    public String getSTT() {
        return this.STT;
    }
    public String getMaNV() {
        return this.MaNV;
    }
    public String getMaQuay() {
        return this.MaQuay;
    }
    public String getMaHDGoc() {
        return this.MaHDGoc;
    }
    public String getMaTheKHTT() {
        return this.MaTheKHTT;
    }
    public Date getNgayHD() {
        return this.NgayHD;
    }
    public Date getNgayBatDau() {
        return this.NgayBatDau;
    }
    public Date getNgayKetThuc() {
        return this.NgayKetThuc;
    }

    public int getGioBatDau() {
        return this.GioBatDau;
    }
    public int getGioKetThuc() {
        return this.GioKetThuc;
    }
    public int getCaBan() {
        return this.CaBan;
    }
    public int getLoaiHoaDon() {
        return this.LoaiHoaDon;
    }

    public int getDaIn() { return this.DaIn; }
    public int getDaCapNhatTon() { return this.DaCapNhatTon; }
    public int getDaVanChuyen() { return this.DaVanChuyen; }
    public int getTrungTamTMThuPhi() { return this.TrungTamTMThuPhi; }
    //private double TriGiaBan, TienCK, ThanhTienBan, TienPhuThu, TienTraKhach;
    public double getTriGiaBan() { return this.TriGiaBan; }
    public double getTienCK() { return this.TienCK; }
    public double getThanhTienBan() { return this.ThanhTienBan; }
    public double getTienPhuThu() { return this.TienPhuThu; }
    public double getTienTraKhach() { return this.TienTraKhach; }
}

