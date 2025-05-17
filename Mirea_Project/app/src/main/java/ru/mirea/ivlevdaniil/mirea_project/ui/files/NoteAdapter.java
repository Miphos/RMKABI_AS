package ru.mirea.ivlevdaniil.mirea_project.ui.files;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    private final List<Note> notes;
    private final OnDeleteClickListener listener;

    public NoteAdapter(List<Note> notes, OnDeleteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView content, timestamp;
        ImageButton deleteBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.note_content);
            timestamp = itemView.findViewById(R.id.note_time);
            deleteBtn = itemView.findViewById(R.id.btnDeleteNote);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.content.setText(note.content);
        holder.timestamp.setText(note.timestamp);
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
