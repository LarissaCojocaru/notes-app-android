package com.example.notesapp2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textTitle.setText(note.getTitle());
        holder.textContent.setText(note.getContent());
        holder.textDate.setText(""); // adaugă data dacă o ai

        int colorResId = context.getResources().getIdentifier(note.getColor(), "color", context.getPackageName());
        if (colorResId != 0) {
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, colorResId));
        } else {
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, R.color.note_yellow));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditNoteActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("color", note.getColor());
            context.startActivity(intent);
        });


        // ⭐ Iconiță stea
        holder.imageFavorite.setImageResource(
                note.isFavorite() ? R.drawable.ic_star_favorite : R.drawable.ic_star_border
        );

        // Click pe stea
        holder.imageFavorite.setOnClickListener(v -> {
            boolean newStatus = !note.isFavorite();
            note.setFavorite(newStatus);

            // Update UI
            holder.imageFavorite.setImageResource(
                    newStatus ? R.drawable.ic_star_favorite : R.drawable.ic_star_border
            );

            // Update DB
            NotesApi api = RetrofitClient.getRetrofitInstance().create(NotesApi.class);
            api.updateFavorite(note.getId(), newStatus ? 1 : 0).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context,
                                newStatus ? "Adăugat la favorite" : "Eliminat din favorite",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Eroare server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Eroare rețea", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Click lung pentru ștergere
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Ștergi notița?")
                    .setMessage("Această acțiune este ireversibilă.")
                    .setPositiveButton("Șterge", (dialog, which) -> {
                        NotesApi api = RetrofitClient.getRetrofitInstance().create(NotesApi.class);
                        api.deleteNote(note.getId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Notiță ștearsă", Toast.LENGTH_SHORT).show();
                                    int pos = holder.getAdapterPosition();
                                    noteList.remove(pos);
                                    notifyItemRemoved(pos);
                                } else {
                                    Toast.makeText(context, "Eroare la ștergere", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "Eroare rețea", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Anulează", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textContent, textDate;
        View viewColor;
        ImageView imageFavorite;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textViewTitle);
            textContent = itemView.findViewById(R.id.textViewContent);
            textDate = itemView.findViewById(R.id.textViewDate);
            viewColor = itemView.findViewById(R.id.viewColor);
            imageFavorite = itemView.findViewById(R.id.imageFavorite);
        }
    }
}
