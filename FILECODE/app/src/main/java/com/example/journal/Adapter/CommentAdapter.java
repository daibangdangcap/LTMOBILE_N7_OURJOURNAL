package com.example.journal.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Model.Comment;
import com.example.journal.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentListViewholder> {
    ArrayList<Comment>lsComment;

    public CommentAdapter(ArrayList<Comment> lsComment) {
        this.lsComment = lsComment;
    }

    public CommentAdapter() {
    }

    @NonNull
    @Override
    public CommentAdapter.CommentListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentListViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class CommentListViewholder extends RecyclerView.ViewHolder{
        ImageView imgAvatarComment;
        TextView tvUsername;
        TextView tvComment;
        public CommentListViewholder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUserNameCmt);
            imgAvatarComment=itemView.findViewById(R.id.imgAvatarCmt);
            tvComment=itemView.findViewById(R.id.tvComment);
        }
    }
}
