package com.example.journal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;

import java.util.ArrayList;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.FriendRequestViewHolder> {
    ArrayList<FriendsRequest> lsFriendRequest;
    Context context;
    FriendRequestCallBack friendRequestCallBack;

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRequestAdapter.FriendRequestViewHolder holder, int position) {
        FriendsRequest item=lsFriendRequest.get(position);
        holder.imgAvatar.setImageBitmap(Ultils.convertToBitmapFromAssets(context,item.getAvatar_friendrequest()));
        holder.tvUserName.setText(item.getNameuser_friendrequest());
    }

    @Override
    public int getItemCount() {
        return lsFriendRequest.size();
    }
    public class FriendRequestViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar;
        TextView tvUserName;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendsRequestItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendsRequestItem);
        }
    }
    public interface FriendRequestCallBack{

    }
}
