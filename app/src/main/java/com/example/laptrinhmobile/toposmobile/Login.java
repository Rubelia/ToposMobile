package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.AsyncTask.NhanVien_AsyncTask;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.util.ArrayList;

public class Login extends Activity {
    Activity activity = this;

    public ArrayList<String> arr_err;
    public static String LOCATION = "Login";
    private static String err ;
    String IP;
    public Button btnLogin;
    public String demo_name, demo_password;
    public EditText editUserName, editPassword;
    ImageButton imgBtnSetup;
    SQLController sqlController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        sqlController = new SQLController(getApplicationContext());
//        sqlController.instance();
        sqlController.open();
        IP = sqlController.getStrIP();
        sqlController.close();

        imgBtnSetup = (ImageButton) findViewById(R.id.imgBtnSetup);
        imgBtnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_view = new Intent(getApplication(),Setting.class);
                startActivity(setting_view);
            }
        });
        editUserName = (EditText) findViewById(R.id.editUserName);
        editUserName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (editUserName.getText().toString().isEmpty()) {
                        String err = "Bạn chưa nhập Tên đăng nhập";
                        arr_err.add(err);
                        showNotice(arr_err);
                    } else {
                        NhanVien_AsyncTask nhanVien_asyncTask = new NhanVien_AsyncTask(activity, editUserName.getText().toString(), IP);
                        nhanVien_asyncTask.execute();
                    }
                    return true;
                } else return false;
            }
        });
//        editPassword = (EditText) findViewById(R.id.editPassword);


    }
    @Override
    public void onResume() {
        super.onResume();

        demo_name = "admin";
        demo_password = "123456";
        editUserName = (EditText) findViewById(R.id.editUserName);
        editUserName.setText(demo_name);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
