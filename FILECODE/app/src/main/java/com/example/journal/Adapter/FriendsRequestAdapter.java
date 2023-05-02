package com.example.journal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.FriendRequestViewHolder> {
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseFirestore db;
    ArrayList<FriendsRequest> lsFriendRequest;
    Context context;
    FriendRequestCallBack friendRequestCallBack;
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
    public interface FriendRequestCallBack{

    }
}
