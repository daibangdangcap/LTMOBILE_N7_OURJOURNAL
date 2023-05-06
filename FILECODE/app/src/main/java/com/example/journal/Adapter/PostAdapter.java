package com.example.journal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journal.HelperClass;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostListViewHolder> {
    public PostAdapter(ArrayList<Post> lsPost) {
        this.lsPost = lsPost;
    }
    FirebaseUser user;
    ArrayList<Post> lsPost;
    Context context;

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
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mnEdit:
                                editPost(item.getPostKey());
                                return true;
                            case R.id.mnDelete:
                                FirebaseDatabase.getInstance().getReference("Posts")
                                        .child(item.getUserId()).child(item.getPostKey()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!item.getUserId().equals(user.getUid())) {
                    popupMenu.getMenu().findItem(R.id.mnEdit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.mnDelete).setVisible(false);
                }
                popupMenu.show();
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
        ImageView imgUserAvatar, ivMore;

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
            user = FirebaseAuth.getInstance().getCurrentUser();
            imgAvatar=itemView.findViewById(R.id.imageAvatar);
            tvCaption=itemView.findViewById(R.id.tvCaption);
            tvUserName=itemView.findViewById(R.id.tvUsername);
            PostImage=itemView.findViewById(R.id.ivPostImage);
            imgUserAvatar = itemView.findViewById(R.id.imageAvatar);
            ivMore = itemView.findViewById(R.id.ivMore_Post);
        }
    }
    public interface PostCallBack {}

    private void editPost(String postId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Edit post");

        EditText editText = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        dialog.setView(editText);

        getText(postId, editText);

        dialog.setPositiveButton("Chỉnh sửa",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("caption", editText.getText().toString());
                        FirebaseDatabase.getInstance().getReference("Posts")
                                .child(postId).updateChildren(hashMap);
                    }
                });
                dialog.setNegativeButton("Thoát",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        dialog.show();
    }

    private void getText(String postId, EditText editText){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editText.setText(snapshot.getValue(Post.class).getCaption());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
