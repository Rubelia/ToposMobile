package com.example.laptrinhmobile.toposmobile.Adapter;

/**
 * Created by LapTrinhMobile on 10/22/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.HinhThucThanhToan;
//import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.R;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgCCITT;

import java.util.ArrayList;

public class DSHinhThucThanhToan_ArrayAdapter extends ArrayAdapter<HinhThucThanhToan> {

    private static String LOCATION = "DSHangHoa_ArrayAdapter";
    Activity context = null;
    ArrayList<HinhThucThanhToan> myArray = null;
    int layoutId;

    private static class ViewHolder {
        private TextView txtViewTenHinhThuc;
        private ImageView imgViewChoose;
    }
    ViewHolder viewHolder;
    public DSHinhThucThanhToan_ArrayAdapter (Activity context, int layoutId, ArrayList<HinhThucThanhToan> arr){
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

//            viewHolder.imgBtnChoose = (ImageButton)
//                    convertView.findViewById(R.id.imgBtnChoose);

            viewHolder.imgViewChoose = (ImageView)
                    convertView.findViewById(R.id.imgViewChoose);

            viewHolder.txtViewTenHinhThuc = (TextView)
                    convertView.findViewById(R.id.txtViewTenHinhThuc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HinhThucThanhToan emp = myArray.get(position);

//        viewHolder.imgBtnChoose.setText(emp.getType());
//        viewHolder.imgBtnChoose.setBackgroundResource(R.drawable.dog_logo_small);
        viewHolder.txtViewTenHinhThuc.setText(""+emp.getTenHinhThuc());

        return convertView;

    }


}

