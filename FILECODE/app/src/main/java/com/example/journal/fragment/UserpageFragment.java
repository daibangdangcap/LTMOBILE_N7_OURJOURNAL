package com.example.journal.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

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

import com.example.journal.HelperClass;
import com.example.journal.MainPageActivity;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.logging.SimpleFormatter;


public class UserpageFragment extends Fragment {
    StorageReference storageReference;
    Uri imageUri;
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
    String fullname;
    File file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_userpage, container, false);
        btnselectAnhDaiDien=view.findViewById(R.id.btnDoiAnh);
        imgAnhDaiDien=view.findViewById(R.id.anhdaidien);
        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID  = user.getUid();

        //information user
        final TextView user_fullname = (TextView) view.findViewById(R.id.tennguoidung);
        final TextView user_dob = (TextView) view.findViewById(R.id.sinhnhatinfo_trangcanhan);
        final TextView user_phone = (TextView) view.findViewById(R.id.sdtinfo_trangcanhan);
        final TextView user_gender = (TextView) view.findViewById(R.id.gioitinhinfo_trangcanhan);

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
                    user_fullname.setText(fullname);
                    user_phone.setText(phone);
                    user_gender.setText(gender);
                    //set hình ảnh từ storage
                    try {
                        file=File.createTempFile("image","jpeg");
                        StorageReference sf=FirebaseStorage.getInstance().getReferenceFromUrl("gs://journal-27ca7.appspot.com").child("imageAvatar/").child(imageAva);
                        sf.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                                imgAnhDaiDien.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        btnChangeInfo=view.findViewById(R.id.btnChinhsuathongtincanhan_trangcanhan);
        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_InfoFragment();
                FragmentTransaction fmTran = getActivity().getSupportFragmentManager().beginTransaction();
                fmTran.replace(R.id.trangchinh,fragment);
                fmTran.addToBackStack(null);
                fmTran.commit();
            }
        });
        return view;
    }
    //hàm save ảnh vào database
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==GALLERY_REG_CODE)
            {
                //nếu bằng nhau thì set imgView bằng ảnh đã chọn
                imageUri=data.getData();
                imgAnhDaiDien.setImageURI(imageUri);
            }
        }
        String filename="avatar"+userID;
        storageReference= FirebaseStorage.getInstance().getReference("imageAvatar/"+filename);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Cập nhật ảnh đại diện thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userID).child("image").setValue(filename, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                }
            });

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