package com.example.joutiaadmin.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaadmin.Models.Vendeur;
import com.example.joutiaadmin.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableAdapter extends RecyclerView.Adapter<SearchableAdapter.ViewHolder> {
    private List<Vendeur> originalList;
    private List<Vendeur> filteredList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Vendeur item);
    }

    public SearchableAdapter(List<Vendeur> list, OnItemClickListener listener) {
        this.originalList = list;
        this.filteredList = new ArrayList<>(list); // Important: Copie de tous les éléments ici
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vendeur item = filteredList.get(position);
        holder.textView.setText(item.nommagasin);
        if(item.ArrayImage.isEmpty()){
            holder.imageView.setImageResource(R.drawable.utilisateur);
        }else {
            Bitmap bitmap = base64ToBitmap(item.ArrayImage.get(0));
            if (bitmap != null) {
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.utilisateur);
            }
        }
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
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

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Vendeur item : originalList) {
                if (item.nommagasin.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.spinner_item_text);
            imageView = itemView.findViewById(R.id.spinner_item_image);
        }
    }
}
