package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.journal.Adapter.FriendsListAdapter;
import com.example.journal.MainPageActivity;
import com.example.journal.Model.FriendsList;
import com.example.journal.R;

import java.util.ArrayList;


public class FriendFragment extends Fragment {

    RecyclerView rvlFriendsList;
    ArrayList<FriendsList> lsFriendsList;
    FriendsListAdapter friendsListAdapter;
    ImageView btnBack_Friends;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_friend, container, false);
        btnBack_Friends=view.findViewById(R.id.btnBack_Friends);
        btnBack_Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainPageActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        rvlFriendsList=view.findViewById(R.id.reListBanBe);
        LoadData();
        FriendsListAdapter friendsListAdapter=new FriendsListAdapter(lsFriendsList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvlFriendsList.setAdapter(friendsListAdapter);
        rvlFriendsList.setLayoutManager(linearLayoutManager);
        return view;
    }
    void LoadData(){
        lsFriendsList=new ArrayList<>();
        lsFriendsList.add(new FriendsList("lily","lily.png"));
        lsFriendsList.add(new FriendsList("haewon","haewon.png"));
        lsFriendsList.add(new FriendsList("jinni","jinni.png"));
        lsFriendsList.add(new FriendsList("bae","bae.png"));
        lsFriendsList.add(new FriendsList("jiwoo","jiwoo.png"));
        lsFriendsList.add(new FriendsList("sullyoon","sullyoon.png"));
        lsFriendsList.add(new FriendsList("kyujin","kyujin.png"));
    }
}