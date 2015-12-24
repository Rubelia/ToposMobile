package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Adapter.DSHangHoa_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Adapter.DSTypePayment_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Object.CTHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HoaDon;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.Timer;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class Payment extends Activity {


//    Calendar c;

//    private String LOCATION = "Payment";
    Activity context = this;
    private SharedPreference sharedPreference;
    Timer timerKetThuc;
    TextView txtViewMoneyHasPay, txtViewMoneyHadPay, txtViewCashInReturn;
    ImageButton imgBtnCompletePayment, imgBtnQuickPayment, imgBtnChoosePayment;
    ArrayList<HangHoa> arr_HangHoa;
//    ArrayList<TypePayment> arr_TypePayment ;
    ArrayList<ThanhToanHoaDon> arr_ThanhToanHoaDon ;
//    private HoaDon hoaDon;
//    private CTHoaDon ctHoaDon;
    private String HoaDonID;
    double total, cashInReturn, amount, cashHasToPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedPreference = new SharedPreference();


        txtViewMoneyHasPay = (TextView) findViewById(R.id.txtViewMoneyHasPay);
        txtViewMoneyHadPay = (TextView) findViewById(R.id.txtViewMoneyHadPay);
        txtViewCashInReturn = (TextView) findViewById(R.id.txtViewCashInReturn);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            arr_HangHoa = (ArrayList<HangHoa>) extras.getSerializable("arr_HangHoa");
            HoaDonID = extras.getString("HoaDonID");
        }
        arr_ThanhToanHoaDon = new ArrayList<ThanhToanHoaDon>();

        total = 0;
        amount = 0;
        for(int i=0;i<arr_HangHoa.size();i++) {
            total += calTotal(arr_HangHoa.get(i));
        }
        cashHasToPay = total;
        txtViewMoneyHasPay.setText(String.format("%s%s", getString(R.string.txtViewMoneyHasPay), total));
        txtViewCashInReturn.setText(String.format("%s%s", getString(R.string.txtViewCashInReturn), total));

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgBtnChoosePayment = (ImageButton) findViewById(R.id.imgBtnChoosePayment);
        imgBtnChoosePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_choose_payment = new Intent(getApplication(), ChoosePayment.class);
                Bundle b = new Bundle();
                total -= amount;
                b.putDouble("AMOUNT",total);
                b.putString("HoaDonID",HoaDonID);
                view_choose_payment.putExtras(b);
                startActivity(view_choose_payment);
            }
        });

        imgBtnQuickPayment = (ImageButton) findViewById(R.id.imgBtnQuickPayment);

        imgBtnCompletePayment = (ImageButton) findViewById(R.id.imgBtnCompletePayment);
        imgBtnCompletePayment.setEnabled(false);
        imgBtnCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                timerKetThuc = new Timer();
                b.putSerializable("Timer",timerKetThuc);
                b.putString("HoaDonID",HoaDonID);
                b.putSerializable("ArrThanhToanHoaDon",arr_ThanhToanHoaDon);
                b.putSerializable("ArrHangHoa",arr_HangHoa);
                Intent view_payment_complete = new Intent(getApplication(),PaymentComplete.class);
                view_payment_complete.putExtras(b);
                startActivity(view_payment_complete);
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        ThanhToanHoaDon thanhToanHoaDon = sharedPreference.getThanhToanHoaDon(context);
        amount = thanhToanHoaDon.getThanhTien();
        cashInReturn = total - amount;
        if(amount == 0 )
        {
            //không save đc trên shared
            // mới tạo mới
        } else if( cashInReturn == 0) {
            arr_ThanhToanHoaDon.add(thanhToanHoaDon);
            txtViewCashInReturn.setText(String.format("%s%s", getString(R.string.txtViewCashInReturn), cashInReturn));
            txtViewMoneyHadPay.setText(String.format("%s%s", getString(R.string.txtViewMoneyHadPay), cashHasToPay - cashInReturn));
            imgBtnCompletePayment.setEnabled(true);
            imgBtnChoosePayment.setEnabled(false);
            imgBtnQuickPayment.setEnabled(false);

        } else {
            arr_ThanhToanHoaDon.add(thanhToanHoaDon);
            txtViewCashInReturn.setText(String.format("%s%s", getString(R.string.txtViewCashInReturn), cashInReturn));
            txtViewMoneyHadPay.setText(String.format("%s%s", getString(R.string.txtViewMoneyHadPay), cashHasToPay - cashInReturn));
        }
//        if(typePayment.getAmount() <= 0) {
//            //don't add
//            Log.d(LOCATION,"Don't add");
//        }
//        else if(typePayment.getPosition() < 0) {
//            arr_TypePayment.add(typePayment);
//            sharedPreference.clearSharedPreference(context);
//            Log.d(LOCATION,"First add");
//        } else {
//            arr_TypePayment.set(typePayment.getPosition(),typePayment);
//            sharedPreference.clearSharedPreference(context);
//            Log.d(LOCATION, "Replace");
//        }
//        cashInReturn = total;
//        for(int i=0;i<arr_TypePayment.size();i++) {
//            cashInReturn -= arr_TypePayment.get(i).getAmount();
//        }
//
//
//        if(cashInReturn == 0 )
//        {
//            txtViewCashInReturn.setText("Tiền còn lại: \n" + "     00.00" );
//            txtViewMoneyHadPay.setText("Tiền đã trả: \n" + "   "+ (total - cashInReturn));
//            imgBtnCompletePayment.setEnabled(true);
//            imgBtnChoosePayment.setEnabled(false);
//            imgBtnQuickPayment.setEnabled(false);
//        } else {
//            txtViewCashInReturn.setText("Tiền còn lại: \n" + cashInReturn );
//            txtViewMoneyHadPay.setText("Tiền đã trả: \n" + "   " + (total - cashInReturn));
////            imgBtnCompletePayment.setEnabled(true);
////            imgBtnChoosePayment.setEnabled(false);
////            imgBtnQuickPayment.setEnabled(false);
//        }

        ListView listPayment = (ListView) findViewById(R.id.listPayment);
        DSTypePayment_ArrayAdapter adapter = new DSTypePayment_ArrayAdapter(context,R.layout.item_type_payment,arr_ThanhToanHoaDon);
        listPayment.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
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
    private double calTotal(HangHoa in_HangHoa) {
//        double result = 0;
        return in_HangHoa.getQuantity() * in_HangHoa.getTienBan();
//        result = PreTaxMoney + PreTaxMoney * in_HangHoa.getVATDauRa() /100 ;
//        return result;
    }


}
