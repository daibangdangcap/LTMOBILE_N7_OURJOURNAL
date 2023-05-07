package com.example.journal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journal.Model.Comment;
import com.example.journal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentListViewholder> {
    ArrayList<Comment>lsComment;
    Context context;

    public CommentAdapter(ArrayList<Comment> lsComment) {
        this.lsComment = lsComment;
    }

    public CommentAdapter() {
    }

    @NonNull
    @Override
    public CommentAdapter.CommentListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_item_comment,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentListViewholder holder, int position) {
            Comment item=lsComment.get(position);
            holder.tvComment.setText(item.getCommentContent());
            holder.tvUsername.setText(item.getFullname());
            Glide.with(context).load(item.getImage()).into(holder.imgAvatarComment);

    }

    @Override
    public int getItemCount() {
        return lsComment.size();
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
