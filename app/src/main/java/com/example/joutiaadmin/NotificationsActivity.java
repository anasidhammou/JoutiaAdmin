package com.example.joutiaadmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaadmin.Adapter.CustomSpinnerAdapter;
import com.example.joutiaadmin.Adapter.SearchableAdapter;
import com.example.joutiaadmin.Models.Notifications;
import com.example.joutiaadmin.Models.Vendeur;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NotificationsActivity extends AppCompatActivity {

    List<Vendeur> allUserarray = new ArrayList<>();

    DatabaseReference allClient;

    Spinner spinner;

    TextInputEditText DescriptionProd;
    EditText title;

    String titleN, description, nommagasin;

    private DatabaseReference reference;

    private TextView selectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner_vendeur);
        selectedText = findViewById(R.id.selected_text);
        DescriptionProd = findViewById(R.id.DescriptionProd);
        title = findViewById(R.id.title);
        allClient = FirebaseDatabase.getInstance().getReference().child("vendeur");
        reference = FirebaseDatabase.getInstance().getReference("notification");
        getallVendeur();

        // Show dialog when the text view is clicked
        selectedText.setOnClickListener(v -> showSearchableDialog());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'élément sélectionné
                Vendeur selectedItem = (Vendeur) parent.getItemAtPosition(position);
                nommagasin = selectedItem.nommagasin;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Action à effectuer si rien n'est sélectionné (optionnel)
            }
        });
    }

    private void showSearchableDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
        SearchView searchView = dialog.findViewById(R.id.search_view);

        // Initialiser l'adaptateur avec la liste complète
        SearchableAdapter adapter = new SearchableAdapter(allUserarray, item -> {
            selectedText.setText(item.nommagasin);
            nommagasin = item.nommagasin;
            dialog.dismiss();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configurer le SearchView pour filtrer les résultats
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // Appliquer le filtre
                return true;
            }
        });

        // Afficher le dialogue
        dialog.show();
    }

    private void getallVendeur() {
        allUserarray.clear();
        allClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allUserarray.add(postSnapshot.getValue(Vendeur.class));
                }

                initialiseSpinner(allUserarray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiseSpinner(List<Vendeur> allUserarray) {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, allUserarray);
        spinner.setAdapter(adapter);

    }

    public void Send(View view) {

        titleN = title.getText().toString();
        description = DescriptionProd.getText().toString();

        if (titleN.isEmpty() || description.isEmpty() || nommagasin == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsActivity.this);
            builder.setTitle("Merci de remplir tous les champs");
            builder.setItems(new CharSequence[]{"Ok"},
                    (dialog, which) -> {
                    });
            builder.show();
        } else {

            Random random = new Random();
            int randomId = random.nextInt(1000);


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(calendar.getTime());

            Notifications notification = new Notifications(randomId, titleN, description, nommagasin, currentDate, false);
            reference.push().setValue(notification);

            AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsActivity.this);
            builder.setTitle("Notifications bien envoyé");
            builder.setItems(new CharSequence[]{"Ok"},
                    (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.show();
        }

    }
}