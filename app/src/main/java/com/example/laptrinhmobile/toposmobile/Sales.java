package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Adapter.DSHangHoa_ArrayAdapter;
import com.example.laptrinhmobile.toposmobile.AsyncTask.DSCaBan_AsyncTask;
import com.example.laptrinhmobile.toposmobile.AsyncTask.InsertCaBan_AsyncTask;
import com.example.laptrinhmobile.toposmobile.AsyncTask.Sync_AsyncTask;
import com.example.laptrinhmobile.toposmobile.AsyncTask.Sync_Up_AsyncTask;
import com.example.laptrinhmobile.toposmobile.Object.CTHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.CaBan;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HoaDon;
import com.example.laptrinhmobile.toposmobile.Object.Timer;
import com.example.laptrinhmobile.toposmobile.Other.PublicFunction;
import com.example.laptrinhmobile.toposmobile.Other.SharedPreference;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.UUID;

public class Sales extends Activity{
    Activity context = this;
    private SharedPreference sharedPreference;

    String LOCATION = "Sales";
//    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    boolean isAddPro;

    DSCaBan_AsyncTask mytt;
//    DSHangHoa_AsyncTask mytt;
    ArrayList<CaBan> arrCaBan = new ArrayList<CaBan>();
    ArrayList<HangHoa> arr_HangHoa = new ArrayList<HangHoa>();

    SQLController sqlController;
//    private static ArrayList<String> arr_err ;
    private TextView txtViewTotal ;
    String IP;
    private static final int CHIEUDAI_KITU_HOADON = 4;
    private static UUID HoaDonID ;
    HoaDon hoaDon;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isAddPro = false;
        arr_HangHoa = new ArrayList<HangHoa>();

        sqlController = new SQLController(context);
        sqlController.open();
        IP = sqlController.getStrIP();
        sqlController.close();
        sharedPreference = new SharedPreference();
//        sharedPreference.clearSharedPreference(getApplicationContext());

        final ImageButton imgBtnMenu = (ImageButton) findViewById(R.id.imgBtnMenu);
        imgBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openOptionsMenu();
                PopupMenu popup = new PopupMenu(Sales.this,imgBtnMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        arr_err = new ArrayList<>();
                        switch (item.getItemId()) {
                            case R.id.action_sync_down:
                                Sync_AsyncTask sync_asyncTask = new Sync_AsyncTask(context,IP);
                                sync_asyncTask.execute();
                                return true;
                            case R.id.action_settings:
                                createSettingDialog();
                                return true;
                            case R.id.action_sync_up:
                                Sync_Up_AsyncTask sync_up_asyncTask = new Sync_Up_AsyncTask(context,IP);
                                sync_up_asyncTask.execute();
//                                String err = "Chức năng đang trong giai đoạn hoàn thiện";
//                                arr_err.add(err);
//                                showNotice(arr_err);
                                return  true;
                            case R.id.action_connect_bluetooth:
                                Intent view_bluetooth_printer = new Intent(getApplicationContext(),BlueToothPrinter.class);
                                    startActivity(view_bluetooth_printer);
                                return true;
                            case R.id.action_send_SMS:
                                sendSMSMessage();
                                return true;

                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        TextView txtViewCustomer = (TextView) findViewById(R.id.txtViewCustomer);
        txtViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_customer = new Intent(getApplication(),Customer.class);
                startActivity(view_customer);
            }
        });

        txtViewTotal = (TextView) findViewById(R.id.txtViewTotal);
        txtViewTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent view_payment = new Intent(getApplication(),Payment.class);
            sharedPreference.saveArrHangHoa(getApplicationContext(), arr_HangHoa);
            Bundle b = new Bundle();
//                b.putSerializable("HoaDon",hoaDon);
//                b.putSerializable("CTHoaDon",ctHoaDon);
            b.putSerializable("arr_HangHoa",arr_HangHoa);
            b.putString("HoaDonID",HoaDonID.toString());
            view_payment.putExtras(b);
            startActivity(view_payment);
            }
        });

        Button btnAddProduct = (Button) findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_add_product = new Intent(getApplication(), AddProductForSales.class);
                startActivity(view_add_product);
            }
        });
    }

    private void setAlarmToRunReport() {
        final Dialog setAlarmDialog = new Dialog(this);
        setAlarmDialog.setContentView(R.layout.alarm_run_report);
        setAlarmDialog.setTitle("Thiết lập giờ chạy báo cáo");
        setAlarmDialog.setCancelable(true);
        setAlarmDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Timer timer = new Timer() ;
        isAddPro = sharedPreference.getisAddPro(getApplicationContext());
        HangHoa add_HangHoa = sharedPreference.getHangHoa(getApplicationContext());
        if(add_HangHoa.getHangHoaID().equalsIgnoreCase("")) {
            Log.d(LOCATION,"Don't add");
        } else {
            if(arr_HangHoa.isEmpty() && isAddPro ) {
                //// TODO: 10/19/2015 : khởi tạo HoaDon
                timer = new Timer(sharedPreference.getTimer(context));
                instanceHoaDon(timer);
            }
            if(add_HangHoa.getPosition() < 0) {
                add_HangHoa.setTimer(timer);
                arr_HangHoa.add(add_HangHoa);
                InsertHangHoaToCTHoaDon(add_HangHoa);
                sharedPreference.clearSharedPreference(context);
            } else {
                arr_HangHoa.set(add_HangHoa.getPosition(),add_HangHoa);
                add_HangHoa.setTimer(timer);
                //TODO: chỉnh sửa hàng hóa
                sharedPreference.clearSharedPreference(context);
            }
        }

        double total =0;
        for(int i=0;i<arr_HangHoa.size();i++) {
            total += calTotal(arr_HangHoa.get(i));
        }
        txtViewTotal.setText(String.format("%s%s", getString(R.string.txtViewTotal), total));

        DSHangHoa_ArrayAdapter adapter = new DSHangHoa_ArrayAdapter(context,R.layout.item_hanghoa_in_sales,arr_HangHoa);
        ListView listSalesProducts = (ListView) findViewById(R.id.listSaleProduct);
        listSalesProducts.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listSalesProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HangHoa hangHoa_clicked = arr_HangHoa.get(position);
            hangHoa_clicked.setPosition(position);
            Intent view_sales = new Intent(getApplication(),Edit_Product.class);
            Bundle send = new Bundle();
            send.putSerializable("HangHoa", hangHoa_clicked);
            view_sales.putExtras(send);
            startActivity(view_sales);
            }
        });
//        PublicFunction
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        menu.add(0, , 0, "Add").setIcon(android.R.drawable.ic_menu_add);
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        return in_HangHoa.getQuantity() * in_HangHoa.getTienBan();
    }
    public void instanceHoaDon(Timer timer) {
        String tableName = "HoaDon" + timer.getStrMonth() + timer.getYear();
//        Log.d(LOCATION, tableName);
        HoaDonID = UUID.randomUUID();
        hoaDon = new HoaDon();
        hoaDon.setHoaDonID(HoaDonID.toString());
//        Log.d(LOCATION, "HoaDonID: " + HoaDonID.toString());
        sqlController.open();
        int stt = sqlController.getCountHoaDon(tableName);
        hoaDon.setSTT("" + (stt + 1));
        PublicFunction publicFunction = new PublicFunction();
//        Log.d(LOCATION,"Timer : " + timer.toString());
        Date dateHD = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            dateHD = format.parse(timer.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String maHD = "AA0101" + timer.getTimeForHD();
        hoaDon.setNgayHD(dateHD);
        hoaDon.setMaHD(maHD + publicFunction.ThemKiTu("" + stt + 1, "0", CHIEUDAI_KITU_HOADON, 1));
        hoaDon.setMaQuay("AA0101");
        hoaDon.setMaHDGoc("NULL");
        hoaDon.setMaTheKHTT("NULL");
        hoaDon.setCaBan(1);
        hoaDon.setMaNV("AA0026");
        hoaDon.setGioBatDau(timer.getGio());
        hoaDon.setGioKetThuc(timer.getGio());
        hoaDon.setLoaiHoaDon(0);
        Date dateBatDau = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            dateBatDau = format.parse(timer.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        String ngayBatDau = publicFunction.dateFormat("yyyy-MM-dd HH:mm:ss.SSS",dateBatDau);
        hoaDon.setNgayBatDau(dateBatDau);
        Date dateKetThuc = new Date();
        try {
            dateKetThuc = format.parse(timer.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        hoaDon.setNgayKetThuc(dateKetThuc);
        hoaDon.setTriGiaBan(0);
        hoaDon.setTienCK(0);
        hoaDon.setThanhTienBan(0);
        hoaDon.setTienPhuThu(0);
        hoaDon.setDaIn(0);
        hoaDon.setDaCapNhatTon(0);
        hoaDon.setDaVanChuyen(0);
        sqlController.IntancesHoaDon(hoaDon,tableName);
        sqlController.close();
    }
    public void InsertHangHoaToCTHoaDon(HangHoa hangHoa) {
//        PublicFunction publicFunction = new PublicFunction();
        sqlController.open();
        Timer timer = hangHoa.getTimer();
        String tableName = "CTHoaDon" + timer.getStrMonth() + timer.getYear();
//        Log.d(LOCATION, tableName);
        CTHoaDon ctHoaDon = new CTHoaDon();

        ctHoaDon.setHoaDonID(HoaDonID.toString());
        ctHoaDon.setHangHoaID(hangHoa.getHangHoaID());
        ctHoaDon.setMaHH(hangHoa.getMaHH());
        int stt = sqlController.getCountForSTT(HoaDonID.toString(), tableName);
//        Log.d(LOCATION, "HoaDonID: " + HoaDonID.toString());
//        Log.d(LOCATION, "STT: " + stt);
        stt += 1;
        ctHoaDon.setSTT((stt));
        ctHoaDon.setLoaiHangHoaBan(0);
        ctHoaDon.setTenHH(hangHoa.getTenHH());
        Date dateNgayGioQuet = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            dateNgayGioQuet = format.parse(timer.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ctHoaDon.setNgayGioQuet(dateNgayGioQuet);
        ctHoaDon.setMaNccDM("NULL");
        ctHoaDon.setSoLuong(hangHoa.getQuantity());
        ctHoaDon.setDGBan(hangHoa.getTienBan());
        ctHoaDon.setTriGiaBan(hangHoa.getQuantity() * hangHoa.getTienBan());
        ctHoaDon.setTLCKGiamGia(0);
        ctHoaDon.setTienGiamGia(0);
        ctHoaDon.setTriGiaSauGiamGia(0);
        ctHoaDon.setTLCK_GioVang(0);
        ctHoaDon.setTienCK_GioVang(0);
        ctHoaDon.setTriGiaSauGioVang(0);
        ctHoaDon.setTLCKHD(0);
        ctHoaDon.setTienCKHD(0);
        ctHoaDon.setTriGiaSauCKHD(0);
        ctHoaDon.setTLCK_TheGiamGia(0);
        ctHoaDon.setTienCK_TheGiamGia(0);
        ctHoaDon.setThanhTienBan(hangHoa.getQuantity() * hangHoa.getTienBan());
        ctHoaDon.setTienPhuThu(0);
        ctHoaDon.setVATDauRa(hangHoa.getVATDauRa());
        ctHoaDon.setDonGiaVonBQ(0);
        ctHoaDon.setTriGiaVonBQ(0);
        ctHoaDon.setDonGiaBQ(0);
        ctHoaDon.setTriGiaBQ(0);
        ctHoaDon.setDaGhiHDTC(0);
        ctHoaDon.setDaVanChuyen(0);
        ctHoaDon.setTriGiaVATDauRa(0);
        ctHoaDon.setLaHangKhuyenMai(0);
        ctHoaDon.setDaSuDung(0);
//        Date dateHanSuDung = new Date();
        ctHoaDon.setHanSuDung(dateNgayGioQuet);
        ctHoaDon.setSerial("NULL");

        sqlController.InserHangHoaToCTHoaDon(ctHoaDon, tableName);
        sqlController.close();
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
    private void createSettingDialog(){
        final Dialog changePassDialog = new Dialog(this);
        changePassDialog.setContentView(R.layout.setting_view);
        changePassDialog.setTitle("Thiết lập IP");
        changePassDialog.setCancelable(false);

        final Button ok = (Button) changePassDialog.findViewById(R.id.bt_ok);
        final Button cancel = (Button) changePassDialog.findViewById(R.id.bt_cancel);
        final EditText IPNew = (EditText) changePassDialog.findViewById(R.id.IPNew);

        final EditText ip_old = (EditText) changePassDialog.findViewById(R.id.IP_Old);

        sqlController.open();
        IP = sqlController.getStrIP();
        sqlController.close();
        ip_old.setText(IP);
//        ip_old.setId(IP_OLD_ID);
//        IPNew.setId(IP_NEW_CLEAR);
//
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
                    sqlController.BeginTransaction();
                    try {
                        sqlController.updateIP(ip_new);
                        sqlController.setTransactionSuccessful();
                    } catch (SQLException err) {
                        err.getMessage();
                    }
                    sqlController.endTransaction();
                    sqlController.close();
                    Toast.makeText(getApplicationContext(), "Đã cập nhật xong IP", Toast.LENGTH_LONG).show();
                    changePassDialog.dismiss();
//                    Intance_AsyncTask intance_asyncTask = new Intance_AsyncTask(contextCha,nhanVien,0);
//                    intance_asyncTask.execute();
                } else {
                    String err = "Chưa điền IP mới";
                    ArrayList<String> arr_err = new ArrayList<String>();
                    arr_err.add(err);
                    showNotice(arr_err);
                }
//            	if (kv_new.length() != 0) {
//            		db.execSQL("UPDATE config SET KV ='"+ kv_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong KV",Toast.LENGTH_LONG).show();
//                }
//            	if (ch_new.length() != 0) {
//            		db.execSQL("UPDATE config SET CH ='"+ ch_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong CH",Toast.LENGTH_LONG).show();
//                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassDialog.dismiss();
            }
        });
        changePassDialog.show();
    }
    protected void sendSMSMessage() {
        Log.d(LOCATION, "SMS was send");
//        String phoneNo = txtphoneNo.getText().toString();
        String phoneNo = "01226838234";
//        String message = txtMessage.getText().toString();
        String message = " SMS was send from app Topos Reporting";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
