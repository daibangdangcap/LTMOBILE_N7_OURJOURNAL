package com.example.journal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.journal.R;

public class AboutUsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView btnBack_AboutUs;
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ve_chung_toi, container, false);
        btnBack_AboutUs=view.findViewById(R.id.Aboutuspage_ivbtnback);
        btnBack_AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new SettingFragment();
                if(fragment!=null)
                {
                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_aboutus,fragment).commit();

                }
            }
        });
        return view;
    }
}
