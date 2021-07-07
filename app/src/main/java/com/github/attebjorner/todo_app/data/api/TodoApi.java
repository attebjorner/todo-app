package com.github.attebjorner.todo_app.data.api;

import com.github.attebjorner.todo_app.model.NoteDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoApi
{
    @GET("tasks")
    Call<List<NoteDto>> getTasks();

    @POST("tasks")
    Call<NoteDto> postTask(@Body NoteDto note);

    @PUT("tasks/{task_id}")
    Call<NoteDto> updateTask(@Path("task_id") String id, @Body NoteDto note);

    @DELETE("tasks/{task_id}")
    Call<NoteDto> deleteTask(@Path("task_id") String id);

    @PUT("tasks")
    Call<List<NoteDto>> putDeletedAndOther(@Body Map<String, List<String>> deleted,
                                           @Body Map<String, List<NoteDto>> other);
}
