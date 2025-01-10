package com.example.joutiaadmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.joutiaadmin.Adapter.ViewPagerAdapter;
import com.example.joutiaadmin.Models.product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsProduitActivity extends AppCompatActivity {

    TextView nom, description, categorie, prix, nommagasin, phone;

    List<product> allPorductarray = new ArrayList<>();
    List<product> allPorductarrayFiltred = new ArrayList<>();

    DatabaseReference allClient;

    ViewPager2 viewPager;

    String value, nomMag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_produit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allClient = FirebaseDatabase.getInstance().getReference().child("product");

        nom = findViewById(R.id.nom);
        description = findViewById(R.id.description);
        categorie = findViewById(R.id.categorie);
        prix = findViewById(R.id.prix);
        nommagasin = findViewById(R.id.nommagasin);
        phone = findViewById(R.id.phone);
        viewPager = findViewById(R.id.viewPager);

        Intent intent = getIntent();
        value = intent.getStringExtra("vendeur");
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
                    if(allPorductarray.get(i).Nom.equals(value)){
                        allPorductarrayFiltred.add(allPorductarray.get(i));
                    }
                }
                initialiserecyclerdfilter(allPorductarrayFiltred);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiserecyclerdfilter(List<product> allPorductarrayFiltred) {
        nom.setText(allPorductarrayFiltred.get(0).Nom.toString());
        description.setText(allPorductarrayFiltred.get(0).Description.toString());
        categorie.setText(allPorductarrayFiltred.get(0).Catégorie.toString());
        prix.setText(allPorductarrayFiltred.get(0).Montant_TTC.toString()+" "+"Dhs");
        nommagasin.setText(allPorductarrayFiltred.get(0).NomMagasin.toString());
        phone.setText(allPorductarrayFiltred.get(0).phone.toString());
        nomMag=allPorductarrayFiltred.get(0).NomMagasin.toString();

        List<String> productImages = allPorductarrayFiltred.get(0).imageArrayList;
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getApplicationContext(), productImages);
        viewPager.setAdapter(pagerAdapter);
    }


    public void onApprouve(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Etes vous sur de vouloir Approuver cet article ?");
        builder.setItems(new CharSequence[]{"Oui", "Non"},
                (dialog, which) -> {
                    if (which == 0) {
                        Approveit();
                    }
                });
        builder.show();

    }

    private void Approveit() {
        allClient.orderByChild("Nom").equalTo(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer la clé de l'utilisateur
                    String userId = userSnapshot.getKey();

                    // Créer une mise à jour
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("State", "1");

                    // Appliquer la mise à jour
                    allClient.child(userId).updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                // Mise à jour réussie
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProduitActivity.this);
                                builder.setTitle("Cette article a été Approuvé");
                                builder.setItems(new CharSequence[]{"OK"},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                Intent i = new Intent(DetailsProduitActivity.this, ProduitListActivity.class);
                                                i.putExtra("vendeur",nomMag);
                                                startActivity(i);
                                            }
                                        });
                                builder.show();
                            })
                            .addOnFailureListener(e -> {
                                // Erreur lors de la mise à jour
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProduitActivity.this);
                                builder.setTitle("Une Erreur est survenue");
                                builder.setItems(new CharSequence[]{"OK"},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                Intent i = new Intent(DetailsProduitActivity.this, ProduitListActivity.class);
                                                i.putExtra("vendeur",nomMag);
                                                startActivity(i);
                                            }
                                        });
                                builder.show();
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs
                Log.e("Firebase", "Erreur : " + databaseError.getMessage());
            }
        });

    }

    public void onRejette(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Etes vous sur de vouloir rejeter cet article ?");
        builder.setItems(new CharSequence[]{"Oui", "Non"},
                (dialog, which) -> {
                    if (which == 0) {
                        RejetIt();
                    }
                });
        builder.show();
    }

    private void RejetIt() {
        allClient.orderByChild("Nom").equalTo(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer la clé de l'utilisateur
                    String userId = userSnapshot.getKey();

                    // Créer une mise à jour
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("State", "2");

                    // Appliquer la mise à jour
                    allClient.child(userId).updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                // Mise à jour réussie
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProduitActivity.this);
                                builder.setTitle("Cette article a été Approuvé");
                                builder.setItems(new CharSequence[]{"OK"},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                Intent i = new Intent(DetailsProduitActivity.this, ProduitListActivity.class);
                                                i.putExtra("vendeur",nomMag);
                                                startActivity(i);
                                            }
                                        });
                                builder.show();
                            })
                            .addOnFailureListener(e -> {
                                // Erreur lors de la mise à jour
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProduitActivity.this);
                                builder.setTitle("Une Erreur est survenue");
                                builder.setItems(new CharSequence[]{"OK"},
                                        (dialog, which) -> {
                                            if (which == 0) {
                                                Intent i = new Intent(DetailsProduitActivity.this, ProduitListActivity.class);
                                                i.putExtra("vendeur",nomMag);
                                                startActivity(i);
                                            }
                                        });
                                builder.show();
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs
                Log.e("Firebase", "Erreur : " + databaseError.getMessage());
            }
        });
    }
}