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

    private static final String[] IMPORTANT_COLORS = new String[2];

    public TodoListAdapter(List<Note> notes, Context context)
    {
        this.notes = notes;
        IMPORTANT_COLORS[0] = context.getString(R.string.red_color);
        IMPORTANT_COLORS[1] = context.getString(R.string.label_primary_color);
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
        holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked);
        holder.tvDescription.setText(notes.get(position).getDescription());
        if (notes.get(position).getDeadline() == null)
        {
            holder.tvDeadline.setVisibility(View.GONE);
        }
        else
        {
            holder.tvDeadline.setText(notes.get(position).getDeadline().toString());
            if (notes.get(position).getDeadline().isBefore(LocalDate.now()))
            {
                holder.setAsOutdated();
            }
        }

        if (notes.get(position).isDone())
        {
            holder.setCheckboxDone(notes.get(position));
        }
        else if (notes.get(position).getImportance() == Importance.HIGH)
        {
            holder.setAsImportant(notes.get(position).getDescription());
        }

        holder.imbCheckbox.setOnClickListener(new CheckboxListener(
                notes.get(position).isDone(), position
        ));
        holder.imbInfo.setOnClickListener(new InfoListener(notes.get(position)));
        holder.tvDescription.setOnClickListener(new InfoListener(notes.get(position)));
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

        public void setCheckboxDone(Note note)
        {
            imbCheckbox.setBackgroundResource(R.drawable.ic_checked);
            if (note.getImportance() == Importance.HIGH)
            {
                tvDescription.setText(note.getDescription());
            }
            tvDescription.setTextColor(ContextCompat.getColor(tvDescription.getContext(), R.color.label_tertiary));
            tvDescription.setPaintFlags(
                    tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        }

        public void setAsImportant(String text)
        {
            tvDescription.setText(
                    Html.fromHtml(
                            "<font color=" + IMPORTANT_COLORS[0] + ">!! </font> " +
                            "<font color=" + IMPORTANT_COLORS[1] + ">" + text + "</font>"
                    )
            );
        }

        public void setAsOutdated()
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
            v.getContext().startActivity(intent);
        }
    }
}
