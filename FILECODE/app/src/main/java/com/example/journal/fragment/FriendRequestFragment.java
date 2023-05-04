package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journal.Adapter.FriendsRequestAdapter;
import com.example.journal.HelperClass;
import com.example.journal.MainPageActivity;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FriendRequestFragment extends Fragment {
    TextView tvFriendRequest;
    FirebaseUser user;
    RecyclerView recyclerView;
    ImageView btnBack_FriendsRequest;
    ArrayList<FriendsRequest>lsFriendRequest;
    FirebaseFirestore db;
    FriendsRequestAdapter friendsRequestAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_request, container, false);
        tvFriendRequest=view.findViewById(R.id.tvFriendsRequests);
        btnBack_FriendsRequest=view.findViewById(R.id.btnBack_FriendsRequests);
        btnBack_FriendsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        recyclerView=view.findViewById(R.id.rvList_FriendsRequests);
        lsFriendRequest=new ArrayList<>();
        friendsRequestAdapter=new FriendsRequestAdapter(lsFriendRequest);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setAdapter(friendsRequestAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID  = user.getUid();
        db = FirebaseFirestore.getInstance();
        getListRequestFromDatabase(userID);
        return view;
    }
    /*void LoadData(){
        lsFriendRequest=new ArrayList<>();
        lsFriendRequest.add(new FriendsRequest("lily.png","lily"));
        lsFriendRequest.add(new FriendsRequest("haewon.png","haewon"));
        lsFriendRequest.add(new FriendsRequest("jinni.png","jinni"));
        lsFriendRequest.add(new FriendsRequest("bae.png","bae"));
        lsFriendRequest.add(new FriendsRequest("jiwoo.png","jiwoo"));
        lsFriendRequest.add(new FriendsRequest("sullyoon.png","sullyoon"));
        lsFriendRequest.add(new FriendsRequest("kyujin.png","kyujin"));
    }*/
    private void getListRequestFromDatabase(String userID)
    {
            db.collection("FriendReceive").document(userID).collection("FriendRequest").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            lsFriendRequest.clear();
                            for(DocumentSnapshot snapshot:task.getResult())
                            {
                                FriendsRequest friendsRequest=new FriendsRequest(snapshot.getString("id"));
                                lsFriendRequest.add(friendsRequest);
                                int count =0;
                                count = task.getResult().size();
                                tvFriendRequest.setText("Lời mời kết bạn (" + Integer.toString(count)+")");
                            }
                            friendsRequestAdapter.notifyDataSetChanged();
                        }
                    });
    }
}