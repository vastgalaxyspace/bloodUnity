package com.example.bloodunityapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Receiver extends AppCompatActivity {

    TextInputEditText name, hname, location;
    Button submit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Receiver");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> {

            Intent intent = new Intent(Receiver.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to general topic";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to general topic failed";
                        }
                        Toast.makeText(Receiver.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        // Get the TextInputEditText fields
        name = findViewById(R.id.editTextName);
        hname = findViewById(R.id.edithname);
        location = findViewById(R.id.editlocation);

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(v -> {
            // Create a map to store the name, hname, and location
            Map<String, String> receiverData = new HashMap<>();
            receiverData.put("name", name.getText().toString());
            receiverData.put("hname", hname.getText().toString());
            receiverData.put("location", location.getText().toString());

            // Add the data to Firestore
            collectionReference.add(receiverData)
                    .addOnSuccessListener(documentReference -> Toast.makeText(Receiver.this, "Update successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Receiver.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}