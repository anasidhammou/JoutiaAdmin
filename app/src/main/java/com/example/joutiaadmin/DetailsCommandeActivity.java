package com.example.joutiaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaadmin.Adapter.ExpandableListAdapter;
import com.example.joutiaadmin.Models.Commande;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsCommandeActivity extends AppCompatActivity {

    String value,value2;

    TextView nom,phone,prix,Etat;


    ArrayList<Commande> allCommandearray = new ArrayList<>();
    ArrayList<Commande> allCommandearrayFiltred = new ArrayList<>();

    DatabaseReference allCommande;

    ExpandableListView expandableListViewExample;

    ExpandableListAdapter expandableListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_commande);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allCommande = FirebaseDatabase.getInstance().getReference().child("Commande");
        expandableListViewExample=findViewById(R.id.expandableListViewSample);
        nom = findViewById(R.id.nom);
        phone = findViewById(R.id.phone);
        prix = findViewById(R.id.prix);
        Etat = findViewById(R.id.Etat);


        Intent intent = getIntent();
        value = intent.getStringExtra("commande");
        value2 = intent.getStringExtra("nom");
        if (value != null) {
            getallCommande(value,value2);
        }
    }

    private void getallCommande(String value, String value2) {
        allCommandearray.clear();
        allCommandearrayFiltred.clear();
        allCommande.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allCommandearray.add(postSnapshot.getValue(Commande.class));
                }

                for(int i=0;i<allCommandearray.size();i++){
                    if(allCommandearray.get(i).prixcomplet.equals(value) && allCommandearray.get(i).NomClient.equals(value2)){
                        allCommandearrayFiltred.add(allCommandearray.get(i));
                    }
                }
                initialiserecyclerdfilter(allCommandearrayFiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecyclerdfilter(ArrayList<Commande> allCommandearrayFiltred) {

        nom.setText(allCommandearrayFiltred.get(0).NomClient);
        phone.setText(allCommandearrayFiltred.get(0).Phone);
        prix.setText(allCommandearrayFiltred.get(0).prixcomplet);
        if(allCommandearrayFiltred.get(0).Etat.equals("0")){
            Etat.setText("En cours");
        }else if(allCommandearrayFiltred.get(0).Etat.equals("1")){
            Etat.setText("Livrée");
        }else if(allCommandearrayFiltred.get(0).Etat.equals("2")){
            Etat.setText("Refusée");
        }else if(allCommandearrayFiltred.get(0).Etat.equals("3")){
            Etat.setText("Annulée");
        }


        expandableListAdapter  = new ExpandableListAdapter(DetailsCommandeActivity.this, allCommandearrayFiltred);
        expandableListViewExample.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();




    }
}