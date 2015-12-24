package com.example.laptrinhmobile.toposmobile.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.CaBan;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.R;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 10/5/2015.
 */
public class DSTypePayment_ArrayAdapter extends ArrayAdapter <ThanhToanHoaDon> {

    private static String LOCATION = "DSHangHoa_ArrayAdapter";
    Activity context = null;
    ArrayList<ThanhToanHoaDon> myArray = null;
    int layoutId;

    private static class ViewHolder {
        private TextView txtViewTypeName, txtViewAmount;
    }
    ViewHolder viewHolder;
    public DSTypePayment_ArrayAdapter (Activity context, int layoutId, ArrayList<ThanhToanHoaDon> arr){
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

            viewHolder.txtViewTypeName = (TextView)
                    convertView.findViewById(R.id.txtViewTypeName);


            viewHolder.txtViewAmount = (TextView)
                    convertView.findViewById(R.id.txtViewAmount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ThanhToanHoaDon emp = myArray.get(position);
        SQLController sqlController = new SQLController(context);
        sqlController.open();
        String tenHinhThuc = sqlController.getTenHinhThuc(emp.getMaHinhThuc());
        sqlController.close();
            viewHolder.txtViewTypeName.setText(tenHinhThuc);
            viewHolder.txtViewAmount.setText(""+emp.getThanhTien());

            return convertView;

    }


}
