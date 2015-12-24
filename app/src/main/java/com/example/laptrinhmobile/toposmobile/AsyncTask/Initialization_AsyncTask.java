package com.example.laptrinhmobile.toposmobile.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Initialization;
import com.example.laptrinhmobile.toposmobile.Login;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.R;
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
 * Created by LapTrinhMobile on 10/15/2015.
 */
public class Initialization_AsyncTask extends AsyncTask<String, String, String> {

    private static String LOCATION = "Initialization_AsyncTask";
    private static ProgressDialog pDialog;
    private static Dialog changePassDialog;
    AlertDialog alertDialog;
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    public String IP ;

    SQLController sqlController;
    Activity contextCha;

    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;

    public Initialization_AsyncTask (Activity ctx, String inputIP) {
        contextCha = ctx;
        this.IP = inputIP;
        pDialog = new ProgressDialog(ctx);
        changePassDialog = new Dialog(ctx);
    }
    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();
        sqlController = new SQLController(contextCha);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Đang khởi tạo dữ liệu khi lần đầu sử dụng");
//        pDialog.setMessage("Bảng Hàng hóa");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        alertDialog = new AlertDialog.Builder(contextCha).create();
    }
    @Override
    protected String doInBackground(String...params) {
        sqlController.instance();
        sqlController.open();
        try {
            sqlController.createTable();
            if(sqlController.checkIP()) {
                //don't do anything
//            Log.d("Main", "Run here");
            } else sqlController.insertIP();
            //get IP
            IP = sqlController.getStrIP();
        } catch (SQLException err) {
            createNoticeDialog(err.getMessage());
        }
        sqlController.close();
        publishProgress("Done");
        //connect to webservice
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://" + IP +"/tungSqlServerProxy.asmx";
//        Log.d(LOCATION, URL);
        String script = "SELECT TOP 1 MaNV, TenNV, TenDangNhap, MatKhau, MaCH, MaNhomNV FROM NhanVien ";
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
//            Log.d(LOCATION, xml);
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
            while(eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        if( nodeName.equalsIgnoreCase("MaNV")) {
                            publishProgress("100");
                        } else {
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
            publishProgress("101");
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
        String condition = values[0];
        if(condition.equalsIgnoreCase("Done")) pDialog.setTitle("Đang kiểm tra kết nối với Webservice");
        if (condition.equalsIgnoreCase("101")) {
            pDialog.dismiss();
            CreateAlertDialog(alertDialog);
        } else if(condition.equalsIgnoreCase("100")) {
            Intent login_view = new Intent(contextCha,Login.class);
            Bundle b = new Bundle();
            b.putString("IP",IP);
            login_view.putExtras(b);
            contextCha.startActivity(login_view);
            contextCha.finish();
        }
    }
    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        pDialog.dismiss();
        sqlController.close();

//        Intent view_login = new Intent(contextCha, Login.class);


        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }

//        contextCha.startActivity(view_login);
    }
    public void CreateAlertDialog(final AlertDialog in) {
        in.setTitle("Kết nối không thành công");
        in.setCanceledOnTouchOutside(false);
        in.setMessage("Vui lòng kiểm tra lại đường truyền hoặc kiểm tra IP");
        in.setButton(AlertDialog.BUTTON_NEUTRAL, "Thử lại", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Initialization_AsyncTask intance_asyncTask = new Initialization_AsyncTask(contextCha,"");
                intance_asyncTask.execute();
                in.dismiss();
            }
        });
        in.setButton(AlertDialog.BUTTON_NEGATIVE, "Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                in.dismiss();
                contextCha.finish();
            }
        });
        in.setButton(AlertDialog.BUTTON_POSITIVE, "Thiết lập IP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                createSettingDialog();
                changePassDialog.show();
            }
        });
        in.show();
    }

    private void createSettingDialog(){

        changePassDialog.setContentView(R.layout.setting_view);
        changePassDialog.setTitle("Thiết lập IP");
        changePassDialog.setCancelable(false);

        final Button ok = (Button) changePassDialog.findViewById(R.id.bt_ok);
        final Button cancel = (Button) changePassDialog.findViewById(R.id.bt_cancel);
        final EditText IPNew = (EditText) changePassDialog.findViewById(R.id.IPNew);

        final EditText ip_old = (EditText) changePassDialog.findViewById(R.id.IP_Old);

        ip_old.setText(IP);
//        ip_old.setId(IP_OLD_ID);
//        IPNew.setId(IP_NEW_CLEAR);
//        KV_New.setVisibility(true);
        ip_old.setFocusable(false);
//       kv_old.setFocusable(false);
//       ch_old.setFocusable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //xu ly su kien khi nhap dữ liệu vào :3
                String ip_new = IPNew.getText().toString();
//            	String kv_new = KV_New.getText().toString();
//            	String ch_new = CH_New.getText().toString();
                if (ip_new.length() != 0) {
//                    db.execSQL("UPDATE config SET IP ='"+ ip_new +"'");
                    sqlController.open();
                    try {
                        sqlController.updateIP(ip_new);
                    } catch (SQLException err) {
                        err.printStackTrace();
                        createNoticeDialog(err.getMessage());
                    }
                    sqlController.close();
                    Toast.makeText(contextCha, "Đã cập nhật xong IP", Toast.LENGTH_LONG).show();
                    changePassDialog.dismiss();
                    Initialization_AsyncTask intance_asyncTask = new Initialization_AsyncTask(contextCha,IP);
                    intance_asyncTask.execute();
                } else {
                    String err = "Chưa điền IP mới";
                    createNoticeDialog(err);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassDialog.dismiss();
            }
        });
    }
    public void createNoticeDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
        builder.setMessage(str);
        AlertDialog alert = builder.create();
        alert.show();
    }
}

