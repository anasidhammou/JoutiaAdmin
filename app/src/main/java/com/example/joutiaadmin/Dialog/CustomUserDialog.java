package com.example.joutiaadmin.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.joutiaadmin.Models.User;
import com.example.joutiaadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomUserDialog  extends Dialog {

    TextView nomComplet,mail,phone,adresse,ville,categorie;
    Button delete;
    public CustomUserDialog(Context context, Object object, List<User> allproductarray) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        nomComplet = findViewById(R.id.nomComplet);
        mail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        adresse = findViewById(R.id.adresse);
        ville = findViewById(R.id.ville);
        categorie = findViewById(R.id.categorie);

        delete = findViewById(R.id.onDelete);


        ShowAllInfo(object,allproductarray);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Etes vous sur de vouloir supprimer cet utilisateur ?");
                builder.setItems(new CharSequence[]{"Oui", "Non"},
                        (dialog, which) -> {
                            if (which == 0) {
                                DeleteHere();
                            }
                        });
                builder.show();
            }
        });


    }


    private void DeleteHere() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("client");

        // Récupérer l'email de l'utilisateur
        String emailToDelete = mail.getText().toString().trim();

        // Vérifier si l'utilisateur existe dans Firebase Authentication
        auth.fetchSignInMethodsForEmail(emailToDelete)
                .addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful() && authTask.getResult() != null) {
                        List<String> signInMethods = authTask.getResult().getSignInMethods();
                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            Log.d("Firebase", "Email trouvé dans Firebase Authentication.");

                            // Supprimer l'utilisateur dans la Realtime Database
                            databaseReference.orderByChild("mail").equalTo(emailToDelete)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // Supprimer chaque enregistrement correspondant
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    snapshot.getRef().removeValue()
                                                            .addOnCompleteListener(dbTask -> {
                                                                if (dbTask.isSuccessful()) {
                                                                    Log.d("Firebase", "Utilisateur supprimé de la base de données.");
                                                                } else {
                                                                    Log.e("Firebase", "Erreur lors de la suppression dans la base de données.", dbTask.getException());
                                                                }
                                                            });
                                                }

                                                // Supprimer l'utilisateur de Firebase Authentication
                                                FirebaseUser currentUser = auth.getCurrentUser();
                                                if (currentUser != null && currentUser.getEmail().equals(emailToDelete)) {
                                                    currentUser.delete()
                                                            .addOnCompleteListener(deleteTask -> {
                                                                if (deleteTask.isSuccessful()) {
                                                                    Log.d("Firebase", "Utilisateur supprimé de Firebase Authentication.");
                                                                } else {
                                                                    Log.e("Firebase", "Erreur lors de la suppression dans Firebase Authentication.", deleteTask.getException());
                                                                }
                                                            });
                                                } else {
                                                    Log.e("Firebase", "Impossible de supprimer : utilisateur non connecté.");
                                                }
                                            } else {
                                                Log.e("Firebase", "Utilisateur non trouvé dans la base de données.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("Firebase", "Erreur de base de données : " + databaseError.getMessage());
                                        }
                                    });
                        } else {
                            Log.e("Firebase", "Aucune méthode de connexion trouvée pour cet email dans Firebase Authentication.");
                        }
                    } else {
                        Log.e("Firebase", "Erreur lors de la recherche de l'email dans Firebase Authentication.", authTask.getException());
                    }
                });
    }


    private void ShowAllInfo(Object object, List<User> allproductarray) {
        for(int i=0;i<allproductarray.size();i++){
            if(allproductarray.get(i).nomcomplet.equals(object)){
                nomComplet.setText(allproductarray.get(i).nomcomplet);
                mail.setText(allproductarray.get(i).mail);
                phone.setText("Téléphone : "+allproductarray.get(i).phone);
                adresse.setText("Adresse : "+allproductarray.get(i).adresselivraison);
                ville.setText("Ville : "+allproductarray.get(i).ville);
                categorie.setText(allproductarray.get(i).categories);
            }
        }
    }


}
