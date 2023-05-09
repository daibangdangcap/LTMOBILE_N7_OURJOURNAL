package com.example.journal.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journal.Adapter.PostAdapter;
import com.example.journal.HelperClass;
import com.example.journal.MainPageActivity;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.SimpleFormatter;


public class UserpageFragment extends Fragment {

    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<Uri> arrayListPickedImg = new ArrayList<Uri>();
    StorageReference storageReference;
    Button btnChangeInfo;
    ImageView btnBack;
    ImageView imgAnhDaiDien;
    int GALLERY_REG_CODE=1000;
    ImageButton btnselectAnhDaiDien;
    //FIREBASE
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String imageAva;
    ArrayList<Post> lsPost;
    RecyclerView rvUserFragment;
    PostAdapter postAdapter;
    Button btnChinhsuathongtin_userpage;
    String fullname;
    File file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_userpage, container, false);
        btnselectAnhDaiDien=view.findViewById(R.id.btnDoiAnh);
        imgAnhDaiDien=view.findViewById(R.id.anhdaidien);
        btnChinhsuathongtin_userpage = view.findViewById(R.id.btnChinhsuathongtincanhan_trangcanhan);
        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID  = user.getUid();
        //information user
        final TextView user_fullname = (TextView) view.findViewById(R.id.tennguoidung);
        final TextView user_dob = (TextView) view.findViewById(R.id.sinhnhatinfo_trangcanhan);
        final TextView user_phone = (TextView) view.findViewById(R.id.sdtinfo_trangcanhan);
        final TextView user_gender = (TextView) view.findViewById(R.id.gioitinhinfo_trangcanhan);
        //RECYCLE VIEW
        /*btnChinhsuathongtin_userpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_userpageFragment_to_setting_InfoFragment);
            }
        });*/
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if (userProfile != null)
                {
                    fullname = userProfile.fullname;
                    String phone = userProfile.phone;
                    String gender = userProfile.gender;
                    imageAva= userProfile.image;
                    //SHOW
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

        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imgAnhDaiDien.setImageURI(result);
                storageReference=storage.getInstance().getReference("imgAvatar").child("avatar"+userID);
                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullname).setPhotoUri(uri).build();
                                database.getReference("Users").child(userID).child("image").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        //Ấn vào ảnh đại diện để xem chi tiết hơn
        imgAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xemAnhDaiDien(Gravity.CENTER);//Gravity.CENTER dùng để hiển thị dialog ở chính giữa màn hình
            }
        });
        //hàm click ảnh vào gallery
        btnselectAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    launcher.launch("image/*");
            }
        });
        btnBack=view.findViewById(R.id.btnBack_trangcanhan);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        btnChangeInfo=view.findViewById(R.id.btnChinhsuathongtincanhan_trangcanhan);
        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userpageFragment_to_setting_InfoFragment);
            }
        });
        //RECYCLE VIEW
        rvUserFragment = view.findViewById(R.id.rvUserPage);
        lsPost=new ArrayList<>();
        postAdapter=new PostAdapter(lsPost);
        LoadData();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvUserFragment.setAdapter(postAdapter);
        rvUserFragment.setLayoutManager(linearLayoutManager);

        return view;

    }
    //hàm save ảnh vào database

    private void xemAnhDaiDien(int gravity)
    {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_xemanhdaidien);
        Window window=dialog.getWindow();
        if(window==null) return;
        else{
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes=window.getAttributes();
            windowAttributes.gravity=gravity;
            window.setAttributes(windowAttributes);
            //khi mà click vào vùng khác vùng của dialog thì sẽ thoát dialog
            if(Gravity.BOTTOM!=gravity)
            {
                dialog.setCancelable(true);
            }
            else {
                dialog.setCancelable(false);
            }
        }
        ImageView imgXemAnhDD;
        imgXemAnhDD=dialog.findViewById(R.id.XemAnhDD);
        Drawable drawable = imgAnhDaiDien.getDrawable();
        if(drawable != null)
        { imgXemAnhDD.setImageDrawable(drawable);//dùng để gán ảnh từ imageview này sang imageview khác }
            dialog.show();
        }
    }

    private void LoadData()
    {
        reference=FirebaseDatabase.getInstance().getReference("Posts").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    lsPost.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}