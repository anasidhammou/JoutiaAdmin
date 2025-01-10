package com.example.joutiaadmin;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joutiaadmin.Adapter.CustomAdapterGrid;
import com.example.joutiaadmin.Dialog.CustomUserDialog;
import com.example.joutiaadmin.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    List<User> allUserarray = new ArrayList<>();

    DatabaseReference allClient;

    public GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridView = findViewById(R.id.gridView);
        allClient = FirebaseDatabase.getInstance().getReference().child("client");
        getallUser();
    }


    private void getallUser() {
        allUserarray.clear();
        allClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    allUserarray.add(postSnapshot.getValue(User.class));
                }

                initialiserecyclerdfilter(allUserarray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialiserecyclerdfilter(List<User> allUserarray) {
        CustomAdapterGrid adapter = new CustomAdapterGrid(this, allUserarray);
        gridView.setAdapter(adapter);
        adapter.setOnDataChangeListener(new CustomAdapterGrid.OnDataChangeListener() {
            @Override
            public void showDetail(Object object, List<User> allproductarray) {
                CustomUserDialog customDialog = new CustomUserDialog(UserListActivity.this, object, allproductarray);
                customDialog.show();
            }
        });

    }





}