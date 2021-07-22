package com.github.attebjorner.todo_app.data.api;

import android.app.Application;
import android.util.Log;

import com.github.attebjorner.todo_app.data.database.repository.DeletedNoteRepository;
import com.github.attebjorner.todo_app.data.database.repository.NoteRepository;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.model.NoteDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequests
{
    private final String BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/";

    private final String API_TOKEN = "Bearer 01f40463204b4bc8a0346961db46f897";

    private final String TAG = "ApiRequests";

    private final NoteRepository noteRepository;

    private final DeletedNoteRepository deletedNoteRepository;

    private final TodoApi todoApi;

    public ApiRequests(Application app)
    {
        noteRepository = new NoteRepository(app);
        deletedNoteRepository = new DeletedNoteRepository(app);

        Interceptor interceptor = chain ->
        {
            Request newRequest = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", API_TOKEN)
                    .build();
            return chain.proceed(newRequest);
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        todoApi = retrofit.create(TodoApi.class);
    }

    public void syncTasks() throws IOException
    {
        Log.d(TAG, "updateTasks");
        Response<List<NoteDto>> response = todoApi.putDeletedAndOther(
                Map.of(
                        "deleted", deletedNoteRepository.getDeletedNotesIds()
                                .stream()
                                .map(UUID::toString)
                                .collect(Collectors.toList()),
                    "other", new ArrayList<>()
                )
        ).execute();
        if (response.isSuccessful())
        {
            Log.d(TAG, "syncTasks: response success");
            deletedNoteRepository.deleteAll();
            Set<Note> serverNotes = response.body()
                    .stream()
                    .map(NoteDto::toNote)
                    .collect(Collectors.toSet());
            Map<UUID, Note> undirtyNotesMap = noteRepository.getUndirtyNotes().stream()
                    .collect(Collectors.toMap(Note::getId, n -> n, (n1, n2) -> n2));
            Map<UUID, Note> dirtyNotesMap = noteRepository.getDirtyNotes().stream()
                    .collect(Collectors.toMap(Note::getId, n -> n, (n1, n2) -> n2));
            serverNotes.removeAll(undirtyNotesMap.values());
            for (Note n : serverNotes)
            {
                if (dirtyNotesMap.containsKey(n.getId()))
                {
                    if (n.getLastUpdate().isAfter(dirtyNotesMap.get(n.getId()).getLastUpdate()))
                    {
                        noteRepository.update(n);
                    }
                    else
                    {
                        todoApi.updateTask(
                                n.getId().toString(),
                                NoteDto.fromNote(dirtyNotesMap.get(n.getId()))
                        ).execute();
                    }
                    dirtyNotesMap.get(n.getId()).setDirty(false);
                    noteRepository.update(dirtyNotesMap.get(n.getId()));
                    dirtyNotesMap.remove(n.getId());
                }
                else if (!undirtyNotesMap.containsKey(n.getId()))
                {
                    noteRepository.insert(n);
                }
                else
                {
                    noteRepository.update(n);
                }
            }
            for (Note n : dirtyNotesMap.values())
            {
                todoApi.postTask(NoteDto.fromNote(n)).execute();
                n.setDirty(false);
                noteRepository.update(n);
            }
        }
    }
}
