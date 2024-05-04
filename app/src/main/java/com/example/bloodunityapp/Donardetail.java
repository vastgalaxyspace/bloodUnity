package com.example.bloodunityapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Donardetail extends AppCompatActivity {
    private TextInputEditText name,address,age,weight;
    private Button save;
    private Spinner bloodgroup;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("donors");

    private final String CHANNEL_ID = "channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donardetail);

        name=findViewById(R.id.editTextName);
        address=findViewById(R.id.editaddress);
        age=findViewById(R.id.editage);
        weight=findViewById(R.id.editweigth);
        bloodgroup=findViewById(R.id.spinnerBloodType);
        save=findViewById(R.id.buttonSubmit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Donardetail.this, HospitalDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedataToFirebase();
            }
        });

        createNotificationChannel();
    }

    public void savedataToFirebase(){
        String donorname=name.getText().toString();
        String donoraddress=address.getText().toString();
        String donorage=age.getText().toString();
        String donorweight=weight.getText().toString();
        String donorbloodgroup=bloodgroup.getSelectedItem().toString();

        Donor donor=new Donor(donorname,donoraddress,donorage,donorweight,donorbloodgroup);
        myRef.push().setValue(donor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AlertDialog dialog = new AlertDialog.Builder(Donardetail.this)
                        .setTitle("Registration Successful")
                        .setMessage("Wow!! You saves a life. Thank you for your donation.")
                        .setIcon(R.drawable.baseline_verified_24)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Navigate back to MainActivity after OK is clicked
                                Intent intent = new Intent(Donardetail.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();


                Random random = new Random();
                LocalDateTime now = LocalDateTime.now();
                int randomDays = random.nextInt(30) + 1; // random number between 1 and 30
                int randomHours = random.nextInt(24); // random number between 0 and 23
                LocalDateTime donationDateTime = now.plusDays(randomDays).withHour(randomHours).withMinute(0).withSecond(0).withNano(0);

                // Format the date and time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String donationDateTimeStr = donationDateTime.format(formatter);

                String notificationText = "Your blood donation is scheduled for " + donationDateTimeStr + ".";
                NotificationHistory.addNotification(notificationText);

                // Create a notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(Donardetail.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle("Blood Donation Schedule")
                        .setContentText(notificationText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);



                @SuppressLint("MissingPermission")
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Donardetail.this);
                notificationManager.notify(0, builder.build());


            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}