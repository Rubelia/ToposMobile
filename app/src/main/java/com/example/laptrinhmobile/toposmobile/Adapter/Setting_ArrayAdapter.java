package com.example.laptrinhmobile.toposmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.Setting_Item;
//import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.R;
//import com.example.laptrinhmobile.toposmobile.database.SQLController;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 12/1/2015.
 */
public class Setting_ArrayAdapter extends ArrayAdapter<Setting_Item> {

    private static String LOCATION = "Setting_ArrayAdapter";
    Context context = null;
    ArrayList<Setting_Item> myArray = null;
    int layoutId;

    private static class ViewHolder {
        private TextView txtViewTitle;
        private ImageView imgIcon;
    }
    ViewHolder viewHolder;
    public Setting_ArrayAdapter (Context context, int layoutId, ArrayList<Setting_Item> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView( int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txtViewTitle = (TextView)
                    convertView.findViewById(R.id.txtViewTitle);


            viewHolder.imgIcon = (ImageView)
                    convertView.findViewById(R.id.imgIcon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Setting_Item temp = myArray.get(position);
        viewHolder.txtViewTitle.setText(temp.getTitle());
        viewHolder.imgIcon.setImageDrawable(Drawable.createFromPath(temp.getImg()));

        return convertView;

    }


}