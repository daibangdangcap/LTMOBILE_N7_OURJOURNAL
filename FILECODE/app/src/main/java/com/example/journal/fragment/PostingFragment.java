package com.example.journal.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.journal.Adapter.ImagesDangBaiAdapter;
import com.example.journal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class PostingFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerView;
    Button addImages_upPost;
    ArrayList<Uri> arrayListPickedImg = new ArrayList<Uri>();
    ImagesDangBaiAdapter adapter;
    private static final int Read_Permission=101;
    private static final int PICK_IMAGE=1;
    private static final int PReqCode = 1;
    private static final int REQUESCODE = 1;
    FirebaseAuth auth;
    FirebaseUser currentUser;

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
                    Uri imageuri=data.getClipData().getItemAt(i).getUri();
                    arrayListPickedImg.add(imageuri);
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                Uri imageuri=data.getData();
                arrayListPickedImg.add(imageuri);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

}