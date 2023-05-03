package com.example.journal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.journal.Adapter.FriendsListAdapter;
import com.example.journal.HelperClass;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Locale;


public class SearchFragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseUser user;
    String userID;
    SearchView searchView;
    FriendsListAdapter friendsListAdapter;
    ArrayList<FriendsList> lsFriendList;
    ArrayList<FriendsList> friendsLists;
    EditText edSearch;
    ImageButton imgSearch;
    RecyclerView rvList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        edSearch=view.findViewById(R.id.edInputSearch_Search);
        imgSearch=view.findViewById(R.id.btnDelete_Seach);
        rvList=view.findViewById(R.id.rvSearch);
        lsFriendList=new ArrayList<>();
        friendsLists=new ArrayList<>();
        friendsListAdapter=new FriendsListAdapter(friendsLists);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lsFriendList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        HelperClass helperClass=dataSnapshot.getValue(HelperClass.class);
                        if(helperClass.getId().contentEquals(userID)) continue;
                        FriendsList friendsList=new FriendsList(helperClass.getId(),helperClass.getFullname(),helperClass.image);
                        lsFriendList.add(friendsList);
                    }
                    friendsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchList(edSearch.getText().toString());
            }
        });

        return view;
    }
    public void SearchList(String text)
    {
        friendsLists.clear();
        for(FriendsList friendsList: lsFriendList)
        {
            if(friendsList.getNameFriendItem_FriendsList().toLowerCase().contains(text.toLowerCase()))
            {
                friendsLists.add(friendsList);
            }
        }
        friendsListAdapter.setLsFriendsList(friendsLists);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvList.setAdapter(friendsListAdapter);
        rvList.setLayoutManager(linearLayoutManager);
    }
}