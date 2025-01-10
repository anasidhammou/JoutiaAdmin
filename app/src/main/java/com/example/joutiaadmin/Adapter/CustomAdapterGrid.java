package com.example.joutiaadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joutiaadmin.Models.User;
import com.example.joutiaadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterGrid extends BaseAdapter {

    private Context context;
    private List<User> allproductarray = new ArrayList<>();

    public CustomAdapterGrid(Context context, List<User> allproductarrayfiltred) {
        this.context = context;
        this.allproductarray = allproductarrayfiltred;
    }


    @Override
    public int getCount() {
        return allproductarray.size();
    }

    @Override
    public Object getItem(int position) {
        return allproductarray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.Nom_user);
            holder.user_details = convertView.findViewById(R.id.user_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User currentProduct = allproductarray.get(position);
        holder.name.setText(currentProduct.nomcomplet.toString());

        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.showDetail(allproductarray.get(position).nomcomplet,allproductarray);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    private static class ViewHolder {
        TextView name;
        LinearLayout user_details;
    }


    CustomAdapterGrid.OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(CustomAdapterGrid.OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {

        void showDetail(Object object, List<User> allproductarray);
    }



}
