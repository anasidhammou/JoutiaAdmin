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

import com.bumptech.glide.Glide;
import com.example.joutiaadmin.Models.Commande;
import com.example.joutiaadmin.Models.product;
import com.example.joutiaadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterGridCommande extends BaseAdapter {

    private Context context;
    private List<Commande> allproductarray = new ArrayList<>();

    public CustomAdapterGridCommande(Context context, List<Commande> allproductarrayfiltred) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_commande, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.Nom_user);
            holder.prix = convertView.findViewById(R.id.prix_user);
            holder.user_details = convertView.findViewById(R.id.user_details);
            holder.image_user = convertView.findViewById(R.id.image_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Commande currentProduct = allproductarray.get(position);
        holder.name.setText(currentProduct.NomClient.toString());
        holder.prix.setText(currentProduct.prixcomplet.toString());



        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.showDetail(allproductarray.get(position).prixcomplet,allproductarray,allproductarray.get(position).NomClient);
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
        TextView name,prix;
        LinearLayout user_details;

        ImageView image_user;
    }


    CustomAdapterGridCommande.OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(CustomAdapterGridCommande.OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {

        void showDetail(Object object, List<Commande> allproductarray,Object objects);
    }



}
