package com.example.journal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.journal.R;


public class Setting_InfoFragment extends Fragment {


    ImageView btnBack_ChangeUserInform;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting__info, container, false);
        btnBack_ChangeUserInform=view.findViewById(R.id.btnBack_ChangeInfo);
        btnBack_ChangeUserInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new SettingFragment();
                if(fragment!=null)
                {
                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_info_setting,fragment).commit();
                }
            }
        });
        return view;
    }
}