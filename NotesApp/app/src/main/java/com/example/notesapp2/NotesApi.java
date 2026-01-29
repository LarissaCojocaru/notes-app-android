package com.example.notesapp2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NotesApi {

    @GET("get_notes.php")
    Call<List<Note>> getNotes();

    @FormUrlEncoded
    @POST("add_note.php")
    Call<ResponseBody> addNote(
            @Field("title") String title,
            @Field("content") String content,
            @Field("color") String color
    );

    @FormUrlEncoded
    @POST("update_note.php")
    Call<ResponseBody> updateNote(
            @Field("id") int id,
            @Field("title") String title,
            @Field("content") String content,
            @Field("color") String color
    );

    @FormUrlEncoded
    @POST("delete_note.php")
    Call<ResponseBody> deleteNote(@Field("id") int id);

    @FormUrlEncoded
    @POST("update_favorite.php") // ✅ Endpoint pentru toggle favorite
    Call<ResponseBody> updateFavorite(
            @Field("id") int id,
            @Field("is_favorite") int isFavorite
    );
}
