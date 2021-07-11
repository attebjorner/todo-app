package com.github.attebjorner.todo_app.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.activity.CreateNoteActivity;

import java.time.LocalDate;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
{
    private final List<Note> notes;

    private static OnCheckboxClickListener checkboxClickListener;

    private static final String[] HTML_LABEL_COLORS = new String[2];

    private static final int[] LABEL_COLORS = new int[2];

    public TodoListAdapter(List<Note> notes, Context context)
    {
        this.notes = notes;
        HTML_LABEL_COLORS[0] = context.getString(R.string.red_color);
        HTML_LABEL_COLORS[1] = context.getString(R.string.label_primary_color);
        LABEL_COLORS[0] = ContextCompat.getColor(context, R.color.label_primary);
        LABEL_COLORS[1] = ContextCompat.getColor(context, R.color.label_tertiary);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listitem, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.ViewHolder holder, int position)
    {
        Note note = notes.get(position);
        holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked);
        holder.setDescription(note);
        holder.setDeadline(note);

        if (note.isDone())
        {
            holder.setCheckboxDone(note);
        }
        else if (note.getImportance() == Importance.HIGH)
        {
            holder.setAsImportant(note.getDescription());
        }

        holder.imbCheckbox.setOnClickListener(new CheckboxListener(
                note.isDone(), position
        ));
        InfoListener infoListener = new InfoListener(note);
        holder.imbInfo.setOnClickListener(infoListener);
        holder.tvDescription.setOnClickListener(infoListener);
        holder.tvDeadline.setOnClickListener(infoListener);
    }

    @Override
    public int getItemCount()
    {
        return notes.size();
    }

    public void setCheckboxClickListener(OnCheckboxClickListener checkboxClickListener)
    {
        TodoListAdapter.checkboxClickListener = checkboxClickListener;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton imbCheckbox, imbInfo;

        TextView tvDescription, tvDeadline;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imbCheckbox = itemView.findViewById(R.id.imbCheckbox);
            imbInfo = itemView.findViewById(R.id.imbInfo);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
        }

        public void setDescription(Note note)
        {
            tvDescription.setText(note.getDescription());
            tvDescription.setTextColor(LABEL_COLORS[note.isDone() ? 1 : 0]);
            tvDescription.setPaintFlags(
                    note.isDone()
                            ? tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                            : 0
            );
        }

        public void setDeadline(Note note)
        {
            if (note.getDeadline() == null)
            {
                tvDeadline.setVisibility(View.GONE);
            }
            else
            {
                tvDeadline.setText(note.getDeadline().toString());
                if (note.getDeadline().isBefore(LocalDate.now())) setAsOutdated();
            }
        }

        public void setCheckboxDone(Note note)
        {
            imbCheckbox.setBackgroundResource(R.drawable.ic_checked);
            if (note.getImportance() == Importance.HIGH)
            {
                tvDescription.setText(note.getDescription());
            }
        }

        public void setAsImportant(String text)
        {
            tvDescription.setText(
                    Html.fromHtml(
                            "<font color=" + HTML_LABEL_COLORS[0] + ">!! </font> " +
                            "<font color=" + HTML_LABEL_COLORS[1] + ">" + text + "</font>"
                    )
            );
        }

        private void setAsOutdated()
        {
            imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked_red);
        }
    }

    public static class CheckboxListener implements View.OnClickListener
    {
        private final boolean isDone;

        private final int pos;

        public CheckboxListener(boolean isDone, int pos)
        {
            this.isDone = isDone;
            this.pos = pos;
        }

        @Override
        public void onClick(View v)
        {
            checkboxClickListener.onClick(isDone, pos);
        }
    }

    public static class InfoListener implements View.OnClickListener
    {
        private final Note note;

        public InfoListener(Note note)
        {
            this.note = note;
        }

        @Override
        public void onClick(View v)
        {
            TinyDB tinyDB = new TinyDB(v.getContext());
            Intent intent = new Intent(v.getContext(), CreateNoteActivity.class);
            intent.putExtra("isNew", false);
            tinyDB.putObject("editNote", note);
            tinyDB.putString("editNoteDeadline", note.getDeadline().toString());
            v.getContext().startActivity(intent);
        }
    }
}
