package com.example.journal.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journal.HelperClass;
import com.example.journal.Model.Comment;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.rpc.Help;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostListViewHolder> {
    public PostAdapter(ArrayList<Post> lsPost) {
        this.lsPost = lsPost;
    }
    FirebaseUser user;
    ArrayList<Post> lsPost;
    Context context;
    DatabaseReference databaseReference;
    FirebaseFirestore db;
    private DatabaseReference reference;
    private String userID;
    String fullname;
    String image;
    ArrayList<Comment>lsComment;
    @NonNull
    @Override
    public PostAdapter.PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_post_item,parent,false);
        db=FirebaseFirestore.getInstance();
        PostListViewHolder postListViewHolder=new PostListViewHolder(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID  = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if (userProfile != null)
                {
                    fullname=userProfile.getFullname();
                    image=userProfile.getImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        if (!item.getUserId().equals(user.getUid())) {
            holder.ivMore.setVisibility(View.GONE);
        }
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mnEdit:
                                editPost(item.getPostKey(),view);
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
        holder.tvTotal_Like.setText(Integer.toString(item.getSum_Like()));

        db.collection("Like").document(item.getUserId()).collection(item.getPostKey())
                .whereEqualTo("idUserLike",userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            holder.imgLike.setImageResource(R.drawable.like);
                        }
                        else
                        {
                            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    }
                });
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=new HashMap<>();
                map.put("idPost",item.getPostKey());
                map.put("idUserLike",userID);
                map.put("fullname",fullname);
                map.put("Avatar",image);
                db.collection("Like").document(item.getUserId()).collection(item.getPostKey())
                        .whereEqualTo("idUserLike",userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty())
                                {

                                    db.collection("Like").document(item.getUserId()).collection(item.getPostKey()).document(userID).set(map);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                                    reference.child(item.getUserId()).child(item.getPostKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Post post=snapshot.getValue(Post.class);
                                            item.setSum_Like(post.getSum_Like());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    int sumLike=item.getSum_Like();
                                    sumLike++;
                                    item.setSum_Like(sumLike);
                                    updateSumLike(sumLike,item);
                                    holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);
                                    lsPost.clear();
                                }
                                else
                                {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                                    reference.child(item.getUserId()).child(item.getPostKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Post post=snapshot.getValue(Post.class);
                                            item.setSum_Like(post.getSum_Like());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    int sumLike=item.getSum_Like();
                                    sumLike--;
                                    item.setSum_Like(sumLike);
                                    updateSumLike(sumLike,item);
                                    db.collection("Like").document(item.getUserId()).collection(item.getPostKey()).document(userID).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                }
                                            });
                                    holder.imgLike.setImageResource(R.drawable.like);
                                    lsPost.clear();
                                }
                            }
                        });
            }
        });
        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogComment(view, Gravity.CENTER,userID,fullname,image,item);
            }
        });
        holder.tvTotal_Comment.setText(Integer.toString(item.getSum_Comment()));
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
        ImageView imgLike;
        ImageView imgComment;
        TextView tvTotal_Like;
        TextView tvTotal_Comment;

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
            user = FirebaseAuth.getInstance().getCurrentUser();
            imgAvatar=itemView.findViewById(R.id.imageAvatar);
            tvCaption=itemView.findViewById(R.id.tvCaption);
            tvUserName=itemView.findViewById(R.id.tvUsername);
            PostImage=itemView.findViewById(R.id.ivPostImage);
            imgUserAvatar = itemView.findViewById(R.id.imageAvatar);
            ivMore = itemView.findViewById(R.id.ivMore_Post);
            imgLike=itemView.findViewById(R.id.ivLike);
            imgComment=itemView.findViewById(R.id.ivComment);
            tvTotal_Like=itemView.findViewById(R.id.tvTotalLike);
            tvTotal_Comment=itemView.findViewById(R.id.tvTotalComment);

        }
    }
    public interface PostCallBack {}

    private void editPost(String postId,View view) {
        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId=user1.getUid();
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
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("caption", editText.getText().toString());
                        FirebaseDatabase.getInstance().getReference("Posts")
                                .child(currentUserId).child(postId).updateChildren(hashMap);
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
        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId=user1.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(currentUserId).child(postId);
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
    /*private int sum_Like(Post item,PostListViewHolder holder,int h)
    {h=0;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.child(item.getUserId()).child(item.getPostKey()).addValueEventListener(new ValueEventListener() {
            public int sumLike = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post=snapshot.getValue(Post.class);
                sumLike=post.getSum_Like();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    private void updateSumLike(int sum_Like,Post item)
    {
        Map<String,Object>map=new HashMap<>();
        map.put("caption",item.getCaption());
        map.put("image",item.getImage());
        map.put("location",item.getLocation());
        map.put("postKey",item.getPostKey());
        map.put("sum_Comment",item.getSum_Comment());
        map.put("sum_Like",item.getSum_Like());
        map.put("userId",item.getUserId());
        map.put("userName",item.getUserName());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1=database.getReference("Posts").child(item.getUserId()).child(item.getPostKey());
        databaseReference1.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }
    private void OpenDialogComment(View view,int gravity,String userID,String fullname,String image,Post item)
    {
        final Dialog dialog=new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_comment_dialog);
        Window window=dialog.getWindow();
        if(window==null)
        {
            return;
        }
        else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes=window.getAttributes();
            windowAttributes.gravity=gravity;
            window.setAttributes(windowAttributes);
            if(Gravity.BOTTOM!=gravity)
            {
                dialog.setCancelable(true);
                dialog.dismiss();
            }
            else dialog.setCancelable(false);
            RecyclerView rvListCmt;
            EditText edInputCmt;
            ImageButton btnSendCmt;
            rvListCmt=dialog.findViewById(R.id.rvListComment);
            edInputCmt=dialog.findViewById(R.id.edInputComment);
            btnSendCmt=dialog.findViewById(R.id.btnSendComment);
            String id=db.collection("Comment").document().getId();
            lsComment=new ArrayList<>();
            CommentAdapter commentAdapter=new CommentAdapter(lsComment);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
            rvListCmt.setAdapter(commentAdapter);
            rvListCmt.setLayoutManager(linearLayoutManager);
            getListCommentFromDatabase(item,commentAdapter);
            btnSendCmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edInputCmt.getText().toString()==null) return;
                    else
                    {

                        Map<String,Object> map=new HashMap<>();
                        map.put("CommentID",id);
                        map.put("idUsername",userID);
                        map.put("username",fullname);
                        map.put("image",image);
                        map.put("commentContent",edInputCmt.getText().toString());
                        db.collection("Comment").document(item.getUserId()).collection(item.getPostKey()).document(id).set(map);
                        Comment comment=new Comment(id,userID,fullname,image,edInputCmt.getText().toString());
                        lsComment.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        edInputCmt.setText("");
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        reference.child(item.getUserId()).child(item.getPostKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Post post=snapshot.getValue(Post.class);
                                item.setSum_Comment(post.getSum_Comment());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        int sum_comment=item.getSum_Comment();
                        sum_comment++;
                        item.setSum_Comment(sum_comment);
                        UpdateSumComment(sum_comment,item);
                    }
                }
            });

        }
        dialog.show();
    }
    private void getListCommentFromDatabase(Post item,CommentAdapter commentAdapter)
    {
        db.collection("Comment").document(item.getUserId()).collection(item.getPostKey()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        lsComment.clear();
                        for(DocumentSnapshot snapshot: task.getResult())
                        {
                            Comment comment=new Comment(snapshot.getString("CommentID"),snapshot.getString("idUsername"),snapshot.getString("username"),snapshot.getString("image"),snapshot.getString("commentContent"));
                            lsComment.add(comment);
                            int count=0;
                            count=task.getResult().size();
                            count++;
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void UpdateSumComment(int sumCmt,Post item)
    {
        Map<String,Object>map=new HashMap<>();
        map.put("caption",item.getCaption());
        map.put("image",item.getImage());
        map.put("location",item.getLocation());
        map.put("postKey",item.getPostKey());
        map.put("sum_Comment",item.getSum_Comment());
        map.put("sum_Like",item.getSum_Like());
        map.put("userId",item.getUserId());
        map.put("userName",item.getUserName());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1=database.getReference("Posts").child(item.getUserId()).child(item.getPostKey());
        databaseReference1.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }
}
