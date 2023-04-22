package com.example.journal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.journal.R;

import org.w3c.dom.Text;

public class UserPolicyFragment extends Fragment {
    TextView PolicyUser_header;
    TextView contentUserPolicy;
    ImageView btnBack_UserPolicy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dieu_khoan_nguoidung, container, false);
        btnBack_UserPolicy=view.findViewById(R.id.Policyuser_ivbtback);
        PolicyUser_header=view.findViewById(R.id.Policyuser_tvheader);
        contentUserPolicy=view.findViewById(R.id.tvContentUserPolicy);
        btnBack_UserPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_PolicyFragment();
                if(fragment!=null)
                {
                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.trangchinh,fragment).commit();

                }
            }
        });
        return view;
    }
    /*void setVisibility(){
        btnBack_UserPolicy.setVisibility(View.INVISIBLE);
        PolicyUser_header.setVisibility(View.INVISIBLE);
        contentUserPolicy.setVisibility(View.INVISIBLE);
    }*/
}
