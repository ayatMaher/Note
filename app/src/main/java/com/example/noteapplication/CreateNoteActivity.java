package com.example.noteapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {
    EditText editContent;
    EditText editTitle;
    Button btnSave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        editContent = findViewById(R.id.edContent);
        editTitle = findViewById(R.id.edTitle);
        btnSave = findViewById(R.id.btn_save);

    }

    public void saveToFirebase(View view) {
        String content = editContent.getText().toString();
        String title = editTitle.getText().toString();
        Map<String, Object> noteText = new HashMap<>();
        if (!content.isEmpty() && !title.isEmpty()) {
            noteText.put("Content", content);
            noteText.put("Title", title);
            db.collection("Note")
                    .add(noteText)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("TAG", "Data added successfully to database");

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Failed to add database");

                        }
                    });

        } else {
            Toast.makeText(this, "Please Fill fields", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}