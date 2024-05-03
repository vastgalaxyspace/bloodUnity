package com.example.bloodunityapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DontateActivity extends AppCompatActivity {
    ArrayList<Donatemodel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dontate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DontateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list.add(new Donatemodel(R.drawable.img1, "Fortis Hospital", "O+", "Delhi"));
        list.add(new Donatemodel(R.drawable.img2, "Max Healthcare", "B+", "Mumbai"));
        list.add(new Donatemodel(R.drawable.img3, "AIIMS", "AB+", "Kolkata"));
        list.add(new Donatemodel(R.drawable.img4, "Narayana Health", "A-", "Chennai"));
        list.add(new Donatemodel(R.drawable.img5, "Manipal Hospitals", "B-", "Hyderabad"));
        list.add(new Donatemodel(R.drawable.img6, "Columbia Asia", "O-", "Pune"));
        list.add(new Donatemodel(R.drawable.img7, "Medanta", "AB-", "Gurgaon"));
        list.add(new Donatemodel(R.drawable.img8, "Apollo Hospital", "A+", "Bangalore"));
        list.add(new Donatemodel(R.drawable.img9, "Fortis Hospital", "B+", "Delhi"));
        list.add(new Donatemodel(R.drawable.img10, "Max Healthcare", "O+", "Mumbai"));
        list.add(new Donatemodel(R.drawable.img11, "AIIMS", "A-", "Kolkata"));
        list.add(new Donatemodel(R.drawable.img12, "Narayana Health", "AB+", "Chennai"));


        Hospital1Adapter adapter = new Hospital1Adapter(list);
        recyclerView.setAdapter(adapter);



        // Set the OnItemClickListener for the adapter
        adapter.setOnItemClickListener(new Hospital1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Donatemodel item) {
                Intent intent = new Intent(DontateActivity.this, HospitalDetailActivity.class);
                intent.putExtra("hospitalName", item.getHname());
                intent.putExtra("hospitalAddress", item.getLocation());
                intent.putExtra("hospitalImage", item. getImage());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);


    }
}