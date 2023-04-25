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

import com.example.journal.Adapter.FriendsRequestAdapter;
import com.example.journal.MainPageActivity;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;

import java.util.ArrayList;


public class FriendRequestFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView btnBack_FriendsRequest;
    ArrayList<FriendsRequest>lsFriendRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_request, container, false);
        btnBack_FriendsRequest=view.findViewById(R.id.btnBack_FriendsRequests);
        btnBack_FriendsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainPageActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        recyclerView=view.findViewById(R.id.rvList_FriendsRequests);
        LoadData();
        FriendsRequestAdapter friendsRequestAdapter=new FriendsRequestAdapter(lsFriendRequest);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setAdapter(friendsRequestAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }
    void LoadData(){
        lsFriendRequest=new ArrayList<>();
        lsFriendRequest.add(new FriendsRequest("lily.png","lily"));
        lsFriendRequest.add(new FriendsRequest("haewon.png","haewon"));
        lsFriendRequest.add(new FriendsRequest("jinni.png","jinni"));
        lsFriendRequest.add(new FriendsRequest("bae.png","bae"));
        lsFriendRequest.add(new FriendsRequest("jiwoo.png","jiwoo"));
        lsFriendRequest.add(new FriendsRequest("sullyoon.png","sullyoon"));
        lsFriendRequest.add(new FriendsRequest("kyujin.png","kyujin"));
    }
}