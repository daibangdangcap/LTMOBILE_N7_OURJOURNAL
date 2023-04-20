package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.journal.MainPageActivity;
import com.example.journal.R;


public class FriendRequestFragment extends Fragment {
    ImageView btnBack_FriendsRequest;

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
        return view;
    }
}