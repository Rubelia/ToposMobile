package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;

import com.example.laptrinhmobile.toposmobile.Object.Timer;

//import com.example.laptrinhmobile.toposmobile.Object.;

public class Edit_Product extends Activity {

    private static final String LOCATION = "Edit_Product";
    HangHoa send_HangHoa;
    Timer timer;
    private SharedPreference sharedPreference;
    private int quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__product);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedPreference = new SharedPreference();
        TextView txtViewMaHH = (TextView) findViewById(R.id.txtViewMaHH);
        TextView txtViewTenHH = (TextView) findViewById(R.id.txtViewTenHH);
        TextView txtViewGiaBan = (TextView) findViewById(R.id.txtViewGiaBan);
        TextView txtViewTax = (TextView) findViewById(R.id.txtViewTax);
        final TextView txtViewTotal = (TextView) findViewById(R.id.txtViewTotal);
        final EditText editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            send_HangHoa = (HangHoa) extras.getSerializable("HangHoa");

            quantity = send_HangHoa.getQuantity();

            txtViewMaHH.setText("\t" + send_HangHoa.getMaHH());
            txtViewTenHH.setText("\t" + send_HangHoa.getTenHH());
            txtViewGiaBan.setText(" \t " +send_HangHoa.getTienBan()+"");
            txtViewTax.setText(" \t " +send_HangHoa.getVATDauRa()+"  %");
            txtViewTotal.setText(" \t " + calTotal(send_HangHoa) + "");
            editTextQuantity.setText(" \t "+ quantity);
        } else {
            Log.d(LOCATION, "Không có hàng hóa");
        }

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_HangHoa.setQuantity(quantity);
                timer = new Timer();
                sharedPreference.FirstAddPro(getApplicationContext());
                sharedPreference.saveTimer(getApplicationContext(),timer);
                sharedPreference.saveHangHoa(getApplicationContext(), send_HangHoa);
                finish();
            }
        });

        ImageView imgViewBack = (ImageView) findViewById(R.id.imgViewBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button btnSub = (Button) findViewById(R.id.btnSub);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity -- ;
                editTextQuantity.setText("\t"+quantity);
                send_HangHoa.setQuantity(quantity);
                txtViewTotal.setText("\t" + calTotal(send_HangHoa));
                if(quantity == 1)
                    btnSub.setEnabled(false);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                editTextQuantity.setText("\t"+quantity);
                send_HangHoa.setQuantity(quantity);
                txtViewTotal.setText("\t" + calTotal(send_HangHoa));
                if(quantity > 1 )
                    btnSub.setEnabled(true);
            }
        });

        if(quantity > 1) btnSub.setEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit__product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public double calTotal(HangHoa in_HangHoa) {
        double result = 0;
        double PreTaxMoney = in_HangHoa.getQuantity() * in_HangHoa.getTienBan();
        result = PreTaxMoney + PreTaxMoney * in_HangHoa.getVATDauRa() /100 ;
//        Log.d(LOCATION,"Tổng tiền: " + result);
        return result;
    }
}
