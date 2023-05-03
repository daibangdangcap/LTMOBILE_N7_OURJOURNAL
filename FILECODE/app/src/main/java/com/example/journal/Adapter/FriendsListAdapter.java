package com.example.journal.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseFirestore db;

    public void setLsFriendsList(ArrayList<FriendsList> lsFriendsList) {
        this.lsFriendsList = lsFriendsList;
    }

    ArrayList<FriendsList> lsFriendsList;
    String userID;
    int state=0;
Context context;


    public FriendsListAdapter(ArrayList<FriendsList> lsFriendsList)
    {
        this.lsFriendsList=lsFriendsList;
    }

    @NonNull
    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.banbe_layout,parent,false);
        //
        FriendsListViewHolder viewHolder=new FriendsListViewHolder(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        db = FirebaseFirestore.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendsListViewHolder holder, int position) {
        FriendsList item=lsFriendsList.get(position);
        if(!item.getAvatar_FriendsList().isEmpty())
        {
            Picasso.get().load(item.getAvatar_FriendsList()).placeholder(R.drawable.account_circle).into(holder.imgAvatar);
        }

        holder.tvUserName.setText(item.getNameFriendItem_FriendsList());
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("id", item.getId());
                bundle.putInt("state",state);
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_strangeUserFragment,bundle);
            }
        });
        Map<String,Object> datafriendRequest=new HashMap<String, Object>();
        db.collection("FriendRequest").document(userID).collection("FriendReceive").whereEqualTo("id",item.getId()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                int count=0;
                                for(QueryDocumentSnapshot documentSnapshot:querySnapshot)
                                {
                                    count++;
                                }
                                if(count==0)//nếu truy vấn không có id của friend receive trong bảng FriendRequest thì set nút button là thêm bạn bè
                                {
                                    db.collection("MyID").document(userID).collection("MyFriendsID").whereEqualTo("id",item.getId()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot querySnapshot) {
                                                    if(!querySnapshot.isEmpty())
                                                    {
                                                        holder.btnConfirm.setVisibility(View.INVISIBLE);
                                                        holder.btnDelete.setVisibility(View.INVISIBLE);
                                                        state=3;
                                                        holder.btnAddFriend.setText("Bạn bè");
                                                        holder.btnAddFriend.setBackgroundColor(Color.parseColor("#33CC33"));
                                                    }
                                                    else
                                                    {
                                                        db.collection("FriendReceive").document(userID).collection("FriendRequest").whereEqualTo("id",item.getId()).get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                                                        if(!querySnapshot.isEmpty())
                                                                        {
                                                                            state=1;
                                                                            holder.btnAddFriend.setVisibility(View.INVISIBLE);
                                                                        }
                                                                        else {
                                                                            holder.btnAddFriend.setText("Thêm bạn bè");
                                                                            holder.btnAddFriend.setBackgroundColor(Color.GRAY);
                                                                            holder.btnConfirm.setVisibility(View.INVISIBLE);
                                                                            holder.btnDelete.setVisibility(View.INVISIBLE);
                                                                            state=0;
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                                else//ngược lại thì set đã gửi lời mời
                                {
                                    holder.btnConfirm.setVisibility(View.INVISIBLE);
                                    holder.btnDelete.setVisibility(View.INVISIBLE);
                                    holder.btnAddFriend.setText("Đã gửi lời mời");
                                    holder.btnAddFriend.setBackgroundColor(Color.GRAY);
                                    state=2;
                                }
                            }
                        });
        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(state==0)//nếu trạng thái =0 => khi nhấn nút thêm bạn bè đổi màu nút + text thành đã gửi lời mời
                    {
                        Map<String,Object> datafriendRequest=new HashMap<String, Object>();
                        datafriendRequest.put("id",item.getId());
                        Map<String,Object> datafriendReceive=new HashMap<String, Object>();
                        datafriendReceive.put("id",userID);
                        db.collection("FriendRequest").document(userID).collection("FriendReceive").document(item.getId()).set(datafriendRequest);
                        db.collection("FriendReceive").document(item.getId()).collection("FriendRequest").document(userID).set(datafriendReceive);
                        holder.btnAddFriend.setText("Đã gửi lời mời");
                        holder.btnAddFriend.setBackgroundColor(Color.GRAY);
                    }
            }
        });
    }
    @Override
    public int getItemCount() {
        return lsFriendsList.size();
    }
    public class FriendsListViewHolder extends RecyclerView.ViewHolder{
        Button btnConfirm;
        Button btnDelete;
        Button btnAddFriend;
        ImageView imgAvatar;
        TextView tvUserName;
        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendsItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendsItem);
            btnAddFriend=itemView.findViewById(R.id.btnFriends_FriendsItems);
            btnConfirm=itemView.findViewById(R.id.btnFriendsRequest_FriendsItems);
            btnDelete=itemView.findViewById(R.id.btnDelete_FriendsItems);
        }
    }
}
