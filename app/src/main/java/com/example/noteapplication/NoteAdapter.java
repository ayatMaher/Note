package com.example.noteapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private  List<Note> mData;
    private  LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener2 itemClickListener2;

    NoteAdapter(Context context, List<Note> data, ItemClickListener onClick, ItemClickListener2 onClick2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = onClick;
        this.itemClickListener2 = onClick2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.content.setText(mData.get(position).getContent());
        holder.title.setText(mData.get(position).getTitle());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(holder.getAdapterPosition(), mData.get(position).id);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener2.onItemClick2(holder.getAdapterPosition(), mData.get(position).id);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView content;
        public TextView title;
        public ImageView delete;
        public LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.txtContent);
            this.title = itemView.findViewById(R.id.txtTitle);
            this.delete = itemView.findViewById(R.id.deleteNote);
            this.container = itemView.findViewById(R.id.container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    Note getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }

    public interface ItemClickListener2 {
        void onItemClick2(int position, String id);
    }
}
