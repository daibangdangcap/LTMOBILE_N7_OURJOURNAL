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
import android.widget.TextView;
import android.widget.Toast;

import com.example.journal.Adapter.FriendsAdapter;
import com.example.journal.Adapter.FriendsListAdapter;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class FriendFragment extends Fragment{
    FirebaseUser user;
    RecyclerView rvlFriendsList;
    ArrayList<FriendsList> lsFriendsList;
    FriendsAdapter friendsAdapter;
    ImageView btnBack_Friends;
    FirebaseFirestore db;
    TextView tvFriends;
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID  = user.getUid();
        lsFriendsList=new ArrayList<>();
        tvFriends = view.findViewById(R.id.tvBanBe);
        friendsAdapter=new FriendsAdapter(lsFriendsList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvlFriendsList.setAdapter(friendsAdapter);
        rvlFriendsList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        getListFriendfromDatabase(userID);
        return view;
    }

    private void getListFriendfromDatabase(String userID)
    {
        db.collection("MyID").document(userID).collection("MyFriendsID").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        lsFriendsList.clear();
                        for(DocumentSnapshot snapshot:task.getResult())
                        {
                            FriendsList friendsList=new FriendsList(snapshot.getString("id"));
                            lsFriendsList.add(friendsList);
                            int count =0;
                            count = task.getResult().size();
                            tvFriends.setText("Bạn bè (" + Integer.toString(count)+")");
                          //  Toast.makeText(getActivity(),Integer.toString( count),Toast.LENGTH_SHORT).show();
                        }
                        friendsAdapter.notifyDataSetChanged();
                    }
                });
    }
}