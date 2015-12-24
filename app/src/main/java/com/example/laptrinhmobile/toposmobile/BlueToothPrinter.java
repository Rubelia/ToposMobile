package com.example.laptrinhmobile.toposmobile;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Other.PublicFunction;


public class BlueToothPrinter extends Activity
{

    /** Called when the activity is first created. */
    private static final String LOCATION = "BlueToothPrinter";
    EditText message;
    Button printbtn;

    String testConver ;
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    PublicFunction pF ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_printer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        message = (EditText)findViewById(R.id.message);
        printbtn = (Button)findViewById(R.id.printButton);
        pF = new PublicFunction();
        testConver = pF.UnicodeToNoSign("Thắng Nguyễn Huỳnh Xuân");
        printbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }
    protected void connect() {
        if (btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btoutputstream = opstream;
            print_bt();
        }
    }
    private void print_bt() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            btoutputstream = btsocket.getOutputStream();

//            String sendData = "Test TP POS \r \n" ;
            String BILL = generalStrBill();
//            char[] str = BILL.toCharArray();
//            String temp ="";
//            for(int i=0;i<str.length;i++) {
//                temp += unicodeEscaped(str[i]);
////                Log.d(LOCATION,temp);
//            }
//            Log.d(LOCATION,"Last: " +temp);
//            btoutputstream.write(temp.getBytes("UTF-8"));
            settingForPrint(btoutputstream);
//            btoutputstream.write(sendData.getBytes());
            btoutputstream.write(BILL.getBytes());
            btoutputstream.flush();
        } catch (IOException e) {
            Log.d(LOCATION,"IOException " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void settingForPrint(OutputStream os) {
        try {
            int gs = 29;
            os.write(intToByteArray(gs));
            int h = 104;
            os.write(intToByteArray(h));
            int n = 162;
            os.write(intToByteArray(n));

            // Setting Width
            int gs_width = 29;
            os.write(intToByteArray(gs_width));
            int w = 119;
            os.write(intToByteArray(w));
            int n_width = 2;
            os.write(intToByteArray(n_width));

            // Print BarCode
            int gs1 = 29;
            os.write(intToByteArray(gs1));
            int k = 107;
            os.write(intToByteArray(k));
            int m = 73;
            os.write(intToByteArray(m));
//            String barCodeVal = "ASDFC028060000005";// "HELLO12345678912345012";
//            Log.d(LOCATION,"Barcode Length : " + barCodeVal.length());
//            int n1 = barCodeVal.length();
//            os.write(intToByteArray(n1));
        } catch (IOException IOEx) {
            Log.d(LOCATION, "IOException - settingForPrint: " + IOEx.getMessage());
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
//        for (int k = 0; k < b.length; k++) {
//            Log.d(LOCATION,"Values - " + value +" Selva  [" + k + "] = " + "0x"
//                    + UnicodeFormatter.byteToHex(b[k]));
//        }
        return b[3];
    }

    private String generalStrBill() {
        String result;

        result = "Simply Mart \n" +
                " 273 Pham Van Chieu \n" +
                " HOA DON BAN LE \n" +
                " ---o0o--- \n" +
                " Liên: 1 \n";
        result += "\nInvoice No: ABCDEF28060000005" + "    "
                + "04-08-2011\n";
        result = result
                + "-----------------------------------------";
        result = result + "\n\n";
        result = result + "Total Qty:" + "      " + "2.0\n";
        result = result + "Total Value:" + "     " + "17625.0\n";
        result = result
                + "-----------------------------------------\n";
        result += testConver;
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            Log.d(LOCATION,"IOException " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if(btsocket != null){
                print_bt();
            }
        } catch (Exception e) {
            Log.d(LOCATION,"Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

}

