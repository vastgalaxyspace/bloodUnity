package com.example.bloodunityapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bloodunityapp.authontication.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CardView cardView,cardView1,cardView3,cardView4;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        boolean showWelcomeBack = getIntent().getBooleanExtra("showWelcomeBack", false);
        if (showWelcomeBack) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Welcome");
            alertDialog.setMessage("🎉 Welcome back to BloodUnity");
            alertDialog.show();

            // Hide after 3 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }, 2000);
        }

        toolbar = findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawerlayout);

        navigationView=findViewById(R.id.navigation);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                                           @Override
                                           public void onComplete(@NonNull Task<String> task) {
                                               if(!task.isSuccessful()){
                                                   Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                                                   return;
                                               }

                                               String token=task.getResult();

                                               String msg=getString(R.string.msg_token_fmt, token);
                                               Log.d("FCM", msg);


                                           }
                                       }
                );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        cardView=findViewById(R.id.donate_cardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, DontateActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cardView1=findViewById(R.id.reciver_cardview);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Receiver.class);
                startActivity(intent);
                finish();
            }
        });

        cardView4=findViewById(R.id.notification_cardview);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, NotificationHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });


//        toolbar=findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.opendrawer,R.string.closedrawer);
//        drawerLayout.addDrawerListener(toggle);
//
//        toggle.syncState();
//
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            System.out.println("clickk-------------------------");
            int id=menuItem.getItemId();
            if(id == R.id.nav_home){
                Intent intent= new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }else if(id == R.id.nav_profile){
                Intent intent= new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();

            }else if(id == R.id.nav_about_us){
                Intent intent= new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);
                finish();

            }else if(id == R.id.nav_logout){
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
//
//        final OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
//        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    setEnabled(false);
//                    dispatcher.onBackPressed();
//                }
//            }
//        });
//    }
    }
    @Override
    public void onBackPressed() {
        // Call the superclass method
        super.onBackPressed();
    }


}