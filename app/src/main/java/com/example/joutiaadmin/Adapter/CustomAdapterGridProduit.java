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
import com.example.joutiaadmin.Models.Vendeur;
import com.example.joutiaadmin.Models.product;
import com.example.joutiaadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterGridProduit extends BaseAdapter {

    private Context context;
    private List<product> allproductarray = new ArrayList<>();

    public CustomAdapterGridProduit(Context context, List<product> allproductarrayfiltred) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_produit, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.Nom_user);
            holder.user_details = convertView.findViewById(R.id.user_details);
            holder.image_user = convertView.findViewById(R.id.image_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        product currentProduct = allproductarray.get(position);
        String originalText = currentProduct.Nom.toString();
        if (originalText.length() > 20) {
            String truncatedText = originalText.substring(0, 20) + "...";
            holder.name.setText(truncatedText);
        } else {
            holder.name.setText(originalText);
        }

        if(currentProduct.imageArrayList.get(0).contains("http")){
            Glide.with(context)
                    .load(currentProduct.imageArrayList.get(0))
                    .placeholder(R.drawable.traits)
                    .error(R.drawable.traits)
                    .into( holder.image_user);
        }else{
            Bitmap bitmap = base64ToBitmap(currentProduct.imageArrayList.get(0));
            if (bitmap != null) {
                holder.image_user.setImageBitmap(bitmap);
            } else {
                holder.image_user.setImageResource(R.drawable.traits);
            }
        }

        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.showDetail(allproductarray.get(position).Nom,allproductarray);
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


    CustomAdapterGridProduit.OnDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(CustomAdapterGridProduit.OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {

        void showDetail(Object object, List<product> allproductarray);
    }



}
