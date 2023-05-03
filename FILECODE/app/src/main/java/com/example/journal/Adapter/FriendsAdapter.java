package com.example.journal.Adapter;

import android.content.Context;
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

import com.example.journal.HelperClass;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseFirestore db;
    ArrayList<FriendsList> lsFriendList;
    Context context;
    String userID;
    public FriendsAdapter(ArrayList<FriendsList> lsFriendList) {
        this.lsFriendList = lsFriendList;
    }
    @NonNull
    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.frienditem_layout,parent,false);
        //
        FriendsAdapter.FriendsViewHolder viewHolder=new FriendsAdapter.FriendsViewHolder(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();

        db = FirebaseFirestore.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, int position) {
        FriendsList item=lsFriendList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(item.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if(userProfile!=null)
                {
                    item.setAvatar_FriendsList(userProfile.getImage());
                    item.setNameFriendItem_FriendsList(userProfile.getFullname());
                    holder.tvUserName.setText(item.getNameFriendItem_FriendsList());
                    if(!item.getAvatar_FriendsList().isEmpty())
                    {Picasso.get().load(item.getAvatar_FriendsList()).placeholder(R.drawable.account_circle).into(holder.imgAvatar);}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return lsFriendList.size();
    }
    public class FriendsViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar;
        TextView tvUserName;
        Button btnFriend;
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendslistItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendslistItem);
            btnFriend=itemView.findViewById(R.id.btnFriends_FriendslistItems);
        }
    }
}
