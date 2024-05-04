package com.example.bloodunityapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Receiver extends AppCompatActivity {

    TextInputEditText name, hname, location;
    private Button submit;
    private Spinner bloodgroup;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Receiver");

    // Array of hospital names
    private String[] hospitals = {"Hospital A", "Hospital B", "Hospital C", "Hospital D", "Hospital E"};

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

        // Get the TextInputEditText fields
        name = findViewById(R.id.editTextName);
        bloodgroup = findViewById(R.id.spinnerBloodType);
        hname = findViewById(R.id.edithname);
        location = findViewById(R.id.editlocation);

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(v -> {
            // Create a map to store the name, hname, and location
            Map<String, String> receiverData = new HashMap<>();
            receiverData.put("name", name.getText().toString());
            receiverData.put("bloodgroup", bloodgroup.getSelectedItem().toString());
            receiverData.put("hname", hname.getText().toString());
            receiverData.put("location", location.getText().toString());

            // Add the data to Firebase Realtime Database
            databaseReference.push().setValue(receiverData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Receiver.this, "Update successful", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(Receiver.this)
                                .setTitle("Registration Successful")
                                .setMessage("You have registered successfully.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Navigate back to MainActivity after OK is clicked
                                        Intent intent = new Intent(Receiver.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.baseline_verified_24)
                                .show();
                        // Check if there is a donor with the same blood group
                        FirebaseDatabase.getInstance().getReference("donors").orderByChild("bloodgroup").equalTo(bloodgroup.getSelectedItem().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Get a random hospital name
                                            String hospital = hospitals[new Random().nextInt(hospitals.length)];
                                            sendNotification("Donor Available", "A donor with your blood group is available. Please go to " + hospital + ".");
                                        } else {
                                            sendNotification("No Donor Available", "No donor with your blood group is currently available.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                    }
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(Receiver.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Get the hospital name from the form
        String hospital = hname.getText().toString();

        // Get the blood group from the form
        String bloodGroup = bloodgroup.getSelectedItem().toString();

        // Add the hospital name and blood group to the notification message
        message += " A donor with blood group " + bloodGroup + " is available. Please go to " + hospital + ".";

        NotificationHistory.addNotification(message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}