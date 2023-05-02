package com.example.journal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.journal.Adapter.FriendsListAdapter;
import com.example.journal.HelperClass;
import com.example.journal.MainPageActivity;
import com.example.journal.Model.FriendsList;
import com.example.journal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendFragment extends Fragment implements FriendsListAdapter.FriendsListCallBack{
    FirebaseUser user;
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
                Navigation.findNavController(view).popBackStack();
            }
        });
        rvlFriendsList=view.findViewById(R.id.reListBanBe);
        lsFriendsList=new ArrayList<>();
        friendsListAdapter=new FriendsListAdapter(lsFriendsList,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvlFriendsList.setAdapter(friendsListAdapter);
        rvlFriendsList.setLayoutManager(linearLayoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID  = user.getUid();
        getListFriendfromDatabase(userID);
        return view;
    }
    /*void LoadData(){
        lsFriendsList.add(new FriendsList("lily","lily.png"));
        lsFriendsList.add(new FriendsList("haewon","haewon.png"));
        lsFriendsList.add(new FriendsList("jinni","jinni.png"));
        lsFriendsList.add(new FriendsList("bae","bae.png"));
        lsFriendsList.add(new FriendsList("jiwoo","jiwoo.png"));
        lsFriendsList.add(new FriendsList("sullyoon","sullyoon.png"));
        lsFriendsList.add(new FriendsList("kyujin","kyujin.png"));
    }*/
    private void getListFriendfromDatabase(String userID)
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    HelperClass userProFile=dataSnapshot.getValue(HelperClass.class);
                    String iduser=userProFile.id;
                    if(!iduser.contentEquals(userID))
                    {
                        FriendsList friendsList=new FriendsList(iduser,userProFile.fullname,userProFile.image);
                        lsFriendsList.add(friendsList);
                    }
                }
                friendsListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onNameClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        Navigation.findNavController(getView()).navigate(R.id.action_friendFragment_to_strangeUserFragment,bundle);
    }
}