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
import com.example.journal.HelperClass;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.rpc.Help;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostListViewHolder> {
    public PostAdapter(ArrayList<Post> lsPost) {
        this.lsPost = lsPost;
    }
    FirebaseUser user;
    ArrayList<Post> lsPost;
    Context context;
    DatabaseReference databaseReference;

    @NonNull
    @Override
    public PostAdapter.PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_post_item,parent,false);
        PostListViewHolder postListViewHolder=new PostListViewHolder(view);
        return postListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostListViewHolder holder, int position) {
        Post item=lsPost.get(position);
        holder.tvUserName.setText(item.getUserName());
        holder.tvCaption.setText(item.getCaption());
        Picasso.get().load(item.getImage()).into(holder.PostImage);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(item.getUserId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass helperClass = snapshot.getValue(HelperClass.class);
                Glide.with(context).load(helperClass.getImage()).into(holder.imgUserAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return lsPost.size();
    }
    public class PostListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar;
        TextView tvUserName;
        TextView tvCaption;
        ImageView PostImage;
        ImageView imgUserAvatar;

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
            user = FirebaseAuth.getInstance().getCurrentUser();
            imgAvatar=itemView.findViewById(R.id.imageAvatar);
            tvCaption=itemView.findViewById(R.id.tvCaption);
            tvUserName=itemView.findViewById(R.id.tvUsername);
            PostImage=itemView.findViewById(R.id.ivPostImage);
            imgUserAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }
    public interface PostCallBack
    {

    }
}
