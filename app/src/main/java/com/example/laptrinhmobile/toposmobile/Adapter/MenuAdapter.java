package com.example.laptrinhmobile.toposmobile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptrinhmobile.toposmobile.Object.ItemMenu;
import com.example.laptrinhmobile.toposmobile.R;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 9/29/2015.
 */
public class MenuAdapter extends ArrayAdapter<ItemMenu> {
    private static class ViewHolder {
        private ImageView imgUrl;
        private TextView txtName;
        private ImageButton imgBtnNext;
    }
    ViewHolder viewHolder;
    public MenuAdapter (Context context, int resource, ArrayList<ItemMenu> arrListMenu )
    {
        super(context,resource,arrListMenu);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_menu, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgUrl = (ImageView) convertView.findViewById(R.id.imgIconMenu);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textViewMenuName);
            viewHolder.imgBtnNext = (ImageButton) convertView.findViewById(R.id.imgBtnNext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemMenu item = getItem(position);
        if(item != null)
            Log.d("MenuAdapter","This is item at: " + position + "with name: " + item.getName());
        return convertView;
    }

}
