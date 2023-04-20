package com.example.journal.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostListViewHolder> {
    @NonNull
    @Override
    public PostAdapter.PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class PostListViewHolder extends RecyclerView.ViewHolder{

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
