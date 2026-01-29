package com.example.notesapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notițele Mele");

        // 🔍 Căutare
        CardView searchCard = findViewById(R.id.cardSearch);
        if (searchCard != null) {
            searchCard.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("notes", new ArrayList<>(noteList));
                startActivity(intent);
            });
        }

        // 🔄 Inițializare RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setAdapter(noteAdapter);

        // 🌸 Buton pentru adăugare notiță
        ImageButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);
        });

        reloadNotes();
    }

    private void reloadNotes() {
        NotesApi api = RetrofitClient.getRetrofitInstance().create(NotesApi.class);
        api.getNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noteList.clear();
                    noteList.addAll(response.body());
                    noteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Eroare la încărcare", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_theme) {
            showThemeDialog();
            return true;
        } else if (id == R.id.action_favorite) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showThemeDialog() {
        String[] themes = {"Luminoasă", "Întunecată", "Automată"};
        int checkedItem = 2;

        new AlertDialog.Builder(this)
                .setTitle("Alege tema")
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case 1:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton("Anulează", null)
                .show();
    }
}
