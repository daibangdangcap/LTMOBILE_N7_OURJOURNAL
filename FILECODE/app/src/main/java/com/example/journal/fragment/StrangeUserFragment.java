package com.example.journal.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journal.HelperClass;
import com.example.journal.Model.FriendsList;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class StrangeUserFragment extends Fragment {
    FirebaseFirestore db;
    ImageView btnBack;
    ImageView imgAnhDaiDien;
    String iduser;
    FirebaseUser user;
    DatabaseReference databaseReference;
    Button btnAddFriend;
    Button btnConfirm;
    Button btnDelete;
    Button btnFriendRequest;
    public StrangeUserFragment(String iduser)
    {
        this.iduser=iduser;
    }
    public StrangeUserFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_strange_user, container, false);
        btnConfirm = view.findViewById(R.id.btnConfirm_StrangerUserPage);
        btnDelete = view.findViewById(R.id.btnDelete_StrangerUserPage);
        btnFriendRequest = view.findViewById(R.id.btnFriendsRequest_StrangerUserPage);
        btnBack = view.findViewById(R.id.btnBack_StrangerUserPage);
        btnAddFriend = view.findViewById(R.id.btnFriendsUserPage_StrangerUserPage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        String idstranger = getArguments().getString("id");
        int state = getArguments().getInt("state");
        imgAnhDaiDien = view.findViewById(R.id.imgAvatar_StrangerUserPage);
        final TextView user_fullname = (TextView) view.findViewById(R.id.tvUserName_StrangerUserPage);
        final TextView user_dob = (TextView) view.findViewById(R.id.DOBInfo_StrangerUserPage);
        final TextView user_phone = (TextView) view.findViewById(R.id.PhoneInfo_StrangerUserPage);
        final TextView user_gender = (TextView) view.findViewById(R.id.SexInfo_StrangerUserPage);
        db = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(idstranger).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if (userProfile != null) {
                    String fullname = userProfile.fullname;
                    String phone = userProfile.phone;
                    String gender = userProfile.gender;
                    String imageAva = userProfile.image;
                    user_fullname.setText(fullname);
                    user_phone.setText(phone);
                    user_gender.setText(gender);
                    //set hình ảnh từ storage
                    if (!imageAva.isEmpty()) {
                        Picasso.get().load(imageAva).placeholder(R.drawable.account_circle).into(imgAnhDaiDien);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (state == 1)
        {
                btnFriendRequest.setVisibility(View.INVISIBLE);
                btnAddFriend.setVisibility(View.INVISIBLE);
         }
        else if(state==2)
        {
                btnConfirm.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);
                btnAddFriend.setVisibility(View.INVISIBLE);
        }
        else {
            btnConfirm.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnFriendRequest.setVisibility(View.INVISIBLE);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> datafriendRequest=new HashMap<String, Object>();
                datafriendRequest.put("id",userID);
                Map<String,Object> datafriendReceive=new HashMap<String, Object>();
                datafriendReceive.put("id",idstranger);
                db.collection("FriendRequest").document(userID).collection("FriendReceive").document(idstranger).set(datafriendRequest);
                db.collection("FriendReceive").document(idstranger).collection("FriendRequest").document(userID).set(datafriendReceive);
                btnAddFriend.setText("Đã gửi lời mời");
                btnAddFriend.setBackgroundColor(Color.GRAY);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnConfirm.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);
                btnAddFriend.setVisibility(View.VISIBLE);
                btnAddFriend.setText("Bạn bè");
                int color=Color.argb(0,144,238,144);
                btnAddFriend.setBackgroundColor(color);
                Map<String,Object> databecomeFriend=new HashMap<String ,Object>();
                databecomeFriend.put("id",userID);
                db.collection("MyID").document(idstranger).collection("MyFriendsID").document(userID).set(databecomeFriend);
                databecomeFriend=new HashMap<String,Object>();
                databecomeFriend.put("id",idstranger);
                db.collection("MyID").document(userID).collection("MyFriendsID").document(idstranger).set(databecomeFriend);
                db.collection("FriendRequest").document(idstranger).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("FriendReceive").document(userID).collection("FriendRequest").document(idstranger).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDelete.setVisibility(View.INVISIBLE);
                btnConfirm.setVisibility(View.INVISIBLE);
                btnAddFriend.setVisibility(View.VISIBLE);
                db.collection("FriendRequest").document(idstranger).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("FriendReceive").document(userID).collection("FriendRequest").document(idstranger).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
        return view;
    }
}