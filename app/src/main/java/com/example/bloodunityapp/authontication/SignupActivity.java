package com.example.bloodunityapp.authontication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bloodunityapp.MainActivity;
import com.example.bloodunityapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private Button registerbtn;
    private EditText emailreg, phonereg, passreg, cpassreg;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailreg = findViewById(R.id.emailregister);
        phonereg = findViewById(R.id.phoneregister);
        passreg = findViewById(R.id.passwordregister);
        cpassreg = findViewById(R.id.confirmpasswordregister);
        registerbtn = findViewById(R.id.registerbtn11);
        progressBar = findViewById(R.id.progressBar1);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser = firebaseAuth.getCurrentUser();

                if (currentuser != null) {
                    //user already logged in

                } else {
                    //not user it
                }
            }
        };

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emailreg.getText().toString()) &&
                        !TextUtils.isEmpty(passreg.getText().toString()) &&
                        !TextUtils.isEmpty(cpassreg.getText().toString()) &&
                        !TextUtils.isEmpty(phonereg.getText().toString())) {

                    String email = emailreg.getText().toString().trim();
                    String phone = phonereg.getText().toString().trim();
                    String password = passreg.getText().toString().trim();
                    String cpassword = cpassreg.getText().toString().trim();

                    if (password.equals(cpassword)) {
                        createuseremailaccount(email, password, phone);
                    } else {
                        Toast.makeText(SignupActivity.this, "password do not match", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(SignupActivity.this, "Empty fields not allowed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createuseremailaccount(String email, String password, String phone) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phone)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentuser = firebaseAuth.getCurrentUser();
                                assert currentuser != null;
                                final String currentuserid = currentuser.getUid();

                                // Get the FCM token
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w("my", "Fetching FCM registration token failed", task.getException());
                                                    return;
                                                }

                                                // Get new FCM registration token
                                                String token = task.getResult();

                                                Map<String, String> userobj = new HashMap<>();
                                                userobj.put("UserId", currentuserid);
                                                userobj.put("phone", phone);
                                                userobj.put("fcmToken", token);  // Add the FCM token to the map

                                                collectionReference.add(userobj)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                documentReference.get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (Objects.requireNonNull(task.getResult()).exists()) {
                                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                    String name = task.getResult().getString("phone");
                                                                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                                                    intent.putExtra("phone", phone);
                                                                                    intent.putExtra("userid", currentuserid);
                                                                                    startActivity(intent);
                                                                                } else {
                                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(SignupActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignupActivity.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Registration error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}