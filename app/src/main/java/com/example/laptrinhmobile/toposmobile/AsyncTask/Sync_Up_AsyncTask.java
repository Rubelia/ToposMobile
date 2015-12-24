package com.example.laptrinhmobile.toposmobile.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.laptrinhmobile.toposmobile.Object.CTHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HoaDon;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.Timer;
import com.example.laptrinhmobile.toposmobile.Other.PublicFunction;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by LapTrinhMobile on 10/23/2015.
 */
public class Sync_Up_AsyncTask extends AsyncTask<String, String, String> {

    private static String LOCATION = "Sync_Up_AsyncTask";
    private static ProgressDialog pDialog;
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    public String IP ;
    private ArrayList<HoaDon> arr_HoaDon = new ArrayList<HoaDon>();
    private ArrayList<CTHoaDon> arr_CTHoaDon = new ArrayList<CTHoaDon>();
    private ArrayList<ThanhToanHoaDon> arr_ThanhToanHoaDon = new ArrayList<ThanhToanHoaDon>();
    double numberToPub ;
    SQLController sqlController;
    Activity contextCha;
    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;

    public Sync_Up_AsyncTask (Activity ctx, String inputIP) {
        contextCha = ctx;
        this.IP = inputIP;
        pDialog = new ProgressDialog(ctx);
    }
    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();
        sqlController = new SQLController(contextCha);
//        sqlController.open();
        arr_HoaDon = new ArrayList<HoaDon>();
        arr_CTHoaDon = new ArrayList<CTHoaDon>();
        arr_ThanhToanHoaDon = new ArrayList<ThanhToanHoaDon>();
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setTitle("Đang đồng bộ dữ liệu lên Server");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected String doInBackground(String...params) {

//        IP = "127.0.0.1";
        Timer timer = new Timer();
        String tableNameHoaDon = "HoaDon" + timer.getStrMonth() + timer.getYear();
        String tableNameCTHoaDon = "CTHoaDon" + timer.getStrMonth() + timer.getYear();
        String tableNameThanhToanHoaDon = "ThanhToanHoaDon" + timer.getStrMonth() + timer.getYear();

        sqlController.open();
        sqlController.BeginTransaction();
        pDialog.setMessage("Bảng Hóa đơn: " + tableNameHoaDon);
        upHoaDon(tableNameHoaDon);

        pDialog.setMessage("Bảng CTHoaDon: " + tableNameCTHoaDon);
        upCTHoaDon(tableNameCTHoaDon);

        pDialog.setMessage("Bảng Thanh toán hóa đơn: " + tableNameThanhToanHoaDon);
        upThanhToanHoaDon(tableNameThanhToanHoaDon);

        sqlController.setTransactionSuccessful();
        sqlController.endTransaction();
        sqlController.close();

        return null;
    }

    /**
     * ta cÃƒÂ¡Ã‚ÂºÃ‚Â­p nhÃƒÂ¡Ã‚ÂºÃ‚Â­p giao diÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n trong hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y
     */
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub

        super.onProgressUpdate(values);
        pDialog.setProgress(Integer.valueOf(values[0]));
    }
    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        pDialog.setMessage("Đồng bộ dữ liệu thành công");

        pDialog.dismiss();
        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }

        SyncHinhThucThanhToan_AsyncTask asyncTask = new SyncHinhThucThanhToan_AsyncTask(contextCha,IP);
        asyncTask.execute();

    }

    private double getNumberToPublishProgress(int max) {
        double result ;
        if(max >= 100) {
            result = max/ 100 ;
        } else {
            result = 100 / max;
        }
//        Log.d(LOCATION, "getNumberToPublishProgress: " + result);
        return result;
    }

    private void upHoaDon(String in_table) {
//        sqlController.open();
        arr_HoaDon = sqlController.getHoaDonForSyncUp(in_table);
//        sqlController.close();
        if(arr_HoaDon.isEmpty())
        {
            Log.d(LOCATION + " - HoaDon", "Không có dữ liệu để đồng bộ");
        } else {
            numberToPub = getNumberToPublishProgress(33);

            int numberEffect = GetResultInsertHoaDon(arr_HoaDon,in_table);

            if(numberEffect == arr_HoaDon.size()) {
//                sqlController.open();
                for(int i=0;i<arr_HoaDon.size();i++) {
                    HoaDon temp = arr_HoaDon.get(i);
                    Log.d(LOCATION + " - HoaDon", "Insert thành công " + temp.getHoaDonID());
                    sqlController.updateDataHadSync(temp.getHoaDonID(), in_table);
                    if(i % numberToPub == 0){
                        publishProgress("" + i);
                    }
                }
//                sqlController.close();
            } else {
                Log.d(LOCATION + " - HoaDon","Đồng bộ không thành công");
            }
        }
    }

    private int GetResultInsertHoaDon(ArrayList<HoaDon> in_arr, String tableName) {
        int result = 0;
        final String NAMESPACE = "http://tempuri.org/";
        //TODO : change METHOD_NAME
        final String METHOD_NAME = "fExecuteNonQuery";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://192.168.200.31:80/MyService/tungSqlServerProxy.asmx";

        //TODO: addProperty for HoaDon
        String script = "INSERT INTO ["+tableName+"]" +
                " ([HoaDonID]" + " ,[MaHD]" + " ,[STT]" + " ,[MaNV]" + " ,[MaQuay]" +
                " ,[NgayHD]" + " ,[NgayBatDau]" + " ,[GioBatDau]" + " ,[NgayKetThuc]" + " ,[GioKetThuc]" +
                " ,[CaBan]" + " ,[DaIn]" + " ,[MaHDGoc]" + " ,[MaTheKHTT]" + " ,[TriGiaBan]" +
                " ,[TienCK]" + " ,[ThanhTienBan]" + " ,[TienPhuThu]" + " ,[LoaiHoaDon]" + " ,[TienTraKhach]" +
                " ,[DaCapNhatTon]" + " ,[DaVanChuyen]" + " ,[TrungTamTMThuPhi])" +
                "     VALUES " ;
//        String temp_sript = "INSERT INTO [HoaDon102015] \n" +
//                "([HoaDonID] ,[MaHD] ,[STT] ,[MaNV] ,[MaQuay] ,[NgayHD] ,[NgayBatDau] ,[GioBatDau] ,[NgayKetThuc] ,[GioKetThuc] ,[CaBan] ,[DaIn] ,[MaHDGoc] ,[MaTheKHTT] ,\n" +
//                "[TriGiaBan] ,[TienCK] ,[ThanhTienBan] ,[TienPhuThu] ,[LoaiHoaDon] ,[TienTraKhach] ,[DaCapNhatTon] ,[DaVanChuyen] ,[TrungTamTMThuPhi])  \n" +
//                "   VALUES ( 'b7e6d28b-59de-4c6b-9557-08445b27bed2', 'AA0101261020150001', '1', 'AA0026', 'AA0101', '2015-10-26 12:07:03.000', '2015-10-26 12:07:03.000', \n" +
//                "   '43623', '2015-10-26 12:07:03.000', '43623', '1', '0', '', 'null', '0.0', '0.0',\n" +
//                " '0.0', '0.0', '0', '0.0', '0', '0', '0'), ( '2f66382b-5993-448f-9250-59d3989f1a9f', 'AA0101261020150011', '1', 'AA0026', 'AA0101', '2015-10-26 14:26:03.000',\n" +
//                "  '2015-10-26 14:26:03.000', '51963', '2015-10-26 14:26:03.000', '51980', '1', '1', '0', 'NULL', '0.0', '0.0', '0.0', '0.0', '0', '0.0', '0', '0', '0'); ";
        for(int i =0;i<in_arr.size();i++) {
            PublicFunction publicFunction = new PublicFunction();
            String dateNgayHD = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",in_arr.get(i).getNgayHD());
//            dateNgayHD = "NULL";
            String dateNgayBatDau = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",in_arr.get(i).getNgayBatDau());
//            dateNgayBatDau = "NULL";
            String dateNgayKetThuc = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",in_arr.get(i).getNgayKetThuc());
//            dateNgayKetThuc = "NULL";
            if(i == in_arr.size()- 1) {
                script += "( '" + in_arr.get(i).getHoaDonID() +
                        "', '" + in_arr.get(i).getMaHD() +
                        "', '" + in_arr.get(i).getSTT() +
                        "', 'AA0026', '" + in_arr.get(i).getMaQuay() +
                        "', '" + dateNgayHD +
                        "', '" + dateNgayBatDau +
                        "', '" + in_arr.get(i).getGioBatDau() +
                        "', '" + dateNgayKetThuc +
                        "', '" + in_arr.get(i).getGioKetThuc() +
                        "', '1', '" + in_arr.get(i).getDaIn() +
                        "', '" + in_arr.get(i).getMaHDGoc() +
                        "', '" + in_arr.get(i).getMaTheKHTT() +
                        "', '" + in_arr.get(i).getTriGiaBan() +
                        "', '" + in_arr.get(i).getTienCK() +
                        "', '" + in_arr.get(i).getThanhTienBan() +
                        "', '" + in_arr.get(i).getTienPhuThu() +
                        "', '" + in_arr.get(i).getLoaiHoaDon() +
                        "', '" + in_arr.get(i).getTienTraKhach() +
                        "', '" + in_arr.get(i).getDaCapNhatTon()+
                        "', '" + in_arr.get(i).getDaVanChuyen() +
                        "', '" + in_arr.get(i).getTrungTamTMThuPhi() +
                        "') ";
            } else {
                script += "( '" + in_arr.get(i).getHoaDonID() +
                        "', '" + in_arr.get(i).getMaHD() +
                        "', '" + in_arr.get(i).getSTT() +
                        "', 'AA0026', '" + in_arr.get(i).getMaQuay() +
                        "', '" + dateNgayHD +
                        "', '" + dateNgayBatDau +
                        "', '" + in_arr.get(i).getGioBatDau() +
                        "', '" + dateNgayKetThuc +
                        "', '" + in_arr.get(i).getGioKetThuc() +
                        "', '1', '" + in_arr.get(i).getDaIn() +
                        "', '" + in_arr.get(i).getMaHDGoc() +
                        "', '" + in_arr.get(i).getMaTheKHTT() +
                        "', '" + in_arr.get(i).getTriGiaBan() +
                        "', '" + in_arr.get(i).getTienCK() +
                        "', '" + in_arr.get(i).getThanhTienBan() +
                        "', '" + in_arr.get(i).getTienPhuThu() +
                        "', '" + in_arr.get(i).getLoaiHoaDon() +
                        "', '" + in_arr.get(i).getTienTraKhach() +
                        "', '" + in_arr.get(i).getDaCapNhatTon()+
                        "', '" + in_arr.get(i).getDaVanChuyen() +
                        "', '" + in_arr.get(i).getTrungTamTMThuPhi() +
                        "'), ";
            }
        }
        Log.d(LOCATION, "SQLQuery " + script);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("query", script);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        MarshalFloat marshal = new MarshalFloat();
        marshal.register(envelope);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.debug = true;
            transport.call(SOAP_ACTION, envelope);

            String xml = transport.responseDump;
            Log.d(LOCATION,"XML file : " + xml);
            try {
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e) {
                list_excep.add(e);
//                Log.d(LOCATION,"XMLPull");
                e.printStackTrace();
            }
            String nodeName;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        //Sucess = 1; fail = -1 ; had = 0;
                        if (nodeName.equalsIgnoreCase("fExecuteNonQueryResult")) {
                            result = Integer.valueOf(parser.nextText());
                        } else if (nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else { break; }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
            list_excep.add(e);
            e.printStackTrace();
        }
        return result;
    }

    private void upCTHoaDon(String in_table) {
//        sqlController.open();
        arr_CTHoaDon = sqlController.getCTHoaDonForSyncUp(in_table);
        if(arr_CTHoaDon.isEmpty()) {
            Log.d(LOCATION + " - CTHoaDon", "Không có dữ liệu cần đồng bộ");
        } else {
            numberToPub = getNumberToPublishProgress(33);

            int numberEffect = GetResultInsertCTHoaDon(arr_CTHoaDon,in_table);

            if(numberEffect == arr_CTHoaDon.size()) {
                for(int i=0;i<arr_CTHoaDon.size();i++) {
                    CTHoaDon temp = arr_CTHoaDon.get(i);
                    Log.d(LOCATION + " - CTHoaDon", "Insert thành công " + temp.getHoaDonID());
                    sqlController.updateCTHoaDonHadSync(temp, in_table);
                    if(i % numberToPub == 0){
                        publishProgress("" + i);
                    }
                }
//                sqlController.close();
            } else {
                Log.d(LOCATION + " - CTHoaDon","Đồng bộ không thành công");
            }
        }
    }

    private int GetResultInsertCTHoaDon(ArrayList<CTHoaDon> in_arr,String in_tableName) {
        int result = 0;
        final String NAMESPACE = "http://tempuri.org/";
        //TODO : change METHOD_NAME
        final String METHOD_NAME = "fExecuteNonQuery";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://192.168.200.31:80/MyService/tungSqlServerProxy.asmx";

        //TODO: addProperty for CTHoaDon
        String script = "INSERT INTO ["  + in_tableName + "]" + " ([HoaDonID] " +
                " ,[HangHoaID] " + " ,[MaHH] " + " ,[STT] " + " ,[LoaiHangHoaBan] " + " ,[TenHH] " +
                " ,[NgayGioQuet] " + " ,[MaNccDM] " + " ,[SoLuong] " + " ,[DGBan] " + " ,[TriGiaBan] " +
                " ,[TLCKGiamGia] " + " ,[TienGiamGia] " + " ,[TriGiaSauGiamGia] " + " ,[TLCK_GioVang] " +
                " ,[TienCK_GioVang] " + " ,[TriGiaSauGioVang] " + " ,[TLCKHD] " + " ,[TienCKHD] " +
                " ,[TriGiaSauCKHD] " + " ,[TLCK_TheGiamGia] " + " ,[TienCK_TheGiamGia] " +
                " ,[ThanhTienBan] " + " ,[TienPhuThu] " + " ,[VATDauRa] " + " ,[DonGiaVonBQ] " +
                " ,[TriGiaVonBQ] " + " ,[DonGiaBQ] " + " ,[TriGiaBQ] " + " ,[DaGhiHDTC] " +
                " ,[DaVanChuyen] " + " ,[TriGiaVATDauRa] " + " ,[LaHangKhuyenMai] " + " ,[DaSuDung]) " +
                " VALUES ";
        for(int i =0;i<in_arr.size();i++) {
            CTHoaDon hoaDon = in_arr.get(i);
            PublicFunction publicFunction = new PublicFunction();
            String dateNgayGioQuet = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",hoaDon.getNgayGioQuet());
            if(i == in_arr.size()- 1) {
                script += "( '" + hoaDon.getHoaDonID()          + "', '" + hoaDon.getHangHoaID() +
                        "', '" + hoaDon.getMaHH()               + "', '" + hoaDon.getSTT() +
                        "', '" + hoaDon.getLoaiHangHoaBan()     + "', '" + hoaDon.getTenHH() +
                        "', '" + dateNgayGioQuet                + "', '" + hoaDon.getMaNccDM() +
                        "', '" + hoaDon.getSoLuong()            + "', '" + hoaDon.getDGBan() +
                        "', '" + hoaDon.getTriGiaBan()          + "', '" + hoaDon.getTLCKGiamGia() +
                        "', '" + hoaDon.getTienGiamGia()        + "', '" + hoaDon.getTriGiaSauGiamGia() +
                        "', '" + hoaDon.getTLCK_GioVang()       +  "', '" + hoaDon.getTienCK_GioVang() +
                        "', '" + hoaDon.getTriGiaSauGioVang()   + "', '" + hoaDon.getTLCKHD() +
                        "', '" + hoaDon.getTienCKHD()           + "', '" + hoaDon.getTriGiaSauCKHD() +
                        "', '" + hoaDon.getTLCK_TheGiamGia()    + "', '" + hoaDon.getTienCK_TheGiamGia() +
                        "', '" + hoaDon.getThanhTienBan()       + "', '" + hoaDon.getTienPhuThu() +
                        "', '" + hoaDon.getVATDauRa()           + "', '" + hoaDon.getDonGiaVonBQ() +
                        "', '" + hoaDon.getTriGiaVonBQ()        + "', '" + hoaDon.getDonGiaBQ() +
                        "', '" + hoaDon.getTriGiaBQ()           + "', '" + hoaDon.getDaGhiHDTC() +
                        "', '" + hoaDon.getDaVanChuyen()        + "', '" + hoaDon.getTriGiaVATDauRa() +
                        "', '" + hoaDon.getLaHangKhuyenMai()    + "', '" + hoaDon.getDaSuDung() +
                        "') ";

            } else {
                script += "( '" + hoaDon.getHoaDonID()          + "', '" + hoaDon.getHangHoaID() +
                        "', '" + hoaDon.getMaHH()               + "', '" + hoaDon.getSTT() +
                        "', '" + hoaDon.getLoaiHangHoaBan()     + "', '" + hoaDon.getTenHH() +
                        "', '" + dateNgayGioQuet                + "', '" + hoaDon.getMaNccDM() +
                        "', '" + hoaDon.getSoLuong()            + "', '" + hoaDon.getDGBan() +
                        "', '" + hoaDon.getTriGiaBan()          + "', '" + hoaDon.getTLCKGiamGia() +
                        "', '" + hoaDon.getTienGiamGia()        + "', '" + hoaDon.getTriGiaSauGiamGia() +
                        "', '" + hoaDon.getTLCK_GioVang()       + "', '" + hoaDon.getTienCK_GioVang() +
                        "', '" + hoaDon.getTriGiaSauGioVang()   + "', '" + hoaDon.getTLCKHD() +
                        "', '" + hoaDon.getTienCKHD()           + "', '" + hoaDon.getTriGiaSauCKHD() +
                        "', '" + hoaDon.getTLCK_TheGiamGia()    + "', '" + hoaDon.getTienCK_TheGiamGia() +
                        "', '" + hoaDon.getThanhTienBan()       + "', '" + hoaDon.getTienPhuThu() +
                        "', '" + hoaDon.getVATDauRa()           + "', '" + hoaDon.getDonGiaVonBQ() +
                        "', '" + hoaDon.getTriGiaVonBQ()        + "', '" + hoaDon.getDonGiaBQ() +
                        "', '" + hoaDon.getTriGiaBQ()           + "', '" + hoaDon.getDaGhiHDTC() +
                        "', '" + hoaDon.getDaVanChuyen()        + "', '" + hoaDon.getTriGiaVATDauRa() +
                        "', '" + hoaDon.getLaHangKhuyenMai()    + "', '" + hoaDon.getDaSuDung() +
                        "'), ";
            }
        }
//        Log.d(LOCATION+ " - CTHoaDon", "SQLQuery " + script);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("query", script);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        MarshalFloat marshal = new MarshalFloat();
        marshal.register(envelope);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.debug = true;
            transport.call(SOAP_ACTION, envelope);

            String xml = transport.responseDump;
//            Log.d(LOCATION+ " - CTHoaDon","XML file : " + xml);
            try {
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e) {
                list_excep.add(e);
//                Log.d(LOCATION, "XMLPull");
                e.printStackTrace();
            }
            String nodeName;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        //Sucess = 1; fail = -1 ; had = 0;
                        if (nodeName.equalsIgnoreCase("fExecuteNonQueryResult")) {
                            result = Integer.valueOf(parser.nextText());
                        } else if (nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else { break; }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
            list_excep.add(e);
            e.printStackTrace();
        }
        return result;
    }

    private void upThanhToanHoaDon(String in_table) {
//        sqlController.open();
        arr_ThanhToanHoaDon = sqlController.getThanhToanHoaDonForSyncUp(in_table);
        numberToPub = getNumberToPublishProgress(34);
        if(arr_CTHoaDon.isEmpty()) {
            Log.d(LOCATION+ " - ThanhToanHoaDon", "Không có dữ liệu cần đồng bộ");
        } else {
            numberToPub = getNumberToPublishProgress(34);
            int numberEffect = GetResultInsertThanhToanHoaDon(arr_ThanhToanHoaDon, in_table);
            if(numberEffect == arr_ThanhToanHoaDon.size()) {
                for(int i=0;i<arr_ThanhToanHoaDon.size();i++) {
                    ThanhToanHoaDon temp = arr_ThanhToanHoaDon.get(i);
                    Log.d(LOCATION + " - ThanhToanHoaDon", "Insert thành công " + temp.getHoaDonID());
                    sqlController.updateDataHadSync(temp.getHoaDonID(), in_table);
                    if(i % numberToPub == 0){
                        publishProgress("" + i);
                    }
                }
//                sqlController.close();
            } else {
                Log.d(LOCATION+ " - ThanhToanHoaDon","Đồng bộ không thành công");
            }
        }

    }

    private int GetResultInsertThanhToanHoaDon(ArrayList<ThanhToanHoaDon> in_arr, String in_tableName) {
        int result = 0;
        final String NAMESPACE = "http://tempuri.org/";
        //TODO : change METHOD_NAME
        final String METHOD_NAME = "fExecuteNonQuery";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://192.168.200.31:80/MyService/tungSqlServerProxy.asmx";

        //TODO: addProperty for ThanhToanHoaDon
        String script = "INSERT INTO [" + in_tableName + "] " +
                " ([HoaDonID] " + ",[MaHinhThuc] " + ",[MaNhomThanhToan]" + ", [MaThe]" + ", [ChuThe]" +
                ", [ThanhTien]" + ", [TyGiaNgoaiTe]" + ", [ThanhTienQuiDoi]" + ", [TLFee]" + ", [DaVanChuyen]" + ", [STT])" +
                " VALUES ";
        for(int i =0;i<in_arr.size();i++) {
            ThanhToanHoaDon temp = in_arr.get(i);
            if(i == in_arr.size()- 1) {
                script += "( '" + temp.getHoaDonID() +
                        "', '" + temp.getMaHinhThuc() +
                        "', '" + temp.getMaNhomThanhToan() +
                        "', '" + temp.getMathe() +
                        "', '" + temp.getChuThe() +
                        "', '" + temp.getThanhTien() +
                        "', '" + temp.getTyGiaNgoaiTe() +
                        "', '" + temp.getThanhTienQuyDoi() +
                        "', '" + temp.getTLFee() +
                        "', '" + temp.getDaVanChuyen() +
                        "', '" + temp.getSTT() +
                        "') ";
            } else {
                script += "( '" + temp.getHoaDonID() +
                        "', '" + temp.getMaHinhThuc() +
                        "', '" + temp.getMaNhomThanhToan() +
                        "', '" + temp.getMathe() +
                        "', '" + temp.getChuThe() +
                        "', '" + temp.getThanhTien() +
                        "', '" + temp.getTyGiaNgoaiTe() +
                        "', '" + temp.getThanhTienQuyDoi() +
                        "', '" + temp.getTLFee() +
                        "', '" + temp.getDaVanChuyen() +
                        "', '" + temp.getSTT() +
                        "'), ";
            }
        }
//        Log.d(LOCATION+ " - ThanhToanHoaDon", "SQLQuery " + script);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("query", script);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        MarshalFloat marshal = new MarshalFloat();
        marshal.register(envelope);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.debug = true;
            transport.call(SOAP_ACTION, envelope);

            String xml = transport.responseDump;
//            Log.d(LOCATION+ " - ThanhToanHoaDon","XML file : " + xml);
            try {
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e) {
                list_excep.add(e);
//                Log.d(LOCATION, "XMLPull");
                e.printStackTrace();
            }
            String nodeName;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        //Sucess = 1; fail = -1 ; had = 0;
                        if (nodeName.equalsIgnoreCase("fExecuteNonQueryResult")) {
                            result = Integer.valueOf(parser.nextText());
                        } else if (nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else { break; }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
            list_excep.add(e);
            e.printStackTrace();
        }
        return result;
    }
}


