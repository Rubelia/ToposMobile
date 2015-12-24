package com.example.laptrinhmobile.toposmobile.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.laptrinhmobile.toposmobile.Object.HangHoa_HinhAnh;
import com.example.laptrinhmobile.toposmobile.R;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 10/5/2015.
 */
public class DSHangHoa_ArrayAdapter extends ArrayAdapter <HangHoa> {

    private static String LOCATION = "DSHangHoa_ArrayAdapter";
    Activity context = null;
    ArrayList<HangHoa> myArray = null;
    int layoutId;
    SQLController sqlController;

    private static class ViewHolder {
        private TextView txtdisplayMaHH, txtdisplayTenHH, txtdisplayGiaBan, txtdisplayDiscount, txtdisplayQuantity, txtdisplayTotal;
        private ImageView imgViewHangHoa;
    }
    ViewHolder viewHolder;
    public DSHangHoa_ArrayAdapter (Activity context, int layoutId, ArrayList<HangHoa> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
        this.sqlController = new SQLController(context);
    }

    public View getView( int position, View convertView, ViewGroup parent){
        HangHoa emp ;
        switch (layoutId) {
            case R.layout.item_hanghoa:
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext())
                            .inflate(layoutId, parent, false);
                    viewHolder = new ViewHolder();

                    viewHolder.txtdisplayMaHH = (TextView)
                            convertView.findViewById(R.id.txtViewMaHH);

                    viewHolder.txtdisplayTenHH = (TextView)
                            convertView.findViewById(R.id.txtViewTenHH);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                emp = myArray.get(position);

                viewHolder.txtdisplayMaHH.setText(emp.getMaHH());
                viewHolder.txtdisplayTenHH.setText(emp.getTenHH());
                break;
            case R.layout.item_hanghoa_in_sales:
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext())
                            .inflate(layoutId, parent, false);
                    viewHolder = new ViewHolder();

                    viewHolder.txtdisplayMaHH = (TextView)
                            convertView.findViewById(R.id.txtViewMaHH);

                    viewHolder.txtdisplayTenHH = (TextView)
                            convertView.findViewById(R.id.txtViewTenHH);

                    viewHolder.txtdisplayGiaBan = (TextView)
                            convertView.findViewById(R.id.txtViewGiaBan);

                    viewHolder.txtdisplayDiscount = (TextView)
                            convertView.findViewById(R.id.txtViewDiscount);

                    viewHolder.txtdisplayQuantity = (TextView)
                            convertView.findViewById(R.id.txtViewQuantity);

                    viewHolder.txtdisplayTotal = (TextView)
                            convertView.findViewById(R.id.txtViewTotal);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                emp = myArray.get(position);

                viewHolder.txtdisplayMaHH.setText(emp.getMaHH());
                viewHolder.txtdisplayTenHH.setText(emp.getTenHH());
                viewHolder.txtdisplayGiaBan.setText("Giá bán: " + emp.getTienBan());
                viewHolder.txtdisplayDiscount.setText("Thuế: " +emp.getVATDauRa() );
                viewHolder.txtdisplayQuantity.setText("Số lượng: " + emp.getQuantity());
                viewHolder.txtdisplayTotal.setText("Thành Tiền: " + calTotal(emp));
                break;
            case R.layout.item_hanghoa_catalog:
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext())
                            .inflate(layoutId, parent, false);
                    viewHolder = new ViewHolder();

                    viewHolder.txtdisplayMaHH = (TextView)
                            convertView.findViewById(R.id.txtViewMaHH);

                    viewHolder.txtdisplayTenHH = (TextView)
                            convertView.findViewById(R.id.txtViewTenHH);

                    viewHolder.txtdisplayGiaBan = (TextView)
                            convertView.findViewById(R.id.txtViewGiaBan);

                    viewHolder.imgViewHangHoa = (ImageView)
                            convertView.findViewById(R.id.imgViewHangHoa);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                emp = myArray.get(position);

                viewHolder.txtdisplayMaHH.setText(emp.getMaHH());
                viewHolder.txtdisplayTenHH.setText(emp.getTenHH());
                viewHolder.txtdisplayGiaBan.setText(String.valueOf(emp.getTienBan()));

                sqlController.open();
//                HangHoa_HinhAnh hangHoa_hinhAnh = new HangHoa_HinhAnh(sqlController.)
                byte[] image = sqlController.getHinhAnh(emp.getHangHoaID());
                Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                viewHolder.imgViewHangHoa.setImageBitmap(bitmap);
                sqlController.close();
                break;
            default:
                Log.d(LOCATION, "Khong ton tai layout");
                break;
        }
        return convertView;
    }

    public double calTotal(HangHoa in_HangHoa) {
        return  in_HangHoa.getQuantity() * in_HangHoa.getTienBan();
        //        Log.d(LOCATION,"Tổng tiền: " + result);
//         PreTaxMoney + PreTaxMoney * in_HangHoa.getVATDauRa() /100;
    }

}
