package com.example.laptrinhmobile.toposmobile.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.CaBan;
import com.example.laptrinhmobile.toposmobile.R;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 10/5/2015.
 */
public class DSCaBan_ArrayAdapter extends ArrayAdapter <CaBan> {

    Activity context = null;
    ArrayList<CaBan> myArray = null;
    int layoutId;

    public DSCaBan_ArrayAdapter (Activity context, int layoutId, ArrayList<CaBan> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView( int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        final CaBan emp = myArray.get(position);

        final TextView txtdisplaySTT =(TextView)
                convertView.findViewById(R.id.txtViewSTTCaBan);

        txtdisplaySTT.setText(emp.getSTT());

        final TextView txtdisplayName =(TextView)
                convertView.findViewById(R.id.txtViewTenCaBan);

        txtdisplayName.setText(emp.getTenCaBan());

        return convertView;
    }
}
