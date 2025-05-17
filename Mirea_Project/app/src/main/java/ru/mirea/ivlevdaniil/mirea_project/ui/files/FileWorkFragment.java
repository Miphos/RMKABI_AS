package ru.mirea.ivlevdaniil.mirea_project.ui.files;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class FileWorkFragment extends Fragment {

    private final List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private final String FILE_NAME = "notes.json";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(notes, this::deleteNote);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> showNoteDialog());

        loadNotes();

        return view;
    }

    private void showNoteDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Введите текст");

        new AlertDialog.Builder(getContext())
                .setTitle("Новая заметка")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String text = input.getText().toString();
                    String time = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
                    notes.add(new Note(text, time));
                    saveNotes();
                    adapter.notifyItemInserted(notes.size() - 1);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deleteNote(int position) {
        notes.remove(position);
        saveNotes();
        adapter.notifyItemRemoved(position);
    }

    private void saveNotes() {
        JSONArray jsonArray = new JSONArray();
        for (Note note : notes) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("content", note.content);
                obj.put("timestamp", note.timestamp);
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = requireContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes() {
        notes.clear();
        try (FileInputStream fis = requireContext().openFileInput(FILE_NAME)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            JSONArray array = new JSONArray(new String(data));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                notes.add(new Note(obj.getString("content"), obj.getString("timestamp")));
            }
        } catch (Exception e) {
            // Файл может быть пуст или не существовать
        }
    }
}
