package com.example.journal.fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


public class StrangeUserFragment extends Fragment {
    ImageView btnBack;
    ImageView imgAnhDaiDien;
    String iduser;
    FirebaseUser user;
    DatabaseReference databaseReference;
    Button btnAddFriend;
    public StrangeUserFragment(String iduser)
    {
        this.iduser=iduser;
    }
    public StrangeUserFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_strange_user, container, false);
        btnBack=view.findViewById(R.id.btnBack_StrangerUserPage);
        btnAddFriend=view.findViewById(R.id.btnFriendsUserPage_StrangerUserPage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID=user.getUid();
        String idstranger=getArguments().getString("id");
        imgAnhDaiDien=view.findViewById(R.id.imgAvatar_StrangerUserPage);
        final TextView user_fullname = (TextView) view.findViewById(R.id.tvUserName_StrangerUserPage);
        final TextView user_dob = (TextView) view.findViewById(R.id.DOBInfo_StrangerUserPage);
        final TextView user_phone = (TextView) view.findViewById(R.id.PhoneInfo_StrangerUserPage);
        final TextView user_gender = (TextView) view.findViewById(R.id.SexInfo_StrangerUserPage);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(idstranger).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile=snapshot.getValue(HelperClass.class);
                if(userProfile!=null)
                {
                    String fullname = userProfile.fullname;
                    String phone = userProfile.phone;
                    String gender = userProfile.gender;
                    String imageAva= userProfile.image;
                    user_fullname.setText(fullname);
                    user_phone.setText(phone);
                    user_gender.setText(gender);
                    //set hình ảnh từ storage
                    if(!imageAva.isEmpty())
                    {
                        Picasso.get().load(imageAva).placeholder(R.drawable.account_circle).into(imgAnhDaiDien);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}