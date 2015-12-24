package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.laptrinhmobile.toposmobile.Object.HinhThucThanhToan;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Edit_HinhThucThanhToan extends Activity {

    private String LOCATION = "Cash";
    Activity context = this;
    SharedPreference sharedPreference;
    ArrayList<String> arr_err;
    double amount ;
    HinhThucThanhToan hinhThucThanhToan;
    String hoaDonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__hinh_thuc_thanh_toan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            hinhThucThanhToan  = (HinhThucThanhToan) extras.getSerializable("HinhThucThanhToan");
            amount = extras.getDouble("AMOUNT");
            hoaDonID = extras.getString("HoaDonID");
        }

        final EditText editTxtAmountForCash = (EditText) findViewById(R.id.editTxtAmount);
        editTxtAmountForCash.setText(""+amount);
        final TextView txtViewTenHinhThuc = (TextView) findViewById(R.id.txtViewTenHinhThuc);
        txtViewTenHinhThuc.setText(hinhThucThanhToan.getTenHinhThuc());
        final TextView txtViewMaNguyenTe = (TextView) findViewById(R.id.txtViewMaNguyenTe);
        txtViewMaNguyenTe.setText(hinhThucThanhToan.getMaNguyenTe());


        sharedPreference = new SharedPreference();
        Button btnBack = (Button) findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr_err = new ArrayList<String>();
                if(editTxtAmountForCash.getText().toString().isEmpty()) {
                    String err = "Chưa điền số tiền";
                    arr_err.add(err);
                    showNotice(arr_err);
                } else {
//                    TypePayment typePayment = new TypePayment();
//                    typePayment.setAmount(Double.valueOf(editTxtAmountForCash.getText().toString()));
//                    Log.d(LOCATION, "Amount : " + typePayment.getAmount());
//                    typePayment.setType("Cash");
//                    sharedPreference.saveTypePayment(context, typePayment);
//                    sharedPreference.saveHinhThucThanhToan(context, hinhThucThanhToan);
                    String amount = editTxtAmountForCash.getText().toString();
                    ThanhToanHoaDon thanhToanHoaDon = new ThanhToanHoaDon(hoaDonID,hinhThucThanhToan,amount);
                    sharedPreference.saveThanhToanHoaDon(context, thanhToanHoaDon);
//                    sharedPreference.saveIsChoosePayment(context);
//                    sharedPreference.saveAmount(context,amount);
                    finish();
                }
            }
        });
//        tools:showIn="@layout/activity_edit__hinh_thuc_thanh_toan"
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void showNotice (ArrayList<String> str) {
        String outStr = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for(int i=0;i<str.size();i++) {
            if(i == str.size()-1) {
                outStr += str.get(i) ;
            } else outStr += str.get(i) + "\n";
        }
        builder.setMessage(outStr);
        AlertDialog alert = builder.create();

        alert.show();
    }

}
