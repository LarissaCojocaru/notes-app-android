package com.example.notesapp2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> favoriteNotes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.toolbarFavorites);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favorite");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this, favoriteNotes);
        recyclerView.setAdapter(noteAdapter);

        loadFavoriteNotes();
    }

    private void loadFavoriteNotes() {
        NotesApi api = RetrofitClient.getRetrofitInstance().create(NotesApi.class);

        api.getNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteNotes.clear();

                    for (Note note : response.body()) {
                        // 🔍 Log pentru verificare
                        android.util.Log.d("FAVORITE_CHECK", "Note ID: " + note.getId() + ", Favorite: " + note.isFavorite());

                        if (note.isFavorite()) {
                            favoriteNotes.add(note);
                        }
                    }

                    noteAdapter.notifyDataSetChanged();

                    if (favoriteNotes.isEmpty()) {
                        Toast.makeText(FavoriteActivity.this, "Nu ai notițe favorite.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FavoriteActivity.this, "Eroare la încărcarea notițelor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(FavoriteActivity.this, "Eroare rețea", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
