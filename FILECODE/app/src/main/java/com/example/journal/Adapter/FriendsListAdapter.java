package com.example.journal.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.R;
import com.example.journal.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


//class này xài cho search fragment



public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {

    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseFirestore db;

    public void setLsFriendsList(ArrayList<FriendsList> lsFriendsList) {
        this.lsFriendsList = lsFriendsList;
    }

    ArrayList<FriendsList> lsFriendsList;
    String userID;
    int state;
    Context context;

    Bundle bundle=new Bundle();
    public FriendsListAdapter(ArrayList<FriendsList> lsFriendsList)
    {
        this.lsFriendsList=lsFriendsList;
    }

    @NonNull
    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.banbe_layout,parent,false);
        //
        FriendsListViewHolder viewHolder=new FriendsListViewHolder(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        db = FirebaseFirestore.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendsListViewHolder holder, int position) {
        holder.btnConfirm.setVisibility(View.INVISIBLE);
        holder.btnAddFriend.setVisibility(View.INVISIBLE);
        holder.btnDelete.setVisibility(View.INVISIBLE);
        FriendsList item=lsFriendsList.get(position);
        if(!item.getAvatar_FriendsList().isEmpty())
        {
            Picasso.get().load(item.getAvatar_FriendsList()).placeholder(R.drawable.account_circle).into(holder.imgAvatar);
        }

        holder.tvUserName.setText(item.getNameFriendItem_FriendsList());


        //SET BUTTON
        Map<String,Object> datafriendRequest=new HashMap<String, Object>();
        db.collection("FriendRequest").document(userID).collection("FriendReceive").whereEqualTo("id",item.getId()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                int count=0;
                                for(QueryDocumentSnapshot documentSnapshot:querySnapshot)
                                {
                                    count++;
                                }
                                if(count==0)//nếu truy vấn không có id của friend receive trong bảng FriendRequest thì set nút button là thêm bạn bè
                                {
                                    db.collection("MyID").document(userID).collection("MyFriendsID").whereEqualTo("id",item.getId()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot querySnapshot) {
                                                    if(!querySnapshot.isEmpty())
                                                    {
                                                        holder.btnAddFriend.setVisibility(View.VISIBLE);
                                                        holder.btnAddFriend.setText("Bạn bè");
                                                        holder.btnAddFriend.setBackgroundColor(Color.parseColor("#B6E2A1"));
                                                        holder.btnAddFriend.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.friendpage_checked, 0);
                                                        state=3; //"Bạn bè"
                                                        setOnClickedName(holder,3,item.getId());

                                                    }
                                                    else
                                                    {
                                                        db.collection("FriendReceive").document(userID).collection("FriendRequest").whereEqualTo("id",item.getId()).get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                                                        if(!querySnapshot.isEmpty())
                                                                        {
                                                                            state=1;//"Xác nhận" và "Hủy"
                                                                            holder.btnConfirm.setVisibility(View.VISIBLE);
                                                                            holder.btnDelete.setVisibility(View.VISIBLE);
                                                                            holder.btnConfirm.setText("Xác nhận");
                                                                            holder.btnDelete.setText("Hủy");
                                                                            holder.btnConfirm.setBackgroundColor(Color.parseColor("#B6E2A1"));
                                                                            holder.btnDelete.setBackgroundColor(Color.parseColor("#FF6666"));
                                                                            setOnClickedName(holder,1,item.getId());

                                                                        }
                                                                        else {
                                                                            holder.btnAddFriend.setVisibility(View.VISIBLE);
                                                                            holder.btnAddFriend.setText("Thêm bạn bè");
                                                                            holder.btnAddFriend.setBackgroundColor(Color.GRAY); //"Thêm bạn bè"
                                                                            state=0;
                                                                            setOnClickedName(holder,0,item.getId());

                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                                else//ngược lại thì set đã gửi lời mời
                                {
                                    holder.btnAddFriend.setVisibility(View.VISIBLE);
                                    holder.btnAddFriend.setText("Đã gửi lời mời");
                                    holder.btnAddFriend.setBackgroundColor(Color.GRAY); //"Đã gửi lời mời"
                                    state=2;
                                    setOnClickedName(holder,2,item.getId());
                                }
                            }
                        });
        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnAddFriend.getText().toString()=="Thêm bạn bè"){
                    Map<String,Object> datafriendRequest=new HashMap<String, Object>();
                    datafriendRequest.put("id",item.getId());
                    Map<String,Object> datafriendReceive=new HashMap<String, Object>();
                    datafriendReceive.put("id",userID);
                    db.collection("FriendRequest").document(userID).collection("FriendReceive").document(item.getId()).set(datafriendRequest);
                    db.collection("FriendReceive").document(item.getId()).collection("FriendRequest").document(userID).set(datafriendReceive);
                    holder.btnAddFriend.setText("Đã gửi lời mời");
                    holder.btnAddFriend.setBackgroundColor(Color.GRAY);
                    state=2;
                    setOnClickedName(holder,2,item.getId());
                }
                else if(holder.btnAddFriend.getText().toString().contentEquals("Đã gửi lời mời"))
                {
                    db.collection("FriendRequest").document(userID).collection("FriendReceive").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    db.collection("FriendReceive").document(item.getId()).collection("FriendRequest").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    holder.btnAddFriend.setText("Đã hủy lời mời");
                    setOnClickedName(holder,0,item.getId());
                }
            }
        });
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnConfirm.setText("Đã kết bạn");
                holder.btnDelete.setVisibility(View.INVISIBLE);
                db.collection("FriendRequest").document(item.getId()).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("FriendReceive").document(userID).collection("FriendRequest").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                Map<String,Object> databecomeFriend=new HashMap<String ,Object>();
                databecomeFriend.put("id",userID);
                db.collection("MyID").document(item.getId()).collection("MyFriendsID").document(userID).set(databecomeFriend);
                databecomeFriend=new HashMap<String,Object>();
                databecomeFriend.put("id",item.getId());
                db.collection("MyID").document(userID).collection("MyFriendsID").document(item.getId()).set(databecomeFriend);
                state=3;
                setOnClickedName(holder,3,item.getId());
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("FriendRequest").document(item.getId()).collection("FriendReceive").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("FriendReceive").document(userID).collection("FriendRequest").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                holder.btnConfirm.setVisibility(View.INVISIBLE);
                holder.btnDelete.setVisibility(View.INVISIBLE);
                holder.btnAddFriend.setVisibility(View.VISIBLE);
                holder.btnAddFriend.setText("Thêm bạn bè");
                holder.btnAddFriend.setBackgroundColor(Color.GRAY);
                state=0;
                setOnClickedName(holder,0,item.getId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return lsFriendsList.size();
    }
    public class FriendsListViewHolder extends RecyclerView.ViewHolder{
        Button btnConfirm;
        Button btnDelete;
        Button btnAddFriend;
        ImageView imgAvatar;
        TextView tvUserName;
        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.imgAvatar_FriendsItems);
            tvUserName=itemView.findViewById(R.id.tvNameUser_FriendsItem);
            btnAddFriend=itemView.findViewById(R.id.btnFriends_FriendsItems);
            btnConfirm=itemView.findViewById(R.id.btnFriendsRequest_FriendsItems);
            btnDelete=itemView.findViewById(R.id.btnDelete_FriendsItems);
        }
    }
    private void setOnClickedName(FriendsListViewHolder holder,int state,String id){
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("id", id);
                bundle.putInt("state",state);
                if(state!=3)Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_strangeUserFragment,bundle);
                else Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_friendPageFragment,bundle);
                bundle.clear();
            }
        });
    }
}
