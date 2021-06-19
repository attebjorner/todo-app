package com.github.attebjorner.todo_app.view.adapter;

import android.graphics.Paint;
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
        else holder.tvDeadline.setText(notes.get(position).getDeadline().toString());
        if (notes.get(position).isDone())
        {
            holder.imbCheckbox.setBackgroundResource(R.drawable.ic_checked);
            holder.tvDescription.setTextColor(0x4D000000);
            holder.tvDescription.setPaintFlags(
                    holder.tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        }
        else if (notes.get(position).getImportance() == Importance.HIGH)
        {
            holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked_red);
        }
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
    }
}
