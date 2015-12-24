package com.example.laptrinhmobile.toposmobile.Object;

import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LapTrinhMobile on 10/12/2015.
 */
public class CTHoaDon implements Serializable {
    private String HoaDonID, HangHoaID, MaHH, TenHH, MaNccDM, Serial;
    private int STT, LoaiHangHoaBan, SoLuong;
    private double  DGBan, TriGiaBan, TLCKGiamGia, TienGiamGia, TriGiaSauGiamGia;
    private double TLCK_GioVang, TienCK_GioVang, TriGiaSauGioVang, TLCKHD, TienCKHD;
    private double TriGiaSauCKHD, TLCK_TheGiamGia, TienCK_TheGiamGia, ThanhTienBan, TienPhuThu;
    private double VATDauRa, DonGiaVonBQ, TriGiaVonBQ, DonGiaBQ, TriGiaBQ, TriGiaVATDauRa;
    private Date NgayGioQuet, HanSuDung;
    private int DaGhiHDTC, DaVanChuyen, LaHangKhuyenMai, DaSuDung;

    public CTHoaDon() {}
    public CTHoaDon(Cursor in) {
        this.HoaDonID = in.getString(0);
        this.HangHoaID = in.getString(1);
        this.MaHH = in.getString(2);
        this.STT = in.getInt(3);
        this.LoaiHangHoaBan = in.getInt(4);
        this.TenHH = in.getString(5);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        try {
            date = format.parse(in.getString(6));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.NgayGioQuet = date;
        this.MaNccDM = in.getString(7);
        this.SoLuong = in.getInt(8);
        this.DGBan = in.getDouble(9);
        this.TriGiaBan = in.getDouble(10);
        this.TLCKGiamGia = in.getDouble(11);
        this.TienGiamGia = in.getDouble(12);
        this.TriGiaSauGiamGia = in.getDouble(13);
        this.TLCK_GioVang = in.getDouble(14);
        this.TienCK_GioVang = in.getDouble(15);
        this.TriGiaSauGioVang = in.getDouble(16);
        this.TLCKHD = in.getDouble(17);
        this.TienCKHD = in.getDouble(18);
        this.TriGiaSauCKHD = in.getDouble(19);
        this.TLCK_TheGiamGia = in.getDouble(20);
        this.TienCK_TheGiamGia = in.getDouble(21);
        this.ThanhTienBan = in.getDouble(22);
        this.TienPhuThu = in.getDouble(23);
        this.VATDauRa = in.getDouble(24);
        this.DonGiaVonBQ = in.getDouble(25);
        this.TriGiaVonBQ = in.getDouble(26);
        this.DonGiaBQ = in.getDouble(27);
        this.TriGiaBQ = in.getDouble(28);
        this.DaGhiHDTC = Integer.valueOf(in.getString(29));
        this.DaVanChuyen = Integer.valueOf(in.getString(30));
        this.TriGiaVATDauRa = in.getDouble(31);
        this.LaHangKhuyenMai = Integer.valueOf(in.getString(32));
        this.DaSuDung = Integer.valueOf(in.getString(33));
        try {
            date = format.parse(in.getString(34));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HanSuDung = date;
        this.Serial = in.getString(35);
    }

    public void setHoaDonID(String in_HoaDonID) { this.HoaDonID = in_HoaDonID; }
    public void setHangHoaID(String in_HangHoaID) { this.HangHoaID = in_HangHoaID; }
    public void setMaHH(String in_MaHH) { this.MaHH = in_MaHH; }
    public void setTenHH(String in_TenHH) { this.TenHH = in_TenHH; }
    public void setMaNccDM(String in_MaNccDM) { this.MaNccDM = in_MaNccDM; }
    public void setSerial(String in_Serial) { this.Serial = in_Serial; }
    public void setSTT(int in_STT) { this.STT = in_STT; }
    public void setLoaiHangHoaBan(int in_LoaiHangHoaBan) { this.LoaiHangHoaBan = in_LoaiHangHoaBan; }

    //private long SoLuong, DGBan, TriGiaBan, TLCKGiamGia, TienGiamGia, TriGiaSauGiamGia;
    public void setSoLuong(int in_SoLuong) { this.SoLuong = in_SoLuong; }
    public void setDGBan(double in_DGBan) { this.DGBan = in_DGBan; }
    public void setTriGiaBan(double in_TriGiaBan) { this.TriGiaBan = in_TriGiaBan; }
    public void setTLCKGiamGia(double in_TLCKGiamGia) { this.TLCKGiamGia = in_TLCKGiamGia; }
    public void setTienGiamGia(double in_TienGiamGia) { this.TienGiamGia = in_TienGiamGia; }
    public void setTriGiaSauGiamGia(double in_TriGiaSauGiamGia) { this.TriGiaSauGiamGia = in_TriGiaSauGiamGia; }
    //private long TLCK_GioVang, TienCK_GioVang, TriGiaSauGioVang, TLCKHD, TienCKHD;
    public void setTLCK_GioVang(double in_TLCK_GioVang) { this.TLCK_GioVang = in_TLCK_GioVang; }
    public void setTienCK_GioVang(double in_TienCK_GioVang) { this.TLCK_GioVang = in_TienCK_GioVang; }
    public void setTriGiaSauGioVang(double in_TriGiaSauGioVang) { this.TriGiaSauGioVang = in_TriGiaSauGioVang; }
    public void setTLCKHD(double in_TLCKHD) { this.TLCKHD = in_TLCKHD; }
    public void setTienCKHD(double in_TienCKHD) { this.TienCKHD = in_TienCKHD; }
    //private long TriGiaSauCKHD, TLCK_TheGiamGia, TienCK_TheGiamGia, ThanhTienBan, TienPhuThu;
    public void setTriGiaSauCKHD(double in_TriGiaSauCKHD) { this.TriGiaSauCKHD = in_TriGiaSauCKHD; }
    public void setTLCK_TheGiamGia(double in_TLCK_TheGiamGia) { this.TLCK_TheGiamGia = in_TLCK_TheGiamGia; }
    public void setTienCK_TheGiamGia(double in_TienCK_TheGiamGia) { this.TienCK_TheGiamGia = in_TienCK_TheGiamGia; }
    public void setThanhTienBan(double in_ThanhTienBan) { this.ThanhTienBan = in_ThanhTienBan; }
    public void setTienPhuThu(double in_TienPhuThu) { this.TienPhuThu = in_TienPhuThu; }
    //private long VATDauRa, DonGiaVonBQ, TriGiaVonBQ, DonGiaBQ, TriGiaBQ, TriGiaVATDauRa;
    public void setVATDauRa(double in_VATDauRa) { this.VATDauRa = in_VATDauRa; }
    public void setDonGiaVonBQ(double in_DonGiaVonBQ) { this.DonGiaVonBQ = in_DonGiaVonBQ; }
    public void setTriGiaVonBQ(double in_TriGiaVonBQ) { this.TriGiaVonBQ = in_TriGiaVonBQ; }
    public void setDonGiaBQ(double in_DonGiaBQ) { this.DonGiaBQ = in_DonGiaBQ; }
    public void setTriGiaBQ(double in_TriGiaBQ) { this.TriGiaBQ = in_TriGiaBQ; }
    public void setTriGiaVATDauRa(double in_TriGiaVATDauRa) { this.TriGiaVATDauRa = in_TriGiaVATDauRa; }

    public void setNgayGioQuet(Date in_NgayGioQuet) { this.NgayGioQuet = in_NgayGioQuet; }
    public void setHanSuDung(Date in_HanSuDung) { this.HanSuDung = in_HanSuDung; }
//    private boolean DaGhiHDTC, DaVanChuyen, LaHangKhuyenMai, DaSuDung;
    public void setDaGhiHDTC(int in_DaGhiHDTC) { this.DaGhiHDTC = in_DaGhiHDTC; }
    public void setDaVanChuyen(int in_DaVanChuyen) { this.DaVanChuyen = in_DaVanChuyen; }
    public void setLaHangKhuyenMai(int in_laHangKhuyenMai) { this.LaHangKhuyenMai = in_laHangKhuyenMai; }
    public void setDaSuDung(int in_DaSuDung) { this.DaSuDung = in_DaSuDung; }
//    public void setHanSuDung(Date in_)
    //get data

    public String getHoaDonID() { return this.HoaDonID ; }
    public String getHangHoaID() { return this.HangHoaID; }
    public String getMaHH() { return this.MaHH; }
    public String getTenHH() { return this.TenHH; }
    public String getMaNccDM() { return this.MaNccDM; }
    public String getSerial() { return this.Serial; }
    public int getSTT() { return this.STT; }
    public int getLoaiHangHoaBan() { return this.LoaiHangHoaBan; }
    //private double SoLuong, DGBan, TriGiaBan, TLCKGiamGia, TienGiamGia, TriGiaSauGiamGia;
    public int getSoLuong() { return this.SoLuong; }
    public double getDGBan() { return this.DGBan; }
    public double getTriGiaBan() { return this.TriGiaBan; }
    public double getTLCKGiamGia() { return this.TLCKGiamGia; }
    public double getTienGiamGia() { return this.TienGiamGia; }
    public double getTriGiaSauGiamGia() { return this.TriGiaSauGiamGia; }
    //private double TLCK_GioVang, TienCK_GioVang, TriGiaSauGioVang, TLCKHD, TienCKHD;
    public double getTLCK_GioVang() { return this.TLCK_GioVang; }
    public double getTienCK_GioVang() { return this.TLCK_GioVang; }
    public double getTriGiaSauGioVang() { return this.TriGiaSauGioVang; }
    public double getTLCKHD() { return this.TLCKHD; }
    public double getTienCKHD() { return this.TienCKHD; }
    //private double TriGiaSauCKHD, TLCK_TheGiamGia, TienCK_TheGiamGia, ThanhTienBan, TienPhuThu;
    public double getTriGiaSauCKHD() { return this.TriGiaSauCKHD; }
    public double getTLCK_TheGiamGia() { return this.TLCK_TheGiamGia ; }
    public double getTienCK_TheGiamGia() { return this.TienCK_TheGiamGia; }
    public double getThanhTienBan() { return this.ThanhTienBan; }
    public double getTienPhuThu() { return this.TienPhuThu; }
    //private double VATDauRa, DonGiaVonBQ, TriGiaVonBQ, DonGiaBQ, TriGiaBQ, TriGiaVATDauRa;
    public double getVATDauRa() { return this.VATDauRa; }
    public double getDonGiaVonBQ() { return this.DonGiaVonBQ; }
    public double getTriGiaVonBQ() { return this.TriGiaVonBQ; }
    public double getDonGiaBQ() { return this.DonGiaBQ; }
    public double getTriGiaBQ() { return this.TriGiaBQ; }
    public double getTriGiaVATDauRa() { return this.TriGiaVATDauRa; }

    public Date getNgayGioQuet() { return this.NgayGioQuet; }
    public Date getHanSuDung() { return this.HanSuDung; }
    //private boolean DaGhiHDTC, DaVanChuyen, LaHangKhuyenMai, DaSuDung;
    public int getDaGhiHDTC() { return this.DaGhiHDTC; }
    public int getDaVanChuyen() { return this.DaVanChuyen; }
    public int getLaHangKhuyenMai() { return this.LaHangKhuyenMai; }
    public int getDaSuDung() { return this.DaSuDung; }
}