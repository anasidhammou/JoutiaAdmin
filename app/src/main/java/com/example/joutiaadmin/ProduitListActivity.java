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

import com.example.joutiaadmin.Adapter.CustomAdapterGrid;
import com.example.joutiaadmin.Adapter.CustomAdapterGridProduit;
import com.example.joutiaadmin.Dialog.CustomCommandeDialog;
import com.example.joutiaadmin.Dialog.CustomUserDialog;
import com.example.joutiaadmin.Models.User;
import com.example.joutiaadmin.Models.product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProduitListActivity extends AppCompatActivity {

    List<product> allPorductarray = new ArrayList<>();
    List<product> allPorductarrayFiltred = new ArrayList<>();

    DatabaseReference allClient;

    public GridView gridView;

    TextView noPorduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_produit_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allClient = FirebaseDatabase.getInstance().getReference().child("product");
        gridView = findViewById(R.id.gridView);
        noPorduct= findViewById(R.id.noproduct);

        Intent intent = getIntent();
        String value = intent.getStringExtra("vendeur");
        if (value != null) {
            getallProduct(value);
        }



    }

    private void getallProduct(String value) {
        allPorductarray.clear();
        allClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allPorductarray.add(postSnapshot.getValue(product.class));
                }

                for(int i=0;i<allPorductarray.size();i++){
                    if(allPorductarray.get(i).NomMagasin.equals(value) && allPorductarray.get(i).Approuved.equals(true)
                            && allPorductarray.get(i).State.equals("0")){
                       allPorductarrayFiltred.add(allPorductarray.get(i));
                    }
                }

                if(allPorductarrayFiltred.size()==0){
                    noPorduct.setVisibility(View.VISIBLE);
                }else{
                    noPorduct.setVisibility(View.GONE);
                    initialiserecyclerdfilter(allPorductarrayFiltred);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecyclerdfilter(List<product> allPorductarrayFiltred) {
        CustomAdapterGridProduit adapter = new CustomAdapterGridProduit(this, allPorductarrayFiltred);
        gridView.setAdapter(adapter);
        adapter.setOnDataChangeListener(new CustomAdapterGridProduit.OnDataChangeListener() {
            @Override
            public void showDetail(Object object, List<product> allproductarray) {
                Intent i = new Intent(ProduitListActivity.this, DetailsProduitActivity.class);
                i.putExtra("vendeur",object.toString());
                startActivity(i);
            }
        });
    }

    public void ShowDetails(View view) {
        CustomCommandeDialog customDialog = new CustomCommandeDialog(ProduitListActivity.this);
        customDialog.show();
    }
}