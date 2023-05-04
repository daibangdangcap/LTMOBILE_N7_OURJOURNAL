package com.example.journal.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journal.HelperClass;
import com.example.journal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class FriendPageFragment extends Fragment {
    Button btnConfirmDelete;
    FirebaseFirestore db;
    ImageView btnBack;
    ImageView imgAnhDaiDien;
    String iduser;
    FirebaseUser user;
    DatabaseReference databaseReference;
    Button btnFriend;
    Button btnDelete;
    Button btnFriendRequest;
    String userID;
    String idstranger;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_friend_page, container, false);
        idstranger = getArguments().getString("id");
        final TextView user_fullname = (TextView) view.findViewById(R.id.tvUserName_FriendsUserPage);
        final TextView user_dob = (TextView) view.findViewById(R.id.DOBInfo_FriendsUserPage);
        final TextView user_phone = (TextView) view.findViewById(R.id.PhoneInfo_FriendsUserPage);
        final TextView user_gender = (TextView) view.findViewById(R.id.SexInfo_FriendsUserPage);
        imgAnhDaiDien=view.findViewById(R.id.imgAvatar_FriendsUserPage);
        btnBack=view.findViewById(R.id.btnBack_FriendsUserPage);
        btnFriend=view.findViewById(R.id.btnFriendsUserPage_FriendsUserPage);
        btnDelete=view.findViewById(R.id.btnDeleteFriends_FriendsUserPage);
        btnConfirmDelete=view.findViewById(R.id.btnConfirmDelete_FriendsUserPage);
        btnConfirmDelete.setVisibility(View.INVISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteFriend(Gravity.CENTER);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        return view;
    }
    private void DeleteFriend(int gravity)
    {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_deletefriend);
        Window window=dialog.getWindow();
        if(window==null) return;
        else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes=window.getAttributes();
            windowAttributes.gravity= gravity;
            window.setAttributes(windowAttributes);
            if(Gravity.BOTTOM!=gravity)
            {
                dialog.setCancelable(true);
            }
            else {
                dialog.setCancelable(false);
            }
        }
        Button btnYes;
        Button btnNo;
        btnYes=dialog.findViewById(R.id.btnYes);
        btnNo=dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setCancelable(true);
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFriend.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);
                btnConfirmDelete.setVisibility(View.VISIBLE);
                btnDelete.setText("Đã hủy kết bạn");
                db.collection("MyID").document(idstranger).collection("MyFriendsID").document(userID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                db.collection("MyID").document(userID).collection("MyFriendsID").document(idstranger).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                dialog.setCancelable(true);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}