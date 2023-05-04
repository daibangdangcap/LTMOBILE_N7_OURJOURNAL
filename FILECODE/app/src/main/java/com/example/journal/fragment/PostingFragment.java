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

import java.util.ArrayList;


public class PostingFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerView;
    Button addImages_upPost;
    ArrayList<Uri> arrayListUri=new ArrayList<Uri>();
    ImagesDangBaiAdapter adapter;
    private static final int Read_Permission=101;
    private static final int PICK_IMAGE=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posting, container, false);
        btnBack=view.findViewById(R.id.btnBack_UpPost);
        recyclerView=view.findViewById(R.id.rvListAnh_DangBai);
        adapter=new ImagesDangBaiAdapter(arrayListUri);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);




        addImages_upPost=view.findViewById(R.id.AddImages_UpPost);
        addImages_upPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
                    return;
                }
                Intent i= new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2);
                {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                //i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),PICK_IMAGE);
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
                    arrayListUri.add(imageuri);
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                Uri imageuri=data.getData();
                arrayListUri.add(imageuri);
            }
            adapter.notifyDataSetChanged();
        }
    }
}