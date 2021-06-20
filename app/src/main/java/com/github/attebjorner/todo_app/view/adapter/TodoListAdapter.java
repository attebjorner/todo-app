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
                setCheckboxDone(holder.imbCheckbox, holder.tvDescription);
            }
            else if (notes.get(position).getImportance() == Importance.HIGH)
            {
                holder.imbCheckbox.setBackgroundResource(R.drawable.ic_unchecked_red);
            }
            holder.imbCheckbox.setOnClickListener(new CheckboxListener(
                    notes.get(position), holder.imbCheckbox, holder.tvDescription
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
    }

    public static class CheckboxListener implements View.OnClickListener
    {
        private Note note;
        private ImageButton imbCheckbox;
        private TextView tvDescription;

        public CheckboxListener(Note note, ImageButton imbCheckbox, TextView tvDescription)
        {
            this.note = note;
            this.imbCheckbox = imbCheckbox;
            this.tvDescription = tvDescription;
        }

        @Override
        public void onClick(View v)
        {
            note.setDone(!note.isDone());
            if (note.isDone())
            {
                setCheckboxDone(imbCheckbox, tvDescription);
            }
            else
            {
                imbCheckbox.setBackgroundResource(
                        note.getImportance() == Importance.HIGH
                                ? R.drawable.ic_unchecked_red
                                : R.drawable.ic_unchecked
                );
                tvDescription.setTextColor(0xFF000000);
                tvDescription.setPaintFlags(
                        tvDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
            }
        }
    }

    private static void setCheckboxDone(ImageButton imbCheckbox, TextView tvDescription)
    {
        imbCheckbox.setBackgroundResource(R.drawable.ic_checked);
        tvDescription.setTextColor(0x4D000000);
        tvDescription.setPaintFlags(
                tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        );
    }
}
