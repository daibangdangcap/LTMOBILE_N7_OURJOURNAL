package com.example.journal.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.journal.MainPageActivity;
import com.example.journal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserpageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserpageFragment extends Fragment {

    ImageView btnBack;
    ImageView imgAnhDaiDien;
    int GALLERY_REG_CODE=1000;
    ImageButton btnselectAnhDaiDien;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_userpage, container, false);
        btnselectAnhDaiDien=view.findViewById(R.id.btnDoiAnh);
        imgAnhDaiDien=view.findViewById(R.id.anhdaidien);
        //Ấn vào ảnh đại diện để xem chi tiết hơn
        imgAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xemAnhDaiDien(Gravity.CENTER);//Gravity.CENTER dùng để hiển thị dialog ở chính giữa màn hình
            }
        });
        btnselectAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//vào gallery để chọn ảnh
                startActivityForResult(gallery,GALLERY_REG_CODE);
            }
        });
        btnBack=view.findViewById(R.id.btnBack_trangcanhan);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainPageActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==GALLERY_REG_CODE)
            {
                //nếu bằng nhau thì set imgView bằng ảnh đã chọn
                imgAnhDaiDien.setImageURI(data.getData());
            }
        }
    }
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
}