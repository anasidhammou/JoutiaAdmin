package com.example.joutiaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaadmin.Dialog.CustomAddBanniereDialog;
import com.example.joutiaadmin.Dialog.CustomCommandeDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goUser(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    public void goVendeur(View view) {
        Intent intent = new Intent(this, VendeurListActivity.class);
        startActivity(intent);
    }

    public void goCommande(View view) {
        Intent intent = new Intent(this, CommandeListActivity.class);
        startActivity(intent);
    }

    public void goProduits(View view) {
        Intent intent = new Intent(this, ProduitListActivity.class);
        startActivity(intent);
    }

    public void goBannieres(View view) {
        Intent intent = new Intent(this, BanniersListActivity.class);
        startActivity(intent);
    }

    public void goAddbanniere(View view) {
        CustomAddBanniereDialog customDialog = new CustomAddBanniereDialog(MainActivity.this);
        customDialog.show();
    }

    public void goNotif(View view) {

        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
}