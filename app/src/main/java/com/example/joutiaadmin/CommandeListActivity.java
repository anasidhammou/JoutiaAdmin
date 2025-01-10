package com.example.joutiaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaadmin.Adapter.CustomAdapterGridCommande;
import com.example.joutiaadmin.Adapter.CustomAdapterGridProduit;
import com.example.joutiaadmin.Dialog.CustomCommandeDialog;
import com.example.joutiaadmin.Dialog.CustomUserDialog;
import com.example.joutiaadmin.Models.Commande;
import com.example.joutiaadmin.Models.product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommandeListActivity extends AppCompatActivity {

    List<Commande> allPorductarray = new ArrayList<>();
    List<product> allPorductarrayFiltred = new ArrayList<>();

    DatabaseReference allClient;

    public GridView gridView;

    TextView noPorduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_commande_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allClient = FirebaseDatabase.getInstance().getReference().child("Commande");
        gridView = findViewById(R.id.gridView);
        noPorduct= findViewById(R.id.noproduct);

        getallCommande();

    }

    private void getallCommande() {
        allClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allPorductarray.add(postSnapshot.getValue(Commande.class));
                }

                initialiserecyclerfiltred(allPorductarray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecyclerfiltred(List<Commande> allPorductarray) {
        CustomAdapterGridCommande adapter = new CustomAdapterGridCommande(this, allPorductarray);
        gridView.setAdapter(adapter);
        adapter.setOnDataChangeListener(new CustomAdapterGridCommande.OnDataChangeListener() {
            @Override
            public void showDetail(Object object, List<Commande> allproductarray,Object objects) {
                Intent i = new Intent(CommandeListActivity.this, DetailsCommandeActivity.class);
                i.putExtra("commande",object.toString());
                i.putExtra("nom",objects.toString());
                startActivity(i);
            }
        });
    }

}