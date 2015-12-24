package com.example.laptrinhmobile.toposmobile.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import com.example.laptrinhmobile.toposmobile.Object.CaBan;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

//import java.io.Do
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
 * Created by LapTrinhMobile on 10/5/2015.
 */
public class Sync_AsyncTask extends AsyncTask<String, String, String> {

    private static final String LOCATION = "Sync_AsyncTask";
    private static ProgressDialog  pDialog;
//    public static final int progress_bar_type = 0;
    //timer
//    private long startServiceTime = 0L;
//    private long timeInServiceMiliseconds = 0L;
//    private long startParserXML = 0L;
//    private long timeInParserXMLMiliseconds = 0L;
//        private Handler customeHandler = new Handler();
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    public String IP ;

//    public ArrayList<CaBan> arrCaBan = new ArrayList<CaBan>();
    public ArrayList<HangHoa> arrHangHoa = new ArrayList<HangHoa>();
    HangHoa emp = new HangHoa();
    int numberRecord;
    double numberToPub ;
    SQLController sqlController;
    Activity contextCha;
    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;

    public Sync_AsyncTask (Activity ctx, String inputIP) {
        contextCha = ctx;
//        adapter = new DSCaBan_ArrayAdapter(contextCha, R.layout.item_caban, arrCaBan ) ;
        this.IP = inputIP;
        pDialog = new ProgressDialog(ctx);
    }
    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();
        sqlController = new SQLController(contextCha);
        sqlController.open();
        arrHangHoa = new ArrayList<HangHoa>();
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setTitle("Đang đồng bộ dữ liệu - Bảng hàng hóa");
        pDialog.setMessage("Kết nối với service");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected String doInBackground(String...params) {

//        IP = "127.0.0.1";
        ArrayList<String> arrHangHoaID ;
        arrHangHoaID = sqlController.getAllHangHoaId();
        String id ;
        id = "'";
        if(arrHangHoaID == null)
        {
            id = "''";
        } else {
            for(int i=0;i<arrHangHoaID.size();i++) {
                if(i == arrHangHoaID.size()-1) {
                    id += arrHangHoaID.get(i) + "'";
                } else {
                    id += arrHangHoaID.get(i) + "', '";
                }
            }
        }

//        sqlController.close();
        String diachiWebService = "/tungSqlServerProxy.asmx";
        String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
//        String URL = "http://192.168.200.31:80/MyService/tungSqlServerProxy.asmx";
//        int numberRecord = 0;
//        String script = "Select TOP 100 * from CaBan";
//        long total = 0;
        String script = "Select TOP 100 * from HangHoa where HangHoaID not in (" + id + ");";
//        Log.d(LOCATION,"List ID: " + id);
//        startServiceTime = SystemClock.uptimeMillis();
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
//            pDialog.setMessage("Đang kết nói Service");
            publishProgress("50");
//            numberToPub =
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = builderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            org.w3c.dom.Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "//Table";
//            InputSource inputSource = new InputSource("result.xml");


            NodeList nodeList;
            try {
                nodeList = (NodeList) xpath.evaluate(expression, xmlDocument,
                        XPathConstants.NODESET);
                numberRecord = nodeList.getLength();
                numberToPub = getNumberToPublishProgress(numberRecord);
                Log.d(LOCATION,"Number of Record in XML: " + numberRecord);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }


            try{
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e)
            {
                list_excep.add(e);
                e.printStackTrace();
            }

//            timeInServiceMiliseconds = SystemClock.uptimeMillis() - startServiceTime;
//            Toast.makeText(contextCha,"Thời gian kết nói Service: " + timeInServiceMiliseconds , Toast.LENGTH_LONG).show();
//            Log.d(LOCATION, "Thời gian kết nối Service: " + timeInServiceMiliseconds);

            String nodeName;
            int count=50;
//            pDialog.setMessage("Đang xử lý dữ liệu");
//            startParserXML = SystemClock.uptimeMillis();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                HangHoa tmp = new HangHoa();
                eventType = parser.next();
                switch(eventType)
                {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        if( nodeName.equalsIgnoreCase("HangHoaID")) {
                            emp.setHangHoaId(parser.nextText());
                        }
                        else if(nodeName.equalsIgnoreCase("MaHH")) {
                            emp.setMaHH(parser.nextText());
                        } else if(nodeName.equalsIgnoreCase("TenHH")) {
                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaGoiNho")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaCu")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaKeHang")) {
//                            emp.setTenHH(parser.nextText());
                            emp.setMaKeHang("0000");
                        }else if(nodeName.equalsIgnoreCase("MaNCC")) {
                            emp.setMaNCC(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaNganh")) {
                            emp.setMaNganh(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaNhom")) {
                            emp.setMaNhom(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaPhanNhom")) {
                            emp.setMaPhanNhom(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaPLU")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("VATDauVao")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("VATDauRa")) {
                            emp.setVATDauRa(Double.valueOf(parser.nextText()));
                        }else if(nodeName.equalsIgnoreCase("GiaVon")) {
                            emp.setTienBan(Double.valueOf(parser.nextText()));
                        }else if(nodeName.equalsIgnoreCase("MaNVTao")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("NgayTao")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaNVCapNhat")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("NgayCapNhat")) {
                            Date date ;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                            String dateTime = parser.nextText();
                            try {
                                date = format.parse(dateTime);
                            } catch (ParseException e) {
                                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                                date = format.parse(dateTime);
                                // TODO Auto-generated catch block
//                                e.printStackTrace();
                            }
//                            Date
                            emp.setNgayCapNhat(date);
                        }else if(nodeName.equalsIgnoreCase("GhiChu")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("LoaiHangHoa")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("DonViTinh")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("CapDo")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("HienThiTrongDanhMuc")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("HangCanKy")) {
                            emp.setHangCanKy(Boolean.valueOf(parser.nextText()));
                        }else if(nodeName.equalsIgnoreCase("MaTam")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("DaDongBo")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F1")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F2")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F3")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F4")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F5")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("STT")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("DaHuy")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F6")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F7")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F8")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F9")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("F10")) {
//                            emp.setTenHH(parser.nextText());
                        }else if(nodeName.equalsIgnoreCase("MaPLHang")) {
//                            emp.setTenHH(parser.nextText());
                            count +=1;
                            if(count % numberToPub == 0){
                                publishProgress("" + count);
                            }
                            arrHangHoa.add(emp);
                            emp = tmp;
                        }
                        else if( nodeName.equalsIgnoreCase("faultstring"))
                        {
                            throw new Exception(parser.nextText());
                        }
                        else {
                            break;
                        }
                    case XmlPullParser.END_TAG:

                        break;
                }
            }
//            timeInServiceMiliseconds = SystemClock.uptimeMillis() -startServiceTime;
//            Toast.makeText(contextCha,"Thời gian xử lý dữ liệu: " + timeInServiceMiliseconds , Toast.LENGTH_LONG).show();
//            Log.d(LOCATION, "Thời gian kết nối xử lý dữ liệu: " + timeInServiceMiliseconds);

        }
        catch (Exception e)
        {
            list_excep.add(e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ta cÃƒÂ¡Ã‚ÂºÃ‚Â­p nhÃƒÂ¡Ã‚ÂºÃ‚Â­p giao diÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n trong hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y
     */
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub

        super.onProgressUpdate(values);
//        Log.d(LOCATION, "Values: " + values[0].toString());
//        pDialog.setProgressNumberFormat(values[0].toString());
        if(Integer.valueOf(values[0]) == 50)
            pDialog.setMessage("Đưa dữ liệu vào database");
        pDialog.setProgress(Integer.valueOf(values[0]));
    }
    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        sqlController.InsertHangHoa(arrHangHoa);
        pDialog.dismiss();
        sqlController.close();
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
//        Log.d(LOCATION,"getNumberToPublishProgress: " + result);
        return result;
    }
}

