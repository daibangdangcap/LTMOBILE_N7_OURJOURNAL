package com.example.journal.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journal.Adapter.ImagesDangBaiAdapter;
import com.example.journal.HelperClass;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PostingFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerView;
    EditText etCaption_UpPost;
    Button addImages_upPost, btnUpPost_UpPost;
    ProgressBar pbPostingProg;
    ArrayList<Uri> arrayListPickedImg = new ArrayList<Uri>();
    Uri pickedImg;
    ImagesDangBaiAdapter adapter;
    private static final int Read_Permission=101;
    private static final int PICK_IMAGE=1;
    private static final int PReqCode = 1;
    private static final int REQUESCODE = 1;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String fullname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posting, container, false);
        btnBack=view.findViewById(R.id.btnBack_UpPost);
        recyclerView=view.findViewById(R.id.rvListAnh_DangBai);
        adapter=new ImagesDangBaiAdapter(arrayListPickedImg);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID  = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                if (userProfile != null)
                {
                    fullname=userProfile.getFullname();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        etCaption_UpPost = view.findViewById(R.id.etCaption_UpPost);
        pbPostingProg = view.findViewById(R.id.pbPostingProg);
        pbPostingProg.setVisibility(View.GONE);
        btnUpPost_UpPost = view.findViewById(R.id.btnUpPost_UpPost);
        btnUpPost_UpPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etCaption_UpPost.getText().toString().isEmpty() && !arrayListPickedImg.isEmpty()){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    StorageReference imageFilePath = storageReference.child(pickedImg.getLastPathSegment());
                    imageFilePath.putFile(pickedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    Post post = new Post(etCaption_UpPost.getText().toString(), imageDownloadLink,
                                            currentUser.getUid(), fullname,"");
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Ôi không! Không thể đăng bài rồi :(", Toast.LENGTH_SHORT).show();
                                    pbPostingProg.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Hình như bạn quên thêm gì đó?", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addImages_upPost=view.findViewById(R.id.AddImages_UpPost);
        addImages_upPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(getActivity(), "Cho phép ứng dụng truy cập thư viện ảnh của bạn nhé", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
                    }
                }

                else{
                    openGallery();
                }
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

    //khi người dùng chọn ảnh
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_IMAGE&&resultCode==Activity.RESULT_OK&&null!=data)
        {
            if(data.getClipData()!=null)
            {
                int countOfImages=data.getClipData().getItemCount();
                for(int i=0;i<countOfImages;i++)
                {
                    pickedImg =data.getClipData().getItemAt(i).getUri();
                    arrayListPickedImg.add(pickedImg);
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                pickedImg =data.getData();
                arrayListPickedImg.add(pickedImg);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void addPost(Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Posts").child(currentUser.getUid()).push();

        String key = reference.getKey();
        post.setPostKey(key);

        reference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Đã đăng thành công!", Toast.LENGTH_SHORT).show();
                pbPostingProg.setVisibility(View.GONE);
            }
        });
    }

}