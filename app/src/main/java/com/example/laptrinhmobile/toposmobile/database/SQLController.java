package com.example.laptrinhmobile.toposmobile.database;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.laptrinhmobile.toposmobile.BlueToothPrinter;
import com.example.laptrinhmobile.toposmobile.Object.CTHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa_HinhAnh;
import com.example.laptrinhmobile.toposmobile.Object.HinhThucThanhToan;
import com.example.laptrinhmobile.toposmobile.Object.HoaDon;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.Timer;
import com.example.laptrinhmobile.toposmobile.Other.PublicFunction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by LapTrinhMobile on 10/6/2015.
 */
public class SQLController {

//    private static String DB_PATH = "/data/data/com.example.laptrinhmobile.toposmobile/databases/";

//    private static String DB_NAME = "topos_client_mobile";
    private static String LOCATION = "SQLController";

    private DbHelper dbHelper;
    private Context myContext;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        myContext = c;
    }

    public void instance() {
        dbHelper = new DbHelper(myContext);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            Log.d(LOCATION, "IOException: " + ioe.getMessage());
        }
    }
    public boolean isTableExist(String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public void createTable() {
//       Cursor cursor = da
        Timer time = new Timer();
        String nameHD = "HoaDon" + time.getStrMonth()+time.getYear();
//        String nameHD = "HoaDon";
        String nameCTHD = "CTHoaDon" + time.getMonth()+time.getYear();
        String nameThanhToanHoaDon = "ThanhToanHoaDon" + time.getMonth()+time.getYear();
//        String checkTable ="select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nameHD + "';";
//        Cursor getHD = database.rawQuery(checkTable,null);
        if(isTableExist(nameHD)) {
            Log.d(LOCATION,"Đã có bảng " + nameHD);
        } else {
            //TODO:
            String sqlCreateHD = "CREATE TABLE `"+nameHD+"` (\n" +
                    "\t`HoaDonID`\tNVARCHAR(36) NOT NULL,\n" +
                    "\t`MaHD`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`STT`\tBIGINT NOT NULL,\n" +
                    "\t`MaNV`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`MaQuay`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`NgayHD`\tDATETIME NOT NULL,\n" +
                    "\t`NgayBatDau`\tTEXT,\n" +
                    "\t`GioBatDau`\tINT,\n" +
                    "\t`NgayKetThuc`\tTEXT,\n" +
                    "\t`GioKetThuc`\tINTEGER,\n" +
                    "\t`CaBan`\tINTEGER NOT NULL,\n" +
                    "\t`DaIn`\tINTEGER NOT NULL,\n" +
                    "\t`MaHDGoc`\tNVARCHAR(36),\n" +
                    "\t`MaTheKHTT`\tNVARCHAR(20),\n" +
                    "\t`TriGiaBan`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienCK`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`ThanhTienBan`\tNUMERIC(19,2),\n" +
                    "\t`TienPhuThu`\tNUMERIC(19,2),\n" +
                    "\t`LoaiHoaDon`\tINTEGER NOT NULL,\n" +
                    "\t`TienTraKhach`\tNUMERIC(19,2),\n" +
                    "\t`DaCapNhatTon`\tINTEGER,\n" +
                    "\t`DaVanChuyen`\tINTEGER,\n" +
                    "\t`TrungTamTMThuPhi`\tINTEGER,\n" +
                    "\tPRIMARY KEY(HoaDonID)\n" +
                    ");";
            try {
                database.execSQL(sqlCreateHD);

            } catch (SQLException exception) {
                Log.d(LOCATION, exception.getMessage());
            }
            Log.d(LOCATION,"Create table succesful");

        }
//        checkTable ="select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nameCTHD + "';";
//        Cursor getCTHD = database.rawQuery(checkTable,null);
        if(isTableExist(nameCTHD)) {
            Log.d(LOCATION,"Đã có bảng " + nameCTHD);
        } else {
            //TODO : create new table CTHD
            String sqlCreateCTHD = "CREATE TABLE `"+nameCTHD+"` (\n" +
                    "\t`HoaDonID`\tNVARCHAR(36) NOT NULL,\n" +
                    "\t`HangHoaID`\tNVARCHAR(36) NOT NULL,\n" +
                    "\t`MaHH`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`STT`\tINTEGER NOT NULL,\n" +
                    "\t`LoaiHangHoaBan`\tINTEGER,\n" +
                    "\t`TenHH`\tNVARCHAR(255) NOT NULL,\n" +
                    "\t`NgayGioQuet`\tTEXT,\n" +
                    "\t`MaNccDM`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`SoLuong`\tNUMERIC(19,3) NOT NULL,\n" +
                    "\t`DGBan`\tNUMERIC(19,4) NOT NULL,\n" +
                    "\t`TriGiaBan`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TLCKGiamGia`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienGiamGia`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TriGiaSauGiamGia`\tNUMERIC(19,2),\n" +
                    "\t`TLCK_GioVang`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienCK_GioVang`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TriGiaSauGioVang`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TLCKHD`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienCKHD`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TriGiaSauCKHD`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TLCK_TheGiamGia`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienCK_TheGiamGia`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`ThanhTienBan`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TienPhuThu`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`VATDauRa`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`DonGiaVonBQ`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TriGiaVonBQ`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`DonGiaBQ`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TriGiaBQ`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`DaGhiHDTC`\tINTEGER NOT NULL,\n" +
                    "\t`DaVanChuyen`\tINTEGER NOT NULL,\n" +
                    "\t`TriGiaVATDauRa`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`LaHangKhuyenMai`\tINTEGER,\n" +
                    "\t`DaSuDung`\tINTEGER,\n" +
                    "\t`HanSuDung`\tTEXT,\n" +
                    "\t`Serial`\tNVARCHAR(200),\n" +
                    "\tPRIMARY KEY(HoaDonID,HangHoaID,STT)\n" +
                    ");";
            database.execSQL(sqlCreateCTHD);
        }
//        checkTable ="select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nameThanhToanHoaDon + "';";
//        Cursor getThanhToanHoaDon = database.rawQuery(checkTable,null);
        if(isTableExist(nameThanhToanHoaDon)) {
            Log.d(LOCATION,"Đã có bảng " + nameThanhToanHoaDon);
        } else {
            //TODO:
            String sqlCreateThanhToanHoaDon = "CREATE TABLE `"+nameThanhToanHoaDon+"` (\n" +
                    "\t`HoaDonID`\tNVARCHAR(36) NOT NULL,\n" +
                    "\t`MaHinhThuc`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`MaNhomThanhToan`\tNVARCHAR(20) NOT NULL,\n" +
                    "\t`MaThe`\tNVARCHAR(30) NOT NULL,\n" +
                    "\t`ChuThe`\tNVARCHAR(255),\n" +
                    "\t`ThanhTien`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TyGiaNgoaiTe`\tNUMERIC(19,2),\n" +
                    "\t`ThanhTienQuiDoi`\tNUMERIC(19,2) NOT NULL,\n" +
                    "\t`TLFee`\tNUMERIC(19,2),\n" +
                    "\t`DaVanChuyen`\tINTEGER,\n" +
                    "\t`STT`\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(HoaDonID,MaHinhThuc,STT)\n" +
                    ");";
            database.execSQL(sqlCreateThanhToanHoaDon);
        }
    }
    public void BeginTransaction() {
        database.beginTransaction();
    }
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
        Log.d(LOCATION, "TransactionSuccessful");
    }
    public void endTransaction() {
        database.endTransaction();
    }
    public SQLController open() throws SQLException {
        dbHelper = new DbHelper(myContext);
        dbHelper.openDataBase();
        database  = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    //get data
//    public Cursor  getAllDataFromTable(String in_tableName) {
//        Cursor cursor = database.rawQuery("Select * from " + in_tableName, null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }
    //Function
    public int getCountForSTT(String in_HoaDonID, String tableName) {
        Cursor get = database.rawQuery("Select * from " + tableName + " Where HoaDonID = '" + in_HoaDonID + "';", null);
        if(get != null)
            return get.getCount();
        else return 0;
    }
    //HangHoa
    public ArrayList<HangHoa> getDSHangHoa() {
        ArrayList<HangHoa> result = new ArrayList<HangHoa>();
        Cursor get = database.rawQuery("Select * from HangHoa", null);
        if(get != null) {
            get.moveToFirst();
            while (!get.isAfterLast()) {
                HangHoa tmp = new HangHoa();
                tmp.setHangHoaId(get.getString(0));
                tmp.setMaHH(get.getString(1));
                tmp.setTenHH(get.getString(2));
                tmp.setMaKeHang(get.getString(3));
                tmp.setMaNCC(get.getString(4));
                tmp.setMaNganh(get.getString(5));
                tmp.setMaNhom(get.getString(6));
                tmp.setMaPhanNhom(get.getString(7));
//                tmp.setMaPLU();
                tmp.setVATDauRa(get.getDouble(9));
                tmp.setTienBan(get.getDouble(10));
//                tmp.setMaNVCapNhat(get.getString(8));
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    date = format.parse(get.getString(11));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                tmp.setNgayCapNhat(date);
//                get.get
//                tmp.setDonViTinh(get.getString(10));
//                tmp.setHienThiTrongDanhMuc(get.getInt(11));
//                tmp.setDaDongBo(Boolean.valueOf(get.getString(12)));
//                Log.d(LOCATION, "Tmp HangHoaID " + tmp.getHangHoaID());
//                Log.d(LOCATION, "Tmp MaHH: " + tmp.getMaHH());
//                Log.d(LOCATION, "Tmp TenHH: " + tmp.getTenHH());
                result.add(tmp);
                get.moveToNext();
            }
        }

        return result;
    }
    public boolean checkHangHoaHasMaHH(String in_MaHH) {
        Cursor get = database.rawQuery("select TenHH from HangHoa Where HangHoaID = '" + in_MaHH + "';", null);
        return get != null;
    }
    public HangHoa getHangHoaHasMaHH(String in_MaHH) {
        HangHoa result = new HangHoa();
        Cursor getHangHoa = database.rawQuery("Select * from HangHoa where HangHoaID = '" + in_MaHH + "';", null);
        if(getHangHoa != null)
        {
            getHangHoa.moveToFirst();
            result.setHangHoaId(getHangHoa.getString(0));
            result.setTenHH(getHangHoa.getString(1));
            result.setMaHH(getHangHoa.getString(2));
            result.setMaKeHang(getHangHoa.getString(3));
            result.setMaNCC(getHangHoa.getString(4));
            result.setMaNganh(getHangHoa.getString(5));
            result.setMaNhom(getHangHoa.getString(6));
            result.setMaPhanNhom(getHangHoa.getString(7));
//                tmp.setMaPLU();
            result.setVATDauRa(getHangHoa.getDouble(9));
            result.setTienBan(getHangHoa.getDouble(10));
//            result.set

            return result;
        } else return null;
    }
    public ArrayList<String> getAllHangHoaId() {
        ArrayList<String> result = new ArrayList<String>();
        Cursor get = database.rawQuery("select * from HangHoa", null);
        if(!get.moveToFirst())
        {
//            Log.d(LOCATION, "getALLHangHoaID: Không có hàng hóa");
            return null;
        } else {
            while (!get.isAfterLast()) {
                result.add(get.getString(0));
                get.moveToNext();
            }
            return result;
        }
    }
    public void InserHangHoaToCTHoaDon(CTHoaDon ctHoaDon, String tableName) {
        PublicFunction publicFunction = new PublicFunction();
        String ngayGioQuet = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS", ctHoaDon.getNgayGioQuet());
        String dateHanSuDung = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS", ctHoaDon.getHanSuDung());
//        Log.d(LOCATION, "ngayGioQuet " + ngayGioQuet);
        String query = "INSERT INTO [" + tableName + "] " +
                " ([HoaDonID]" + " ,[HangHoaID] " + " ,[MaHH] " + " ,[STT] " + " ,[LoaiHangHoaBan] " +
                " ,[TenHH] " + " ,[NgayGioQuet] " + " ,[MaNccDM] " + " ,[SoLuong] " + " ,[DGBan] " +
                " ,[TriGiaBan] " + " ,[TLCKGiamGia] " + " ,[TienGiamGia] " + " ,[TriGiaSauGiamGia] " +
                " ,[TLCK_GioVang] " + " ,[TienCK_GioVang] " + " ,[TriGiaSauGioVang] " + " ,[TLCKHD] " +
                " ,[TienCKHD] " + " ,[TriGiaSauCKHD] " + " ,[TLCK_TheGiamGia] " + " ,[TienCK_TheGiamGia] " +
                " ,[ThanhTienBan] " + " ,[TienPhuThu] " + " ,[VATDauRa] " + " ,[DonGiaVonBQ] " +
                " ,[TriGiaVonBQ] " + " ,[DonGiaBQ] " + " ,[TriGiaBQ] " + " ,[DaGhiHDTC] " + " ,[DaVanChuyen] " +
                " ,[TriGiaVATDauRa] " + " ,[LaHangKhuyenMai] " + " ,[DaSuDung] " + " ,[HanSuDung] " + " ,[Serial]) " +
                " VALUES " +
                " ( '" + ctHoaDon.getHoaDonID() +
                "','" + ctHoaDon.getHangHoaID() +
                "' ,'" + ctHoaDon.getMaHH() +
                "' ,'" + ctHoaDon.getSTT() +
                "' ,'" + ctHoaDon.getLoaiHangHoaBan() +
                "' ,'" + ctHoaDon.getTenHH() +
                "' ,'" + ngayGioQuet +
                "' ,'" + ctHoaDon.getMaNccDM() +
                "' ,'" + ctHoaDon.getSoLuong() +
                "' ,'" + String.valueOf(ctHoaDon.getDGBan()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaBan()) +
                "' ,'" + String.valueOf(ctHoaDon.getTLCKGiamGia()) +
                "' ,'" + String.valueOf(ctHoaDon.getTienGiamGia()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaSauGiamGia()) +
                "' ,'" + String.valueOf(ctHoaDon.getTLCK_GioVang()) +
                "' ,'" + String.valueOf(ctHoaDon.getTienCK_GioVang()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaSauGioVang()) +
                "' ,'" + String.valueOf(ctHoaDon.getTLCKHD()) +
                "' ,'" + String.valueOf(ctHoaDon.getTienCKHD()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaSauCKHD()) +
                "' ,'" + String.valueOf(ctHoaDon.getTLCK_TheGiamGia()) +
                "' ,'" + String.valueOf(ctHoaDon.getTienCK_TheGiamGia()) +
                "' ,'" + String.valueOf(ctHoaDon.getThanhTienBan()) +
                "' ,'" + String.valueOf(ctHoaDon.getTienPhuThu()) +
                "' ,'" + String.valueOf(ctHoaDon.getVATDauRa()) +
                "' ,'" + String.valueOf(ctHoaDon.getDonGiaVonBQ()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaVonBQ()) +
                "' ,'" + String.valueOf(ctHoaDon.getDonGiaBQ()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaBQ()) +
                "' ,'" + String.valueOf(ctHoaDon.getDaGhiHDTC()) +
                "' ,'" + String.valueOf(ctHoaDon.getDaVanChuyen()) +
                "' ,'" + String.valueOf(ctHoaDon.getTriGiaVATDauRa()) +
                "' ,'" + String.valueOf(ctHoaDon.getLaHangKhuyenMai()) +
                "' ,'" + String.valueOf(ctHoaDon.getDaSuDung()) +
                "' ,'" + dateHanSuDung +
                "' ,'" + ctHoaDon.getSerial()  + "' )";
                database.execSQL(query);
    }
    public void InsertHangHoa(ArrayList<HangHoa> in_arr) {
        String query = "INSERT INTO [HangHoa] " +
                "([HangHoaID], [MaHH], [TenHH], [MaKeHang], [MaNCC], " +
                "[MaNganh], [MaNhom], [MaPhanNhom], [VATDauRa], [NgayCapNhat], " +
                "[GiaVon], [HangCanKy], [MaTam], [DaDongBo] ) VALUES ";
        if(in_arr.isEmpty()) {
            //
            Log.d(LOCATION,"InsertHangHoa: Không có hàng hóa để insert");
        } else {
            for(int i=0;i<in_arr.size();i++) {
                PublicFunction publicFunction = new PublicFunction();
                String dateFormat = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",in_arr.get(i).getNgayCapNhat());
                if(i == in_arr.size()- 1) {
                    query += "( '" + in_arr.get(i).getHangHoaID() +
                            "', '" + in_arr.get(i).getMaHH() +
                            "', '" + in_arr.get(i).getTenHH() +
                            "', '" + in_arr.get(i).getMaKeHang() +
                            "', '" + in_arr.get(i).getMaNCC() +
                            "', '" + in_arr.get(i).getMaNganh() +
                            "', '" + in_arr.get(i).getMaNhom() +
                            "', '" + in_arr.get(i).getMaPhanNhom() +
                            "', '" + in_arr.get(i).getVATDauRa() +
                            "', '" + dateFormat +
                            "', '" + in_arr.get(i).getTienBan() +
                            "', '" + in_arr.get(i).getHangCanKy() +
                            "', '" + in_arr.get(i).getMaTam() +
                            "', '" + in_arr.get(i).getDaDongBo() +
                            "'); ";
                } else {
                    query += "( '" + in_arr.get(i).getHangHoaID() +
                            "', '" + in_arr.get(i).getMaHH() +
                            "', '" + in_arr.get(i).getTenHH() +
                            "', '" + in_arr.get(i).getMaKeHang() +
                            "', '" + in_arr.get(i).getMaNCC() +
                            "', '" + in_arr.get(i).getMaNganh() +
                            "', '" + in_arr.get(i).getMaNhom() +
                            "', '" + in_arr.get(i).getMaPhanNhom() +
                            "', '" + in_arr.get(i).getVATDauRa() +
                            "', '" + dateFormat +
                            "', '" + in_arr.get(i).getTienBan() +
                            "', '" + in_arr.get(i).getHangCanKy() +
                            "', '" + in_arr.get(i).getMaTam() +
                            "', '" + in_arr.get(i).getDaDongBo() +
                            "'), ";
                }
            }
            Log.d(LOCATION,"Query insert: " + query);
            database.execSQL(query);
        }
    }
    //HoaDon
    public int getCountHoaDon(String tableName) {
        Cursor getCount = database.rawQuery("Select * from " + tableName + " ", null);
        if(getCount != null)
            return getCount.getCount();
        else return  0;
    }
    public void IntancesHoaDon(HoaDon hoaDon, String tableName) {
        PublicFunction publicFunction = new PublicFunction();
        String ngayHDFormat = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",hoaDon.getNgayHD());
        Log.d(LOCATION,"NgayHDFormat " +  ngayHDFormat);
        String ngayBatDauFormat = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",hoaDon.getNgayBatDau());
        Log.d(LOCATION,"ngayBatDauFormat " +  ngayBatDauFormat);
        String ngayKetThucFormat = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",hoaDon.getNgayKetThuc());
        Log.d(LOCATION,"ngayKetThucFormat " +  ngayKetThucFormat);
        String query = "INSERT INTO ["+tableName+"]" +
                " ([HoaDonID]" + " ,[MaHD]" + " ,[STT]" + " ,[MaNV]" + " ,[MaQuay]" +
                " ,[NgayHD]" + " ,[NgayBatDau]" + " ,[GioBatDau]" + " ,[NgayKetThuc]" + " ,[GioKetThuc]" +
                " ,[CaBan]" + " ,[DaIn]" + " ,[MaHDGoc]" + " ,[MaTheKHTT]" + " ,[TriGiaBan]" +
                " ,[TienCK]" + " ,[ThanhTienBan]" + " ,[TienPhuThu]" + " ,[LoaiHoaDon]" + " ,[TienTraKhach]" +
                " ,[DaCapNhatTon]" + " ,[DaVanChuyen]" + " ,[TrungTamTMThuPhi])" +
                "     VALUES" +
                " ( '" + hoaDon.getHoaDonID() +
                "' ,'" + hoaDon.getMaHD() +
                "' ,'" + hoaDon.getSTT() +
                "' ,'" + hoaDon.getMaNV() +
                "' ,'" + hoaDon.getMaQuay() +
                "' ,'" + ngayHDFormat +
                "' ,'" + ngayBatDauFormat +
                "' ,'" + hoaDon.getGioBatDau() +
                "' ,'" + ngayKetThucFormat +
                "' ,'" + hoaDon.getGioKetThuc() +
                "' ,'" + hoaDon.getCaBan() +
                "' ,'" + String.valueOf(hoaDon.getDaIn()) +
                "' ,'" + hoaDon.getMaHDGoc() +
                "' ,'" + hoaDon.getMaTheKHTT() +
                "' ,'" + hoaDon.getTriGiaBan() +
                "' ,'" + hoaDon.getTienCK() +
                "' ,'" + hoaDon.getThanhTienBan() +
                "' ,'" + hoaDon.getTienPhuThu() +
                "' ,'" + hoaDon.getLoaiHoaDon() +
                "' ,'" + hoaDon.getTienTraKhach() +
                "' ,'" + hoaDon.getDaCapNhatTon() +
                "' ,'" + hoaDon.getDaVanChuyen() +
                "' ,'" + hoaDon.getTrungTamTMThuPhi() + "' )";

        database.execSQL(query);
    }
    public void updateHoaDon(String HoaDonID, String gioKetThuc, String tableName) {
        String sql = "update " + tableName +" \n"
                + "SET GioKetThuc = '" + gioKetThuc + "' \n"
                + ", DaIn = '1' " +" \n"
                + "WHERE HoaDonID = '" + HoaDonID + "';";
        database.execSQL(sql);
    }
    public void updateDataHadSync(String HoaDonID, String tableName) {
        Log.d(LOCATION,tableName);
        String sql = "update " + tableName +" \n"
                + " SET DaVanChuyen = '1' " +" \n"
                + "WHERE HoaDonID = '" + HoaDonID + "'";
        database.execSQL(sql);
    }
    public void updateCTHoaDonHadSync(CTHoaDon in_cthoaDon, String tableName) {
        Log.d(LOCATION, tableName);
        try {
            String sql = "update " + tableName +" \n"
                    + " SET DaVanChuyen = '1' " +" \n"
                    + "WHERE HoaDonID = '" + in_cthoaDon.getHoaDonID() + "' AND  " +
                    "STT = '" + in_cthoaDon.getSTT() + "' AND " +
                    "HangHoaID = '" + in_cthoaDon.getHangHoaID() + "'";
            database.execSQL(sql);
        } catch ( SQLiteException ex) {
            Log.d(LOCATION + "updateCTHoaDonHadSync", ex.getMessage());
        }

    }
    public ArrayList<HoaDon> getHoaDonForSyncUp(String tableName) {
        ArrayList<HoaDon> result = new ArrayList<HoaDon>();
        String sql = "SELECT * FROM " + tableName + " WHERE DaVanChuyen = '0'";
        Cursor get = database.rawQuery(sql,null);
        if( get != null) {
            get.moveToFirst();
            while(!get.isAfterLast()) {
                HoaDon temp = new HoaDon(get);
                Log.d(LOCATION,"GetHoaDonForSyncUp");
                result.add(temp);
                get.moveToNext();
            }
        }
        return result;
    }
    //CTHoaDon
    public ArrayList<CTHoaDon> getCTHoaDonForSyncUp(String tableName) {
    ArrayList<CTHoaDon> result = new ArrayList<CTHoaDon>();
    String sql = "SELECT * FROM " + tableName + " WHERE DaVanChuyen = '0'";
    Cursor get = database.rawQuery(sql,null);
    if( get != null) {
        get.moveToFirst();
        while(!get.isAfterLast()) {
            CTHoaDon temp = new CTHoaDon(get);
            result.add(temp);
            get.moveToNext();
        }
    }
    return result;

}

    //ThanhToanHoaDon
    public ArrayList<ThanhToanHoaDon> getThanhToanHoaDonForSyncUp(String tableName) {
        ArrayList<ThanhToanHoaDon> result = new ArrayList<ThanhToanHoaDon>();
        String sql = "SELECT * FROM " + tableName + " WHERE DaVanChuyen = '0'";
        Cursor get = database.rawQuery(sql,null);
        if( get != null) {
            get.moveToFirst();
            while(!get.isAfterLast()) {
                ThanhToanHoaDon temp = new ThanhToanHoaDon(get);
                result.add(temp);
                get.moveToNext();
            }
        }
        return result;

    }
    public void InsertThanhToanHoaDon(String tableName, ThanhToanHoaDon thanhToanHoaDon) {
        String query = "INSERT INTO [" + tableName + "] \n" +
                "           ([HoaDonID]\n" +
                "           ,[MaHinhThuc]\n" +
                "           ,[MaNhomThanhToan]\n" +
                "           ,[MaThe]\n" +
                "           ,[ChuThe]\n" +
                "           ,[ThanhTien]\n" +
                "           ,[TyGiaNgoaiTe]\n" +
                "           ,[ThanhTienQuiDoi]\n" +
                "           ,[TLFee]\n" +
                "           ,[DaVanChuyen]\n" +
                "           ,[STT])\n" +
                "     VALUES\n" +
                " ( '" + thanhToanHoaDon.getHoaDonID() +
                "','" + thanhToanHoaDon.getMaHinhThuc() +
                "','" + thanhToanHoaDon.getMaNhomThanhToan() +
                "','" + thanhToanHoaDon.getMathe() +
                "','" + thanhToanHoaDon.getChuThe() +
                "','" + thanhToanHoaDon.getThanhTien() +
                "','" + thanhToanHoaDon.getTyGiaNgoaiTe() +
                "','" + thanhToanHoaDon.getThanhTienQuyDoi() +
                "','" + thanhToanHoaDon.getTLFee() +
                "','" + thanhToanHoaDon.getDaVanChuyen() +
                "','" + thanhToanHoaDon.getSTT() + "' )";
        database.execSQL(query);
    }
    public double GetThanhTienInCTHoaDon(String HoaDonID,String tableName) {
        double result = 0;
//        String cthoadonName = "CTHoaDon" + timer.getStrMonth() + timer.getYear();
        Cursor get = database.rawQuery("select ThanhTienBan from " + tableName + " Where HoaDonID = '" + HoaDonID + "';" , null);
        if(get != null ) {
            get.moveToFirst();
            while(!get.isAfterLast()) {
                result += get.getDouble(0);
                get.moveToNext();
            }
        } else {
            Log.d(LOCATION,"Lỗi khi insert CTHoaDon");
        }
        return result;
    }
    //table HinhThucThanhToan
    public void deleteHinhThucThanhToan() {
        database.execSQL("delete from HinhThucThanhToan");
    }
    public void InsertHinhThucThanhToan(ArrayList<HinhThucThanhToan> arr_HinhThucThanhToan) {
        String query = "INSERT INTO [HinhThucThanhToan]" + " ([MaHinhThuc] " + " ,[STT] " + " ,[MaNhomThanhToan] " +
                " ,[TenHinhThuc] " + " ,[MoTa] " + " ,[MaThe] " + " ,[MaNguyenTe] " + " ,[TLFee] " + " ,[TrangThai] " +
                " ,[MaNVTao] " + " ,[NgayTao] " + " ,[MaNVCapNhat] " + " ,[NgayCapNhat] " +
                " ,[DoUuTien] " + " ,[MaChu]) " + " VALUES ";
        if(arr_HinhThucThanhToan.isEmpty()) {
            Log.d(LOCATION, "InsertHangHoa: Không có hàng hóa để insert");
        } else {
            for(int i=0;i<arr_HinhThucThanhToan.size();i++) {
                PublicFunction publicFunction = new PublicFunction();
                String dateNgayCapNhat = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",arr_HinhThucThanhToan.get(i).getNgayCapNhat());
                String dateNgayTao = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",arr_HinhThucThanhToan.get(i).getNgayTao());
                if(i == arr_HinhThucThanhToan.size()- 1) {
                    query += "( '" + arr_HinhThucThanhToan.get(i).getMaHinhThuc() +
                            "', '" + arr_HinhThucThanhToan.get(i).getSTT() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNhomThanhToan() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTenHinhThuc() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMoTa() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaThe() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNguyenTe() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTLFee() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTrangThai() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNVTao() +
                            "', '" + dateNgayTao +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNVCapNhat() +
                            "', '" + dateNgayCapNhat +
                            "', '" + arr_HinhThucThanhToan.get(i).getDoUuTien() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaChu() +
                            "'); ";
                } else {
                    query += "( '" + arr_HinhThucThanhToan.get(i).getMaHinhThuc() +
                            "', '" + arr_HinhThucThanhToan.get(i).getSTT() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNhomThanhToan() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTenHinhThuc() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMoTa() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaThe() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNguyenTe() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTLFee() +
                            "', '" + arr_HinhThucThanhToan.get(i).getTrangThai() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNVTao() +
                            "', '" + dateNgayTao +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaNVCapNhat() +
                            "', '" + dateNgayCapNhat +
                            "', '" + arr_HinhThucThanhToan.get(i).getDoUuTien() +
                            "', '" + arr_HinhThucThanhToan.get(i).getMaChu() +
                            "'), ";
                }
            }
//            Log.d(LOCATION,"Query insert: " + query);
            database.execSQL(query);
        }
    }
    public ArrayList<HinhThucThanhToan> getHinhThucThanhToan() {
        ArrayList<HinhThucThanhToan> result = new ArrayList<HinhThucThanhToan>();
        Cursor get = database.rawQuery("Select * From HinhThucThanhToan Where TrangThai = '1' ;", null);
        if(get != null) {
            get.moveToFirst();
            while(!get.isAfterLast()) {
                HinhThucThanhToan temp = new HinhThucThanhToan(get);
                result.add(temp);
                get.moveToNext();
            }
        }
        return result;
    }
    public String getTenHinhThuc(String maHinhThuc) {
        String sql = "SELECT TenHinhThuc FROM HinhThucThanhToan WHERE MaHinhThuc = '" + maHinhThuc + "';";
        Cursor get = database.rawQuery(sql,null);
        if(get != null)
        {
            get.moveToFirst();
            return get.getString(0);
        } else {
            return  "NULL";
        }
    }
    public byte[] getHinhAnh(String HangHoaID) {
        byte[] result = null ;
        Cursor get = database.rawQuery("Select * FROM HangHoa_HinhAnh WHERE HangHoaID = '" + HangHoaID + "';", null);
        Log.d(LOCATION,"Select * FROM HangHoa_HinhAnh WHERE HangHoaID = '" + HangHoaID + "';" );
        if(get != null) {
            if(get.moveToFirst()) {
                String image = get.getString(0);
                Log.d(LOCATION,image);
                result =  get.getBlob(1);
            }
        }
        return  result;
    }

    public void addHangHoaHinhAnh(ArrayList<HangHoa_HinhAnh> in) {
        for(int i=0;i<in.size();i++) {
            Log.d(LOCATION, " - AddHangHoaHinhAnh: " + i);
            ContentValues values = new ContentValues();
            values.put("HangHoaID",in.get(i).getHangHoaID());
            values.put("HinhAnh",in.get(i).getHinhAnh());

            database.insert("HangHoa_HinhAnh", null, values);
        }
    }
    public void deleteHangHoa_HinhAnh() {
        database.execSQL("Delete FROM HangHoa_HinhAnh");
    }

//    /table config
//table config
public boolean checkIP() {
    Cursor check = database.rawQuery("SELECT * FROM Config",null);
    if(check != null) {
        return check.moveToFirst();
    } else return false;
}
    public void insertIP() {
//        database.delete()
        String query = "INSERT INTO Config(IP, KV, CH) VALUES ('192.168.200.31/MyService','KV001','AA');";
        database.execSQL(query);
    }
    public Cursor getIP() {
        Cursor get = database.rawQuery("SELECT * FROM Config",null);
        get.moveToFirst();
        return  get;
    }
    public String getStrIP() {
        Cursor get = database.rawQuery("SELECT IP FROM Config", null);
        get.moveToFirst();
        return get.getString(0);
    }
    public void updateIP(String IP) {
        String query = "UPDATE Config SET IP = '" + IP +"';";
        database.execSQL(query);
    }
    //table Bluetooth
    public boolean checkBluetooth(String name) {
        Cursor check = database.rawQuery("SELECT * FROM Bluetooth WHERE Name = '" + name +"'", null);
        return check != null && check.moveToFirst();
    }
    public void insertBluetooth(BluetoothDevice device) {
        database.execSQL("INSERT INTO Bluetooth (Name, Address, Type) VALUES ('" + device.getName() +"'" +
                ", '" + device.getAddress() + "', '1');");
    }
    public String getAddressBlue() {
         Cursor get = database.rawQuery("SELECT Address FROM Bluetooth WHERE Type = '1'", null);
        if( get != null) {
            if(get.moveToFirst()) return get.getString(0);
            else return "";
        } else  return "";
    }
    public void updateAllType() {
        database.execSQL("UPDATE Bluetooth SET Type = '0'") ;
    }
    public void updateDevice(String name) {
        database.execSQL("UPDATE Bluetooth SET Type = '1' WHERE Name = '" + name + "';");
    }
}
