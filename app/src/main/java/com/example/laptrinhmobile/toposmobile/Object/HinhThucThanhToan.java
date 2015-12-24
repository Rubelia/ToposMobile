package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LapTrinhMobile on 10/22/2015.
 */
public class HinhThucThanhToan implements Serializable {

    private String MaHinhThuc, MaNhomThanhToan, TenHinhThuc, MoTa, MaThe, MaNguyenTe, TLFee, MaNVTao, MaNVCapNhat, MaChu;
    private Date NgayCapNhat, NgayTao;
    private int STT, TrangThai, DoUuTien;

    public HinhThucThanhToan(Cursor in ) {
        MaHinhThuc = in.getString(0);
        STT = in.getInt(1);
        MaNhomThanhToan = in.getString(2);
        TenHinhThuc = in.getString(3);
        MoTa = in.getString(4);
        MaThe = in.getString(5);
        MaNguyenTe = in.getString(6);
        TLFee = in.getString(7);
        TrangThai = in.getInt(8);
        MaNVTao = in.getString(9);
        Date dateNgayTao = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            dateNgayTao = format.parse(in.getString(10));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NgayTao = dateNgayTao;
        MaNVCapNhat = in.getString(11);
        Date dateNgayNVCapNhat = new Date();
        try {
            dateNgayNVCapNhat = format.parse(in.getString(12));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NgayCapNhat = dateNgayNVCapNhat;
        DoUuTien = in.getInt(13);
        MaChu = in.getString(14);
    }
    public HinhThucThanhToan() {}

    public void setMaHinhThuc(String in) { this.MaHinhThuc = in; }
    public void setSTT(int in) { this.STT = in; }
    public void setMaNhomThanhToan(String in) { this.MaNhomThanhToan = in; }
    public void setTenHinhThuc(String in) { this.TenHinhThuc = in; }
    public void setMoTa(String in) { this.MoTa = in; }
    public void setMaThe(String in) { this.MaThe = in; }
    public void setMaNguyenTe(String in) { this.MaNguyenTe = in; }
    public void setTLFee(String in) { this.TLFee = in; }
    public void setTrangThai(int in) { this.TrangThai = in; }
    public void setMaNVTao(String in) { this.MaNVTao = in; }
    public void setNgayTao(Date in) { this.NgayTao = in; }
    public void setMaNVCapNhat(String in) { this.MaNVCapNhat = in; }
    public void setNgayCapNhat(Date in) { this.NgayCapNhat = in; }
    public void setDoUuTien(int in) { this.DoUuTien = in; }
    public void setMaChu(String in) { this.MaChu = in; }

    public String getMaHinhThuc() { return this.MaHinhThuc ; }
    public int getSTT() { return this.STT; }
    public String getMaNhomThanhToan() { return this.MaNhomThanhToan ; }
    public String getTenHinhThuc() { return this.TenHinhThuc ; }
    public String getMoTa() { return this.MoTa ; }
    public String getMaThe() { return this.MaThe ; }
    public String getMaNguyenTe() { return this.MaNguyenTe ; }
    public String getTLFee() { return this.TLFee ; }
    public int getTrangThai() { return this.TrangThai ; }
    public String getMaNVTao() { return this.MaNVTao ; }
    public Date getNgayTao() { return this.NgayTao ; }
    public String getMaNVCapNhat() { return this.MaNVCapNhat ; }
    public Date getNgayCapNhat() { return this.NgayCapNhat ; }
    public int getDoUuTien() { return this.DoUuTien ; }
    public String getMaChu() { return this.MaChu ; }




}
