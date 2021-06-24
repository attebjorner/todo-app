package com.github.attebjorner.todo_app.view.adapter;

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
    private List<Note> notes;
    private static OnCheckboxListener onCheckboxListener;

    public TodoListAdapter(List<Note> notes)
    {
        this.notes = notes;
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
        holder.tvDescription.setText(notes.get(position).getDescription());
        if (notes.get(position).getDeadline() == null) holder.tvDeadline.setVisibility(View.GONE);
        else
        {
            holder.tvDeadline.setText(notes.get(position).getDeadline().toString());
            if (notes.get(position).getDeadline().isBefore(LocalDate.now())) holder.setAsOutdated();
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
                notes.get(position), holder
        ));
        holder.imbInfo.setOnClickListener(new InfoListener(notes.get(position), position));
    }

    @Override
    public int getItemCount()
    {
        return notes.size();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton imbCheckbox, imbInfo;
        TextView tvDescription, tvDeadline;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imbCheckbox = (ImageButton) itemView.findViewById(R.id.imbCheckbox);
            imbInfo = (ImageButton) itemView.findViewById(R.id.imbInfo);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDeadline = (TextView) itemView.findViewById(R.id.tvDeadline);
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
            onCheckboxListener.onClick(1);
        }

        public void setAsImportant(String text)
        {
            tvDescription.setText(
                    Html.fromHtml(
                            "<font color=#FF3B30>!! </font> <font color=#000000>"
                                    + text
                                    + "</font>"
                    )
            );
        }

        public void setAsOutdated()
        {
            imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked_red);
        }
    }

    public interface OnCheckboxListener
    {
        void onClick(int d);
    }

    public void setOnCheckboxListener(OnCheckboxListener listener)
    {
        onCheckboxListener = listener;
    }

    public static class CheckboxListener implements View.OnClickListener
    {
        private Note note;
        private ViewHolder holder;

        public CheckboxListener(Note note, ViewHolder holder)
        {
            this.note = note;
            this.holder = holder;
        }

        @Override
        public void onClick(View v)
        {
            note.setDone(!note.isDone());
            if (note.isDone())
            {
                holder.setCheckboxDone(note);
            }
            else
            {
                if (note.getImportance() == Importance.HIGH) holder.setAsImportant(note.getDescription());
                else holder.tvDescription.setTextColor(ContextCompat.getColor(holder.imbInfo.getContext(), R.color.label_primary));
                holder.tvDescription.setPaintFlags(
                        holder.tvDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
                if (note.getDeadline() != null && note.getDeadline().isBefore(LocalDate.now()))
                {
                    holder.setAsOutdated();
                }
                else holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked);
                onCheckboxListener.onClick(-1);
            }
        }
    }

    public static class InfoListener implements View.OnClickListener
    {
        private Note note;
        private int pos;

        public InfoListener(Note note, int pos)
        {
            this.note = note;
            this.pos = pos;
        }

        @Override
        public void onClick(View v)
        {
            TinyDB tinyDB = new TinyDB(v.getContext());
            tinyDB.putObject("editNote", note);
            Intent intent = new Intent(v.getContext(), CreateNoteActivity.class);
            intent.putExtra("isNew", false);
            intent.putExtra("pos", pos);
            v.getContext().startActivity(intent);
        }
    }
}
