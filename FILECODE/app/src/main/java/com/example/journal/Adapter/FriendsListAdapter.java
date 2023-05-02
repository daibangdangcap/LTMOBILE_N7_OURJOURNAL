package com.example.journal.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Model.FriendsList;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {
    ArrayList<FriendsList> lsFriendsList;
Context context;
FriendsListCallBack friendsListCallBack;

    public FriendsListAdapter(ArrayList<FriendsList> lsFriendsList, FriendsListCallBack friendsListCallBack) {
        this.lsFriendsList = lsFriendsList;
        this.friendsListCallBack=friendsListCallBack;
    }

    @NonNull
    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.banbe_layout,parent,false);
        //
        FriendsListViewHolder viewHolder=new FriendsListViewHolder(view);
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
                Navigation.findNavController(view).navigate(R.id.action_friendFragment_to_strangeUserFragment,bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return lsFriendsList.size();
    }
    public class FriendsListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar;
        TextView tvUserName;
        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendsItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendsItem);
        }
    }
    public interface FriendsListCallBack
    {
        void onNameClick(String id);
    }

}
