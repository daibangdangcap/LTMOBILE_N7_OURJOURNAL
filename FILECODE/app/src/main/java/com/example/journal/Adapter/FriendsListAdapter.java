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
import com.example.journal.R;
import com.example.journal.ultils.Ultils;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {
ArrayList<FriendsList> lsFriendsList;
Context context;
FriendsListCallBack friendsListCallBack;

    public FriendsListAdapter(ArrayList<FriendsList> lsFriendsList) {
        this.lsFriendsList = lsFriendsList;
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
            holder.imgAvatar.setImageBitmap(Ultils.convertToBitmapFromAssets(context,item.getAvatar_FriendsList()));
            holder.tvUserName.setText(item.getNameFriendItem_FriendsList());
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

    }
}
