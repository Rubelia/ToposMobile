package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LapTrinhMobile on 10/1/2015.
 */
public class HangHoa implements Serializable {
    private  String HangHoaID, MaHH, TenHH, MaNCC, MaNganh, MaNhom, MaPhanNhom, MaNVCapNhat, DonViTinh, MaKeHang;
    private  int  HienThiTrongDanhMuc;
    private  Date NgayCapNhat;
    private  boolean HangCanKy, MaTam, DaDongBo;
    private int Quantity;
    private double VATDauRa, TienBan;
    private Timer timer;

    private int position;
    public HangHoa() {
        HangHoaID =" ";
        TenHH = "";
        Quantity = 1;
        position = -1;
        timer = new Timer();
    }
//    public HangHoa(Cursor in) {
//        MaNV = in.getString(4);
//        MaQuay = in.getString(5);
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        try {
//            date = format.parse(in.getString(6));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        NgayHD = date;
//        try {
//            date = format.parse(in.getString(7));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        NgayBatDau = date;
//        GioBatDau = in.getInt(8);
//        try {
//            date = format.parse(in.getString(9));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        NgayKetThuc = date;
//        GioKetThuc = in.getInt(10);
//        CaBan = in.getInt(11);
//        DaIn = Boolean.valueOf(in.getString(12));
//        MaHDGoc = in.getString(13);
//        MaTheKHTT = in.getString(14);
//        TriGiaBan = in.getLong(15);
//        TienCK = in.getLong(16);
//        ThanhTienBan = in.getLong(17);
//        TienPhuThu = in.getLong(18);
//        LoaiHoaDon = in.getInt(19);
//        TienTraKhach = in.getLong(20);
//        DaCapNhatTon = Boolean.valueOf(in.getString(21));
//        DaVanChuyen = Boolean.valueOf(in.getString(22));
//        TrungTamTMThuPhi = Boolean.valueOf(in.getString(23));
//    }

    public void setHangHoaId(String in_HangHoaID) {
        this.HangHoaID = in_HangHoaID;
    }
    public void setMaHH(String in_MaHH) {
        this.MaHH = in_MaHH;
    }
    public void setTenHH(String in_TenHH) {
        this.TenHH = in_TenHH;
    }
    public void setMaNCC(String in_MaNCC) {
        this.MaNCC = in_MaNCC;
    }
    public void setMaNganh(String in_MaNganh) {
        this.MaNganh = in_MaNganh;
    }
    public void setMaNhom(String in_MaNhom) {
        this.MaNhom = in_MaNhom;
    }
    public void setMaPhanNhom(String in_MaPhanNhom) {
        this.MaPhanNhom = in_MaPhanNhom;
    }
    public void setMaNVCapNhat(String in_MaNVCapNhat) {
        this.MaNVCapNhat = in_MaNVCapNhat;
    }
    public void setDonViTinh(String in_DonViTinh) {
        this.DonViTinh = in_DonViTinh;
    }
    public void setVATDauRa(double in_VATDauRa) {
        this.VATDauRa = in_VATDauRa;
    }
    public void setTienBan(double in_TienBan) {
        this.TienBan = in_TienBan;
    }
    public void setHienThiTrongDanhMuc(int in_HienThiTrongDanhMuc) {
        this.HienThiTrongDanhMuc = in_HienThiTrongDanhMuc;
    }
    public void setNgayCapNhat(Date in_NgayCapNhat) {
        this.NgayCapNhat = in_NgayCapNhat;
    }
    public void setHangCanKy(boolean in_HangCanKy) { this.HangCanKy = in_HangCanKy; }
    public void setMaTam(boolean in_MaTam) { this.MaTam = in_MaTam; }
    public void setDaDongBo(boolean in_DaDongBo) {
        this.DaDongBo =  in_DaDongBo;
    }
//
    public void setQuantity(int in_Quantity) { this.Quantity = in_Quantity; }
    public void setMaKeHang(String in_MaKeHang) { this.MaKeHang = in_MaKeHang; }
    public void setPosition(int in_Position) { this.position = in_Position; }

    public void setTimer(Timer in_Timer) {
        this.timer = new Timer(in_Timer);
    }

    public String getHangHoaID() {
        return this.HangHoaID;
    }
    public String getMaHH() { return this.MaHH; }
    public String getTenHH() {
        return this.TenHH;
    }
    public String getMaNCC() {  return this.MaNCC; }
    public String getMaNganh() { return this.MaNganh; }
    public String getMaNhom() { return this.MaNhom; }
    public String getMaPhanNhom() { return this.MaPhanNhom; }
    public String getMaNVCapNhat() { return this.MaNVCapNhat; }
    public String getDonViTinh() { return this.DonViTinh; }
    public double getVATDauRa() { return this.VATDauRa; }
    public double getTienBan() { return this.TienBan; }
    public int getHienThiTrongDanhMuc() { return this.HienThiTrongDanhMuc; }
    public Date getNgayCapNhat() { return this.NgayCapNhat; }

    public int getQuantity() { return this.Quantity; }
    public String getMaKeHang() { return this.MaKeHang; }
    public String getDaDongBo() {
        if(DaDongBo == true)
            return "1";
        else return "0";
    }
    public String getHangCanKy() {
        if(DaDongBo == true)
            return "1";
        else return "0";
    }
    public String getMaTam() {
        if(DaDongBo == true)
            return "1";
        else return "0";
    }

    public Timer getTimer() {
        return timer;
    }

    public int getPosition() { return this.position; }
    public boolean isDaDongBo() {
        return DaDongBo;
    }

    public boolean isHangCanKy() {
        return HangCanKy;
    }

    public boolean isMaTam() {
        return MaTam;
    }

}
