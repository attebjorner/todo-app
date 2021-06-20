package com.github.attebjorner.todo_app.view.adapter;

import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
{
    private List<Note> notes;
    private int totalHeightPx = 8 + 8;

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
        if (position == notes.size())
        {
            holder.tvDescription.setText(R.string.add_new);
            holder.tvDescription.setTextColor(0x4D000000);
            holder.tvDeadline.setVisibility(View.GONE);
            holder.imbCheckbox.setVisibility(View.GONE);
            holder.imbInfo.setVisibility(View.GONE);
            int paddingPx = (int) (35 * holder.tvDescription.getContext().getResources().getDisplayMetrics().density + 0.5f);
            holder.tvDescription.setPadding(paddingPx, 0, 0, 0);
        }
        else
        {
            holder.tvDescription.setText(notes.get(position).getDescription());
            if (notes.get(position).getDeadline() == null) holder.tvDeadline.setVisibility(View.GONE);
            else holder.tvDeadline.setText(notes.get(position).getDeadline().toString());
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
        }
        holder.itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        totalHeightPx += holder.itemView.getMeasuredHeight();
    }

    @Override
    public int getItemCount()
    {
        return notes.size() + 1;
    }

    public int getTotalHeightPx()
    {
        return totalHeightPx;
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
            tvDescription.setTextColor(0x4D000000);
            tvDescription.setPaintFlags(
                    tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        }

        public void setAsImportant(String text)
        {
            imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked_red);
            tvDescription.setText(
                    Html.fromHtml(
                            "<font color=#FF3B30>!! </font> <font color=#000000>"
                                    + text
                                    + "</font>"
                    )
            );
        }
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
                if (note.getImportance() == Importance.HIGH)
                {
                    holder.setAsImportant(note.getDescription());
                }
                else
                {
                    holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked);
                    holder.tvDescription.setTextColor(0xFF000000);
                }
                holder.tvDescription.setPaintFlags(
                        holder.tvDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
            }
        }
    }
}
