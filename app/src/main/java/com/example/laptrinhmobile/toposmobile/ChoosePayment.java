package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Adapter.DSHinhThucThanhToan_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HinhThucThanhToan;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.util.ArrayList;

public class ChoosePayment extends Activity {

//    String LOCATION = "ChoosePayment";
    Activity context = this;
    ArrayList<HinhThucThanhToan> arr_HinhThucThanhToan, arr_selectHTTT ;
    SharedPreference sharedPreference;
    SQLController sqlController;
    HinhThucThanhToan hinhThucThanhToan;
    String hoaDonID ;

    double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedPreference = new SharedPreference();
        sqlController = new SQLController(context);
        arr_HinhThucThanhToan = new ArrayList<HinhThucThanhToan>();
        arr_selectHTTT = new ArrayList<HinhThucThanhToan>();

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            total = extras.getDouble("AMOUNT");
            hoaDonID = extras.getString("HoaDonID");
        }

        ImageView imgViewBack = (ImageView) findViewById(R.id.imgViewBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sqlController.open();
        arr_HinhThucThanhToan = sqlController.getHinhThucThanhToan();
        sqlController.close();
        ListView listHinhThucThanhToan = (ListView) findViewById(R.id.listHinhThucThanhToan);
        DSHinhThucThanhToan_ArrayAdapter adapter = new DSHinhThucThanhToan_ArrayAdapter(context,R.layout.item_hinhthucthanhtoan,arr_HinhThucThanhToan);
        listHinhThucThanhToan.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//            <ImageButton
//            android:layout_width="80dp"
//            android:layout_height="match_parent"
//            android:descendantFocusability="blocksDescendants"
//            android:focusable="false"
//            android:focusableInTouchMode="false"
//                    />

        listHinhThucThanhToan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hinhThucThanhToan = arr_HinhThucThanhToan.get(position);
//                arr_selectHTTT.add(hinhThucThanhToan);
                Intent view_choose_hinhthucthanhtoan = new Intent(getApplication(), Edit_HinhThucThanhToan.class);
                Bundle b = new Bundle();
                b.putSerializable("HinhThucThanhToan", hinhThucThanhToan);
                b.putString("HoaDonID", hoaDonID);
                b.putDouble("AMOUNT", total);
                view_choose_hinhthucthanhtoan.putExtras(b);
                startActivity(view_choose_hinhthucthanhtoan);
                finish();
            }
        });




    }


    @Override
    public void onResume() {
        super.onResume();

//        boolean isChossed = sharedPreference.getisChoosedPayment(context);
//
//        ThanhToanHoaDon thanhToanHoaDon = new ThanhToanHoaDon();
////        ThanhToanHoaDon thanhToanHoaDon = sharedPreference.getThanhToanHoaDon(context);
//        sharedPreference.saveThanhToanHoaDon(context,thanhToanHoaDon);
////        TypePayment typePayment = sharedPreference.getTypePayment(context);
////        sharedPreference.saveTypePayment(context, typePayment);
////        Log.d(LOCATION,"Amount: "+typePayment.getAmount());
//        double amount = sharedPreference.getAmount(context);
//        if(isChossed == true && total == amount)
//        {
//            thanhToanHoaDon = sharedPreference.getThanhToanHoaDon(context);
//            sharedPreference.saveThanhToanHoaDon(context,thanhToanHoaDon);
////            sharedPreference.saveArrHinhThucThanhToan(context, arr_selectHTTT);
////            sharedPreference.saveAmount(context,""+amount);
//            finish();
//        }
//        else {
//            total = total - amount;
//            sqlController.open();
//            arr_HinhThucThanhToan = sqlController.getHinhThucThanhToan();
//            sqlController.close();
//            ListView listHinhThucThanhToan = (ListView) findViewById(R.id.listHinhThucThanhToan);
//            DSHinhThucThanhToan_ArrayAdapter adapter = new DSHinhThucThanhToan_ArrayAdapter(context,R.layout.item_hinhthucthanhtoan,arr_HinhThucThanhToan);
//            listHinhThucThanhToan.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//
////            <ImageButton
////            android:layout_width="80dp"
////            android:layout_height="match_parent"
////            android:descendantFocusability="blocksDescendants"
////            android:focusable="false"
////            android:focusableInTouchMode="false"
////                    />
//
//            listHinhThucThanhToan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    hinhThucThanhToan = arr_HinhThucThanhToan.get(position);
//                    arr_selectHTTT.add(hinhThucThanhToan);
//                    Toast.makeText(getApplicationContext(), "dasdsadasdas", Toast.LENGTH_LONG).show();
//                    Intent view_choose_hinhthucthanhtoan = new Intent(getApplication(), Edit_HinhThucThanhToan.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("HinhThucThanhToan", hinhThucThanhToan);
//                    b.putString("HoaDonID",hoaDonID);
//                    b.putDouble("AMOUNT",total);
//                    view_choose_hinhthucthanhtoan.putExtras(b);
//                    startActivity(view_choose_hinhthucthanhtoan);
//                }
//            });
//        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_payment, menu);
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
}
