package com.example.laptrinhmobile.toposmobile;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.laptrinhmobile.toposmobile.Object.ItemMenu;

import java.util.ArrayList;
import java.util.List;

public class ListMenu extends AppCompatActivity {

    private static String[] arrUrl =  { "imgSales", "imgRefund", "imgSpecialOrder", "imgSpecialOrderCancellation" };
    private static String[] arrName = { "Sales", "Refund", "Special Order", "Special Order Cancellation"};
    public ArrayList<ItemMenu> arrListMenu = new ArrayList<ItemMenu>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        for(int i=0;i<arrUrl.length;i++)
        {
            ItemMenu itemMenu = new ItemMenu();
            itemMenu.setImgUrl(arrUrl[i]);
            itemMenu.setName((arrName[i]));
            arrListMenu.add(itemMenu);
        }


    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onStart() { super.onStart(); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_menu, menu);
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
