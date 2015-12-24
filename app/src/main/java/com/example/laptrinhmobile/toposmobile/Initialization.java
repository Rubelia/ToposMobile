package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.AsyncTask.Initialization_AsyncTask;

public class Initialization extends AppCompatActivity {

    Activity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        WindowManager windowManager = (WindowManager)context.getSystemService( Context.WINDOW_SERVICE );
        Display display = windowManager.getDefaultDisplay();

//        Now we can check for the rotation and also get the screen size:

//        int rotation = display.getRotation();
//        Point size = new Point();
//        display.getSize( size );

//        And finally, perform a check on the rotation, followed by a check on the screen size, to determine what kind of hardware we are running, be it a Tablet or a Phone

//        if( Surface.ROTATION_0 == rotation || Surface.ROTATION_180 == rotation ) {
//            if( size.x > size.y ) {
//                // This is a Tablet and it is in Landscape orientation
//                Toast.makeText(getApplicationContext(),"This is Tablet",Toast.LENGTH_LONG).show();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            } else {
//                // This is a Phone and it is in Portrait orientation
//                Toast.makeText(getApplicationContext(),"This is phone",Toast.LENGTH_LONG).show();
//            }
//        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        settingApp();
    }

    private void settingApp() {
        Initialization_AsyncTask mytt = new Initialization_AsyncTask(context,"");
        mytt.execute();
    }

}
