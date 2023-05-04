package com.example.journal.Adapter;

import android.content.Context;
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

import com.example.journal.HelperClass;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.FriendRequestViewHolder> {
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseFirestore db;
    ArrayList<FriendsRequest> lsFriendRequest;
    Context context;
    String userID;
    public FriendsRequestAdapter(ArrayList<FriendsRequest> lsFriendRequest) {
        this.lsFriendRequest = lsFriendRequest;
    }
    @NonNull
    @Override
    public FriendsRequestAdapter.FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.loimoiketban_layout,parent,false);
        //
        FriendsRequestAdapter.FriendRequestViewHolder viewHolder=new FriendsRequestAdapter.FriendRequestViewHolder(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();

        db = FirebaseFirestore.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRequestAdapter.FriendRequestViewHolder holder, int position) {
        FriendsRequest item=lsFriendRequest.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(item.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if(userProfile!=null)
                {
                    item.setAvatar_friendrequest(userProfile.getImage());
                    item.setNameuser_friendrequest(userProfile.getFullname());
                    holder.tvUserName.setText(item.getNameuser_friendrequest());
                    if(!item.getAvatar_friendrequest().isEmpty())
                    {Picasso.get().load(item.getAvatar_friendrequest()).placeholder(R.drawable.account_circle).into(holder.imgAvatar);}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnXacnhan.getText().toString().contentEquals("Xác nhận")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", item.getId());
                    bundle.putInt("state", 1);
                    Navigation.findNavController(view).navigate(R.id.action_friendRequestFragment_to_strangeUserFragment, bundle);
                }
                if(holder.btnXacnhan.getText().toString().contentEquals("Đã kết bạn")){
                    Bundle bundle=new Bundle();
                    bundle.putString("id",item.getId());
                    bundle.putInt("state",3);
                    Navigation.findNavController(view).navigate(R.id.action_friendRequestFragment_to_friendPageFragment,bundle);
                }
                if(holder.btnXacnhan.getText().toString().contentEquals("Đã hủy lời mời kết bạn")){
                    Bundle bundle=new Bundle();
                    bundle.putString("id",item.getId());
                    bundle.putInt("state",0);
                    Navigation.findNavController(view).navigate(R.id.action_friendRequestFragment_to_strangeUserFragment,bundle);
                }
            }
        });
        holder.btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnXacnhan.getText().toString().contentEquals("Xác nhận"))
                {
                    holder.btnXacnhan.setText("Đã kết bạn");
                    holder.btnHuy.setVisibility(View.INVISIBLE);
                    db.collection("FriendRequest").document(item.getId()).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    db.collection("FriendReceive").document(userID).collection("FriendRequest").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    Map<String,Object> databecomeFriend=new HashMap<String ,Object>();
                    databecomeFriend.put("id",userID);
                    db.collection("MyID").document(item.getId()).collection("MyFriendsID").document(userID).set(databecomeFriend);
                    databecomeFriend=new HashMap<String,Object>();
                    databecomeFriend.put("id",item.getId());
                    db.collection("MyID").document(userID).collection("MyFriendsID").document(item.getId()).set(databecomeFriend);
                }
            }
        });
        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnHuy.setVisibility(View.INVISIBLE);
                holder.btnXacnhan.setText("Đã hủy lời mời kết bạn");
                holder.btnXacnhan.setBackgroundColor(Color.GRAY);
                db.collection("FriendRequest").document(item.getId()).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("FriendReceive").document(userID).collection("FriendRequest").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return lsFriendRequest.size();
    }
    public class FriendRequestViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar;
        TextView tvUserName;
        Button btnXacnhan;
        Button btnHuy;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendsRequestItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendsRequestItem);
            btnXacnhan=itemView.findViewById(R.id.btnFriendsRequest_FriendsRequestItems);
            btnHuy=itemView.findViewById(R.id.btnDelete_FriendsRequestItems);
        }
    }

}
