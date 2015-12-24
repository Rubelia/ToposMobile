package com.example.laptrinhmobile.toposmobile.Other;

/**
 * Created by LapTrinhMobile on 10/9/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
//import android.util.Log;

import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;

import java.io.IOException;
import java.util.ArrayList;
import com.example.laptrinhmobile.toposmobile.Object.Timer;

public class SharedPreference {

    private static final String LOCATION = "SharedPreference";
    private static final String PREFS_NAME = "AOP_PREFS";
    private static final String PREFS_KEY = "AOP_PREFS_String";
//    public static final String TYPE_GIAODICH = "TYPE_GIAODICH_String";
    private static final String HANGHOAID = "HangHoaID";
    private static final String MAHH ="MaHH";
    private static final String TENHH = "TenHH";
    private static final String QUANTITY = "Quantity";
    private static final String POSITION = "Position";
    private static final String VATDAURA = "VATDauRa";
    private static final String GIABAN = "GiaBan";

    private static final String DAY ="Day";
    private static final String MONTH = "Month";
    private static final String YEAR = "Year";
    private static final String HOUR = "Hour";
    private static final String MINUTES = "Minutes";
    private static final String SECONDS = "Seconds";

    private static final String isAddProduct = "isAddProduct";
    private static final String isChoosedPayment = "isChoosedPayment";
    ///typePayment
    private static final String TYPEPAYMENT = "TypePayment";
    private static final String AMOUNT ="Amount";
    //ThanhToanHoaDon
    private static final String HoaDonID ="HoaDonID";
    private static final String MaHinhThuc = "MaHinhThuc";
    private static final String MaNhomThanhToan = "MaNhomThanhToan";
    private static final String MaThe = "MaThe";
    private static final String ChuThe = "ChuThe";
    private static final String ThanhTien = "ThanhTien";
    private static final String TyGiaNgoaiTe = "TyGiaNgoaiTe";
    private static final String ThanhTienQuyDoi = "ThanhTienQuyDoi";
    private static final String TLFee = "TLFee";
    private static final String DaVanChuyen = "DaVanChuyen";
    private static final String STT = "STT";

    public SharedPreference() {
        super();
    }

    public void saveAmount(Context context, String in_amount ) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(AMOUNT, in_amount);
        editor.commit();
    }
    public void saveHangHoa(Context context, HangHoa in_HangHoa) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(HANGHOAID, ""+ in_HangHoa.getHangHoaID());
        editor.putString(MAHH, in_HangHoa.getMaHH());
        editor.putString(TENHH, in_HangHoa.getTenHH());
        editor.putInt(QUANTITY, in_HangHoa.getQuantity());
        editor.putInt(POSITION, in_HangHoa.getPosition());
        editor.putLong(VATDAURA, (long) in_HangHoa.getVATDauRa());
        editor.putLong(GIABAN, (long) in_HangHoa.getTienBan());

        editor.commit();
    }

    public void saveArrHangHoa(Context context, ArrayList<HangHoa> arrHangHoa) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND); //1
        editor= settings.edit();
        try {
            editor.putString("HangHoaList", ObjectSerializer.serialize(arrHangHoa));
        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.commit();
    }

    public void saveTypePayment(Context context, TypePayment typePayment) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

//        editor.putString(PREFS_KEY, text); //3

        editor.putString(TYPEPAYMENT, typePayment.getType());
        editor.putString(AMOUNT, typePayment.getAmount() + "");
//        Log.d(LOCATION,"saveTypePayment: Amount - " + typePayment.getAmount());
        editor.putInt(POSITION,typePayment.getPosition());

        editor.commit(); //4
    }

    public void saveThanhToanHoaDon(Context context, ThanhToanHoaDon thanhToanHoaDon) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(HoaDonID, thanhToanHoaDon.getHoaDonID());
        editor.putString(MaHinhThuc, thanhToanHoaDon.getMaHinhThuc());
        editor.putString(MaNhomThanhToan, thanhToanHoaDon.getMaNhomThanhToan());
        editor.putString(MaThe, thanhToanHoaDon.getMathe());
        editor.putString(ChuThe, thanhToanHoaDon.getChuThe());
        editor.putString(ThanhTien, thanhToanHoaDon.getThanhTien() + "");
        editor.putString(TyGiaNgoaiTe, thanhToanHoaDon.getTyGiaNgoaiTe()+"");
        editor.putString(ThanhTienQuyDoi, thanhToanHoaDon.getThanhTienQuyDoi()+"");
        editor.putString(TLFee, thanhToanHoaDon.getTLFee()+"");
        editor.putString(DaVanChuyen, thanhToanHoaDon.getDaVanChuyen()+"");
        editor.putString(STT, thanhToanHoaDon.getSTT() + "");     //3

        editor.commit(); //4
    }

    public void saveTimer(Context context, Timer timer) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        //3
        editor.putInt(DAY, timer.getDay());
        editor.putInt(MONTH, timer.getMonth());
        editor.putInt(YEAR, timer.getYear());
        editor.putInt(HOUR, timer.getHour());
        editor.putInt(MINUTES, timer.getMinutes());
        editor.putInt(SECONDS, timer.getSeconds());

        editor.commit(); //4
    }
    public void save(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(PREFS_KEY, text); //3

        editor.commit(); //4
    }

    public void FirstAddPro(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(isAddProduct, true);

        editor.commit();
    }

    public void saveIsChoosePayment(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(isChoosedPayment,true);

        editor.commit();

    }

    public double getAmount(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return Double.valueOf(settings.getString(AMOUNT, "0.00"));
    }
    public boolean getisChoosedPayment(Context context) {
        boolean result = false;
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        result = settings.getBoolean(isChoosedPayment, false);

        return  result;
    }
    public boolean getisAddPro(Context context) {
        boolean result = false;
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        result = settings.getBoolean(isAddProduct,false);

        return  result;
    }
    public HangHoa getHangHoa(Context context) {
        HangHoa resutl = new HangHoa();
        SharedPreferences settings;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        resutl.setHangHoaId(settings.getString(HANGHOAID, ""));
        resutl.setMaHH(settings.getString(MAHH, ""));
        resutl.setTenHH(settings.getString(TENHH, ""));
        resutl.setQuantity(settings.getInt(QUANTITY, 1));
        resutl.setPosition(settings.getInt(POSITION, -1));
        resutl.setVATDauRa((double) settings.getLong(VATDAURA, 0));
        resutl.setTienBan((double) settings.getLong(GIABAN, 0));
        return resutl;
    }

    public Timer getTimer(Context context) {
        Timer resutl = new Timer();
        SharedPreferences settings;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        resutl.setDay(settings.getInt(DAY, 1));
        resutl.setMonth(settings.getInt(MONTH, 1));
        resutl.setYear(settings.getInt(YEAR, 1992));
        resutl.setHour(settings.getInt(HOUR, 0));
        resutl.setMinutes(settings.getInt(MINUTES, 0));
        resutl.setSeconds(settings.getInt(SECONDS, 0));
        return resutl;
    }

    public ThanhToanHoaDon getThanhToanHoaDon(Context context) {
        SharedPreferences settings;
        ThanhToanHoaDon result = new ThanhToanHoaDon();
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        result.setHoaDonID(settings.getString(HoaDonID, "NULL"));
        result.setMaHinhThuc(settings.getString(MaHinhThuc, "0"));
        result.setMaNhomThanhToan(settings.getString(MaNhomThanhToan, "0"));
        result.setMathe(settings.getString(MaThe, "0"));
        result.setChuThe(settings.getString(ChuThe, "NULL"));
        result.setThanhTien(Double.valueOf(settings.getString(ThanhTien, "0.00")));
        result.setTyGiaNgoaiTe(Double.valueOf(settings.getString(TyGiaNgoaiTe, "0.00")));
        result.setThanhTienQuyDoi(Double.valueOf(settings.getString(ThanhTienQuyDoi, "0.00")));
        result.setDaVanChuyen(Integer.valueOf(settings.getString(DaVanChuyen, "0")));
        result.setSTT(Integer.valueOf(settings.getString(STT,"0")));

        return result;
    }
    public TypePayment getTypePayment(Context context) {
        TypePayment result = new TypePayment();
        SharedPreferences settings;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        result.setType(settings.getString(TYPEPAYMENT, "Cash"));
//        Log.d(LOCATION,"Amount: " + settings.getLong(AMOUNT,0));
        result.setAmount((Double.valueOf(settings.getString(AMOUNT, "0"))));
        result.setPosition(settings.getInt(POSITION, -1));

        return result;

    }

    public String getValue(Context context) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY, null);
        return text;
    }

    public ArrayList<HangHoa> getArrHangHoa(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        ArrayList<HangHoa> result = new ArrayList<HangHoa>();
        try {
            result = (ArrayList) ObjectSerializer.deserialize(prefs.getString("HangHoaList", ObjectSerializer.serialize(new ArrayList<HangHoa>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  result;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }


}