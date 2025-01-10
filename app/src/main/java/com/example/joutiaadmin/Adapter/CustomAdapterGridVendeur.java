package com.example.joutiaadmin.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joutiaadmin.Models.User;
import com.example.joutiaadmin.Models.Vendeur;
import com.example.joutiaadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterGridVendeur extends BaseAdapter {

    private Context context;
    private List<Vendeur> allproductarray = new ArrayList<>();

    public CustomAdapterGridVendeur(Context context, List<Vendeur> allproductarrayfiltred) {
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
            holder.image_user = convertView.findViewById(R.id.image_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Vendeur currentProduct = allproductarray.get(position);
        holder.name.setText(currentProduct.nommagasin.toString());

        Bitmap bitmap = base64ToBitmap(allproductarray.get(position).ArrayImage.get(0));
        if (bitmap != null) {
            holder.image_user.setImageBitmap(bitmap);
        } else {
            holder.image_user.setImageResource(R.drawable.utilisateur);
        }

        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.showDetail(allproductarray.get(position).nommagasin,allproductarray);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static class ViewHolder {
        TextView name;
        LinearLayout user_details;

        ImageView image_user;
    }


    CustomAdapterGridVendeur.OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(CustomAdapterGridVendeur.OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {

        void showDetail(Object object, List<Vendeur> allproductarray);
    }



}
