package com.example.bloodunityapp.authontication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn,signupBtn;
    private EditText emaillogin,passwordlogin;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn = findViewById(R.id.login_button);
        signupBtn = findViewById(R.id.signup_button);
        progressBar=findViewById(R.id.progressBar);
        emaillogin=findViewById(R.id.email_text);
        passwordlogin=findViewById(R.id.password_edit_textview);
        auth = FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!TextUtils.isEmpty(emaillogin.getText().toString())) && (!TextUtils.isEmpty(passwordlogin.getText().toString()))){
                    String email=emaillogin.getText().toString().trim();
                    String password=passwordlogin.getText().toString().trim();

                    loginuser(email,password);

                }else{
                    Toast.makeText(LoginActivity.this,"please enter Email or Password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginuser(String email,String password){
        if((!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(password))){
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user=FirebaseAuth.getInstance().getCurrentUser();
                                String currentuserid=user.getUid();

                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("userid",currentuserid);
                                intent.putExtra("showWelcomeBack", true);
                                startActivity(intent);
                                System.out.println("reached-----------------------------------------------");
                                finish();
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"Authontication Failed: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if (user != null) {
            // User is already logged in, navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}