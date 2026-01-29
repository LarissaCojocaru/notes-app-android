package com.example.notesapp2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Note> allNotes = new ArrayList<>();
    private List<Note> filteredNotes = new ArrayList<>();
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Toolbar cu titlu și buton de back
        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Căutare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inițializare RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this, filteredNotes);
        recyclerView.setAdapter(noteAdapter);

        // Obține lista de notițe transmisă din MainActivity
        if (getIntent() != null && getIntent().hasExtra("notes")) {
            allNotes = (List<Note>) getIntent().getSerializableExtra("notes");
            filteredNotes.addAll(allNotes); // inițial le afișăm pe toate
            noteAdapter.notifyDataSetChanged();
        }

        // Funcționalitate de căutare
        EditText searchInput = findViewById(R.id.editTextSearch);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterNotes(String query) {
        filteredNotes.clear();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        noteAdapter.notifyDataSetChanged();
    }
}
