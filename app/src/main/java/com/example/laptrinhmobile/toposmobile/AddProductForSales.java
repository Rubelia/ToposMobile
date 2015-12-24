package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.laptrinhmobile.toposmobile.Adapter.DSHangHoa_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa_HinhAnh;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductForSales extends Activity {

    Activity context = this;
//    String LOCATION = "AddProductForSales";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private ArrayList<HangHoa> arr_HangHoa ;
    ArrayList<String> arr_err;
    private SQLController sqlController;
    private SharedPreference sharedPreference;
    private Button btnViewList, btnViewPanel, btnViewCatalog;
    private ListView listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_for_sales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedPreference = new SharedPreference();
        sqlController = new SQLController(getApplicationContext());
//        sqlController.open();
        btnViewList = (Button) findViewById(R.id.btnViewList);
        btnViewCatalog = (Button) findViewById(R.id.btnViewCatalog);
        btnViewPanel = (Button) findViewById(R.id.btnViewPanel);

        btnViewList.setEnabled(false);



        Button btnScanBarcode = (Button) findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //start the scanning activity from the com.google.zxing.client.android.SCAN intent
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    //on catch, show the download dialog
                    showDialog(AddProductForSales.this, getString(R.string.mess_not_found_scand_tool), getString(R.string.mess_ass_download_scand_tool), getString(R.string.mess_yes_download_scand_tool), getString(R.string.mess_no_download_scand_tool)).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sqlController.open();
        arr_HangHoa = sqlController.getDSHangHoa();
//        saveImgForHangHoa(arr_HangHoa);
        sqlController.close();

        listProduct = (ListView) findViewById(R.id.listProduct);
        DSHangHoa_ArrayAdapter adapter = new DSHangHoa_ArrayAdapter(this,R.layout.item_hanghoa,arr_HangHoa);
        listProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HangHoa hangHoa_clicked = arr_HangHoa.get(position);
//                hangHoa_clicked.setPosition(position);
//                sharedPreference.saveHangHoa(getApplicationContext(), hangHoa_clicked);
                Intent view_sales = new Intent(getApplication(), Edit_Product.class);
                Bundle send = new Bundle();
                send.putSerializable("HangHoa", hangHoa_clicked);
                view_sales.putExtras(send);
                startActivity(view_sales);
                finish();
            }
        });
        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DSHangHoa_ArrayAdapter adapter = new DSHangHoa_ArrayAdapter(context, R.layout.item_hanghoa, arr_HangHoa);
                listProduct.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                btnViewList.setEnabled(false);
                btnViewCatalog.setEnabled(true);
                btnViewPanel.setEnabled(true);
            }
        });

        btnViewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DSHangHoa_ArrayAdapter adapter = new DSHangHoa_ArrayAdapter(context, R.layout.item_hanghoa_catalog, arr_HangHoa);
                listProduct.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                btnViewList.setEnabled(true);
                btnViewCatalog.setEnabled(false);
                btnViewPanel.setEnabled(true);
            }
        });

        btnViewPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnViewList.setEnabled(true);
                btnViewCatalog.setEnabled(true);
                btnViewPanel.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product_for_sales, menu);
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
    //action for scanbarcode
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                sqlController.open();
                if(!sqlController.checkHangHoaHasMaHH(contents)) {
                    arr_err = new ArrayList<String>();
                    arr_err.add("Hàng hóa " + contents + " không tồn tại");
                    showNotice(arr_err);
                } else {
                    //TODO : add hang hoa to list view
                    HangHoa get = sqlController.getHangHoaHasMaHH(contents);
                    sqlController.close();
                    sharedPreference.saveHangHoa(getApplicationContext(),get);
                    finish();
                }
            }
        }
    }

    public void showNotice (ArrayList<String> str) {
        String outStr = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for(int i=0;i<str.size();i++) {
            if(i == str.size()-1) {
                outStr += str.get(i) ;
            }
            else outStr += str.get(i) + "\n";
        }
        builder.setMessage(outStr);
        AlertDialog alert = builder.create();
        alert.show();
    }


}
