package com.example.bloodunityapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    TextInputEditText name, address, bloodGroup, age;
    Button save;
    ImageView profileImage;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DocumentReference documentReference = db.collection("users").document(userId);
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.editTextName);
        address = findViewById(R.id.editTextAddress);
        bloodGroup = findViewById(R.id.editTextBloodGroup);
        age = findViewById(R.id.editTextAge);
        save = findViewById(R.id.buttonSave);
        profileImage = findViewById(R.id.profileImage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        name.setText(documentSnapshot.getString("name"));
                        address.setText(documentSnapshot.getString("address"));
                        bloodGroup.setText(documentSnapshot.getString("bloodGroup"));
                        age.setText(documentSnapshot.getString("age"));

                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null) {
                            Picasso.get().load(profileImageUrl).into(profileImage);
                        }
                    }
                })
                .addOnFailureListener(e -> Snackbar.make(name, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        save.setOnClickListener(v -> {
            String nameText = name.getText().toString();
            String addressText = address.getText().toString();
            String bloodGroupText = bloodGroup.getText().toString();
            String ageText = age.getText().toString();

            Map<String, Object> profile = new HashMap<>();
            profile.put("name", nameText);
            profile.put("address", addressText);
            profile.put("bloodGroup", bloodGroupText);
            profile.put("age", ageText);

            documentReference.set(profile)
                    .addOnSuccessListener(aVoid -> Snackbar.make(v, "Profile saved", Snackbar.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Snackbar.make(v, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            ref.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        documentReference.update("profileImageUrl", uri.toString())
                                                .addOnSuccessListener(aVoid -> Snackbar.make(profileImage, "Image uploaded", Snackbar.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Snackbar.make(profileImage, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                                    })
                                    .addOnFailureListener(e -> Snackbar.make(profileImage, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e -> Snackbar.make(profileImage, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}