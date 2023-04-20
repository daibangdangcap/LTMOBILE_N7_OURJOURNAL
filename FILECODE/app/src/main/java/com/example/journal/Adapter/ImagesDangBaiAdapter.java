package com.example.journal.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.R;

import java.util.ArrayList;

public class ImagesDangBaiAdapter extends RecyclerView.Adapter <ImagesDangBaiAdapter.ViewHolder>{
    public ArrayList<Uri> getUriArrayList() {
        return uriArrayList;
    }

    public void setUriArrayList(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public ImagesDangBaiAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    private ArrayList<Uri> uriArrayList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layoutimageitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.imgViewDangBai.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
ImageView imgViewDangBai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewDangBai=itemView.findViewById(R.id.imgAnhDangBai);
        }
    }
}
