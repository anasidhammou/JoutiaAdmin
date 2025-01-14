package com.example.joutiaadmin.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joutiaadmin.Models.Vendeur;
import com.example.joutiaadmin.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<Vendeur> {
    private Context context;
    private List<Vendeur> spinnerItems;

    public CustomSpinnerAdapter(Context context, List<Vendeur> spinnerItems) {
        super(context, 0, spinnerItems);
        this.context = context;
        this.spinnerItems = spinnerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        Vendeur item = spinnerItems.get(position);

        ImageView imageView = convertView.findViewById(R.id.spinner_item_image);
        TextView textView = convertView.findViewById(R.id.spinner_item_text);

        if(item.ArrayImage.isEmpty()){
            imageView.setImageResource(R.drawable.utilisateur);
        }else {
            Bitmap bitmap = base64ToBitmap(item.ArrayImage.get(0));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.utilisateur);
            }
        }
        textView.setText(item.nommagasin);

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
}

