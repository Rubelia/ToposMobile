package com.example.laptrinhmobile.toposmobile.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.laptrinhmobile.toposmobile.Object.HangHoa_HinhAnh;
import com.example.laptrinhmobile.toposmobile.Object.HinhThucThanhToan;
import com.example.laptrinhmobile.toposmobile.database.DBSQLServer;
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
import java.io.FileOutputStream;
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
import java.io.*;
import java.sql.*;
/**
 * Created by LapTrinhMobile on 10/28/2015.
 */

public class SyncHangHoa_HinhAnh_AsyncTask extends AsyncTask<String, String, String> {

    private static final String LOCATION = "SyncHangHoa_HinhAnh_AsyncTask";
    private static ProgressDialog pDialog;
//    public static final int progress_bar_type = 0;
    //timer
//    private long startServiceTime = 0L;
//    private long timeInServiceMiliseconds = 0L;
//    private long startParserXML = 0L;
//    private long timeInParserXMLMiliseconds = 0L;
    //    private Handler customeHandler = new Handler();
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    public String IP ;
    private ArrayList<HangHoa_HinhAnh> arr_HangHoa_hinhAnh ;
    HangHoa_HinhAnh emp = new HangHoa_HinhAnh();
    int numberRecord;
    double numberToPub ;
    SQLController sqlController;
    Activity contextCha;
    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;

    public SyncHangHoa_HinhAnh_AsyncTask (Activity ctx, String inputIP) {
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
        arr_HangHoa_hinhAnh = new ArrayList<HangHoa_HinhAnh>();
        sqlController.close();

        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setTitle("Đang đồng bộ dữ liệu - Bảng Hình Thức Thanh Toán");
        pDialog.setMessage("Kết nối với service");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected String doInBackground(String...params) {

//        DBSQLServer db = new DBSQLServer();
//        Connection conn=db.dbConnect(
//                "jdbc:jtds:sqlserver://192.168.200.31:1433/topos_vanlang","sa","123456");
////        db.insertImage(conn,"d://filepath//test.JPG");
//        db.getImageData(conn);
//        IP = "127.0.0.1";
        ArrayList<String> arrHangHoaID = new ArrayList<String>();
//        arrHangHoaID = sqlController.getAllHangHoaId();
        String id = "'";
        if(arrHangHoaID == null) {
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

        String diachiWebService = "/tungSqlServerProxy.asmx?WSDL";
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
//        String URL = "http://192.168.200.16:80/MyService/tungSqlServerProxy.asmx";
        String URL = "http://" + IP + diachiWebService;

        String script = "Select TOP 100 * from HangHoa_HinhAnh ";
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

            NodeList nodeList;
            try {
                nodeList = (NodeList) xpath.evaluate(expression, xmlDocument,
                        XPathConstants.NODESET);
                numberRecord = nodeList.getLength();
                numberToPub = getNumberToPublishProgress(numberRecord);
//                Log.d(LOCATION, "Number of Record in XML: " + numberRecord);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            try {
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e) {
                list_excep.add(e);
                e.printStackTrace();
            }

            String nodeName;
            int count=50;
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                HangHoa_HinhAnh tmp = new HangHoa_HinhAnh();
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
                            emp.setHangHoaID(parser.nextText());
                        } else if(nodeName.equalsIgnoreCase("HinhAnh")) {
                            String image = parser.nextText();
                            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
//                            Log.d(LOCATION, ""+ decodedString.toString() + " \n Image String: " + image);
//                            emp.setPath(parser.nextText());
                            emp.setHinhAnh(decodedString);
                            count +=1;
                            if(count % numberToPub == 0) {
                                publishProgress("" + count);
                            }
                            arr_HangHoa_hinhAnh.add(emp);
                            emp = tmp;
                        } else if( nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else {
                            break;
                        }
                    case XmlPullParser.END_TAG:

                        break;
                }
            }
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

        sqlController.open();
        sqlController.BeginTransaction();
        try {
            sqlController.deleteHangHoa_HinhAnh();
            sqlController.addHangHoaHinhAnh(arr_HangHoa_hinhAnh);
            sqlController.setTransactionSuccessful();
        } catch (SQLiteException sqlite) {
            sqlite.printStackTrace();
        }
        sqlController.endTransaction();
        sqlController.close();
        pDialog.dismiss();
        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }
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
