package com.example.notesapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditNoteActivity extends AppCompatActivity {

    private String selectedColor = "note_yellow";
    private View[] colorViews;

    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextContent = findViewById(R.id.editTextContent);
        Button buttonSave = findViewById(R.id.buttonSave);

        View colorYellow = findViewById(R.id.colorYellow);
        View colorBlue = findViewById(R.id.colorBlue);
        View colorGreen = findViewById(R.id.colorGreen);
        View colorRed = findViewById(R.id.colorRed);
        View colorOrange = findViewById(R.id.colorOrange);
        View colorPurple = findViewById(R.id.colorPurple);

        colorViews = new View[]{
                colorYellow, colorBlue, colorGreen,
                colorRed, colorOrange, colorPurple
        };

        Intent intent = getIntent();
        noteId = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String color = intent.getStringExtra("color");

        if (title != null) editTextTitle.setText(title);
        if (content != null) editTextContent.setText(content);
        if (color != null) {
            selectedColor = color;
            int viewId = getColorViewId(color);
            if (viewId != -1) highlightSelected(findViewById(viewId));
        } else {
            highlightSelected(colorYellow);
        }

        colorYellow.setOnClickListener(v -> { selectedColor = "note_yellow"; highlightSelected(colorYellow); });
        colorBlue.setOnClickListener(v -> { selectedColor = "note_blue"; highlightSelected(colorBlue); });
        colorGreen.setOnClickListener(v -> { selectedColor = "note_green"; highlightSelected(colorGreen); });
        colorRed.setOnClickListener(v -> { selectedColor = "note_red"; highlightSelected(colorRed); });
        colorOrange.setOnClickListener(v -> { selectedColor = "note_orange"; highlightSelected(colorOrange); });
        colorPurple.setOnClickListener(v -> { selectedColor = "note_purple"; highlightSelected(colorPurple); });

        buttonSave.setOnClickListener(v -> {
            String titleText = editTextTitle.getText().toString().trim();
            String contentText = editTextContent.getText().toString().trim();

            if (titleText.isEmpty() || contentText.isEmpty()) {
                Toast.makeText(this, "Completează titlul și conținutul", Toast.LENGTH_SHORT).show();
                return;
            }

            NotesApi api = RetrofitClient.getRetrofitInstance().create(NotesApi.class);
            Call<ResponseBody> call = (noteId == -1)
                    ? api.addNote(titleText, contentText, selectedColor)
                    : api.updateNote(noteId, titleText, contentText, selectedColor);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String msg = (noteId == -1) ? "Notiță adăugată!" : "Notiță actualizată!";
                        Toast.makeText(AddEditNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddEditNoteActivity.this, "Eroare la salvare", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AddEditNoteActivity.this, "Eroare rețea", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void highlightSelected(View selectedView) {
        for (View view : colorViews) {
            if (view.getId() == R.id.colorYellow) {
                view.setBackgroundResource(R.color.note_yellow);
            } else if (view.getId() == R.id.colorBlue) {
                view.setBackgroundResource(R.color.note_blue);
            } else if (view.getId() == R.id.colorGreen) {
                view.setBackgroundResource(R.color.note_green);
            } else if (view.getId() == R.id.colorRed) {
                view.setBackgroundResource(R.color.note_red);
            } else if (view.getId() == R.id.colorOrange) {
                view.setBackgroundResource(R.color.note_orange);
            } else if (view.getId() == R.id.colorPurple) {
                view.setBackgroundResource(R.color.note_purple);
            }
        }

        // Aplica conturul SELECTAT
        if (selectedView.getId() == R.id.colorYellow) {
            selectedView.setBackgroundResource(R.drawable.color_selected);
        } else if (selectedView.getId() == R.id.colorBlue) {
            selectedView.setBackgroundResource(R.drawable.color_selected_blue);
        } else if (selectedView.getId() == R.id.colorGreen) {
            selectedView.setBackgroundResource(R.drawable.color_selected_green);
        } else if (selectedView.getId() == R.id.colorRed) {
            selectedView.setBackgroundResource(R.drawable.color_selected_red);
        } else if (selectedView.getId() == R.id.colorOrange) {
            selectedView.setBackgroundResource(R.drawable.color_selected_orange);
        } else if (selectedView.getId() == R.id.colorPurple) {
            selectedView.setBackgroundResource(R.drawable.color_selected_purple);
        }
    }


    private int getOriginalColor(int viewId) {
        if (viewId == R.id.colorYellow) return R.color.note_yellow;
        if (viewId == R.id.colorBlue) return R.color.note_blue;
        if (viewId == R.id.colorGreen) return R.color.note_green;
        if (viewId == R.id.colorRed) return R.color.note_red;
        if (viewId == R.id.colorOrange) return R.color.note_orange;
        if (viewId == R.id.colorPurple) return R.color.note_purple;
        return R.color.darker_gray;
    }

    private int getColorViewId(String colorName) {
        switch (colorName) {
            case "note_yellow": return R.id.colorYellow;
            case "note_blue": return R.id.colorBlue;
            case "note_green": return R.id.colorGreen;
            case "note_red": return R.id.colorRed;
            case "note_orange": return R.id.colorOrange;
            case "note_purple": return R.id.colorPurple;
            default: return -1;
        }
    }
}
