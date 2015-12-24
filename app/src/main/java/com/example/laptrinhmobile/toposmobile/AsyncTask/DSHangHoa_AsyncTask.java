package com.example.laptrinhmobile.toposmobile.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Adapter.DSCaBan_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Adapter.DSHangHoa_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Object.CaBan;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 10/5/2015.
 */
public class DSHangHoa_AsyncTask extends AsyncTask<String, HangHoa, ArrayList<HangHoa>> {

    private static final String LOCATION = "DSHangHoa_AsyncTask";
    //timer
    long startServiceTime = 0L;
    long timeInServiceMiliseconds = 0L;
    //    private Handler customeHandler = new Handler();
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    public String IP ;

    public ArrayList<HangHoa> arrHangHoa = new ArrayList<HangHoa>();
    DSHangHoa_ArrayAdapter adapter = null;
    HangHoa emp = new HangHoa();

    Activity contextCha;
    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;

    public DSHangHoa_AsyncTask (Activity ctx, String inputIP) {
        contextCha = ctx;
        adapter = new DSHangHoa_ArrayAdapter(contextCha, R.layout.item_hanghoa, arrHangHoa ) ;
        this.IP = inputIP;
    }
    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();
        Toast.makeText(contextCha, "Đang lấy dữ liệu! Vui lòng đợi giây lát...",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected ArrayList<HangHoa> doInBackground(String...params) {

//        IP = "127.0.0.1";
        String diachiWebService = "/tungSqlServerProxy.asmx";
        String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
//        String URL = "http://192.168.200.31:80/MyService/tungSqlServerProxy.asmx";

        String script = "Select TOP 100 HangHoaID, TenHH from HangHoa";

        startServiceTime = SystemClock.uptimeMillis();
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
//			System.out.println(xml);

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

            timeInServiceMiliseconds = SystemClock.uptimeMillis() - startServiceTime;
//            Toast.makeText(contextCha,"Thời gian kết nói Service: " + timeInServiceMiliseconds , Toast.LENGTH_LONG).show();
            Log.d(LOCATION, "Thời gian kết nối Service: " + timeInServiceMiliseconds);
            startServiceTime = 0L;
            timeInServiceMiliseconds = 0L;
            String nodeName;
            startServiceTime = SystemClock.uptimeMillis();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                HangHoa tmp = new HangHoa();
                eventType = parser.next();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        if( nodeName.equalsIgnoreCase("HangHoaID")) {
                            emp.setHangHoaId(parser.nextText());
                        } else if(nodeName.equalsIgnoreCase("TenHH")) {
                            emp.setTenHH(parser.nextText());
                            publishProgress(emp);
                            emp = tmp;
                        } else if( nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else break;
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
            timeInServiceMiliseconds = SystemClock.uptimeMillis() -startServiceTime;
//            Toast.makeText(contextCha,"Thời gian xử lý dữ liệu: " + timeInServiceMiliseconds , Toast.LENGTH_LONG).show();
            Log.d(LOCATION, "Thời gian kết nối xử lý dữ liệu: " + timeInServiceMiliseconds);

        } catch (Exception e) {
            list_excep.add(e);
            e.printStackTrace();
        }


        return null;
    }
    /**
     * ta cÃƒÂ¡Ã‚ÂºÃ‚Â­p nhÃƒÂ¡Ã‚ÂºÃ‚Â­p giao diÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n trong hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y
     */
    @Override
    protected void onProgressUpdate(HangHoa... values) {
        // TODO Auto-generated method stub

        arrHangHoa.add(values[0]);
//        System.out.println(arrHangHoa.size());
//        ListView lv1 = (ListView) contextCha.findViewById(R.id.listSaleProduct);
//        lv1.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        super.onProgressUpdate(values);


    }
    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(ArrayList<HangHoa> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }


        Toast.makeText(contextCha, "Đã lấy dữ liệu xong!",
                Toast.LENGTH_SHORT).show();
    }
}

