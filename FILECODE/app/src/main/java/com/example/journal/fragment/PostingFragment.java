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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.journal.Adapter.ImagesDangBaiAdapter;
import com.example.journal.R;

import java.util.ArrayList;


public class PostingFragment extends Fragment {
    RecyclerView recyclerView;
    Button addImages_upPost;
    ArrayList<Uri> arrayListUri=new ArrayList<Uri>();
    ImagesDangBaiAdapter adapter;
    private static final int Read_Permission=101;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posting, container, false);
        recyclerView=view.findViewById(R.id.rvListAnh_DangBai);
        adapter=new ImagesDangBaiAdapter(arrayListUri);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        recyclerView.setAdapter(adapter);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }



        addImages_upPost=view.findViewById(R.id.AddImages_UpPost);
        addImages_upPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent();
                i.setType("image/*");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2)
                {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),1);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1&&resultCode==Activity.RESULT_OK)
        {
            if(data.getClipData()!=null)
            {
                int x=data.getClipData().getItemCount();
                for(int i=0;i<x;i++)
                {
                    arrayListUri.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();

            } else if (data.getData()!=null) {
                String imageUrl=data.getData().getPath();
                arrayListUri.add(Uri.parse(imageUrl));

            }
        }
    }
}