package com.example.noteapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener, NoteAdapter.ItemClickListener2 {
    FloatingActionButton fAdd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Note> items;
    NoteAdapter noteAdapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    RecyclerView rvNote;
    ImageView imgDelete;
    EditText updateNote;
    EditText updateTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAdd = findViewById(R.id.fAdd);
        updateNote = findViewById(R.id.updateNote);
        rvNote = findViewById(R.id.rvNote);
        imgDelete = findViewById(R.id.deleteNote);

        items = new ArrayList<Note>();
        noteAdapter = new NoteAdapter(this, items, this, this);
        GetAllNotes();
    }

    private void GetAllNotes() {
        db.collection("Note").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("Ayat", "onSuccess: LIST EMPTY");
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            String id = documentSnapshot.getId();
                            String content = documentSnapshot.getString("Content");
                            String title = documentSnapshot.getString("Title");

                            Note e_note = new Note(id, content, title);
                            items.add(e_note);

                            rvNote.setLayoutManager(layoutManager);
                            rvNote.setHasFixedSize(true);
                            rvNote.setAdapter(noteAdapter);

                            noteAdapter.notifyDataSetChanged();
                            Log.e("LogDATA", items.toString());

                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogDATA", "get failed with ");
            }
        });
    }

    public void deleteNote(final Note note) {
        db.collection("Note").document(note.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("Ayat", "deleted");
                        items.remove(note);
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Ayat", "fail");
                    }
                });
    }

    public void updateNote(final Note note) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(note.title);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(customLayout);
        builder.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingInflatedId")
                    public void onClick(DialogInterface dialog, int id) {
                        updateNote = customLayout.findViewById(R.id.updateNote);

                        db.collection("Note").document(note.getId()).update("Content", updateNote.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Ayat", "DocumentSnapshot successfully updated!");
                                        items.clear();
                                        GetAllNotes();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Ayat", "Error updating document", e);
                                    }
                                });
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(int position, String id) {
        deleteNote(items.get(position));
    }

    @Override
    public void onItemClick2(int position, String id) {
        updateNote(items.get(position));

    }

    public void addNote(View v) {
        createNote();
    }

    public void createNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }


}