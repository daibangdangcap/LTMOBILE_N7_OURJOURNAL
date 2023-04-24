package com.example.journal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journal.R;


public class Setting_PolicyFragment extends Fragment {
    ImageView btnBack_Policy;
    Button btnUserPolicy;
    Button btnAppPolicy;
    Button btnOurJournalPolicy;
    TextView tvPolicy;
    ImageView imgBackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting_policy, container, false);
        btnBack_Policy=view.findViewById(R.id.btnBack_Policy);
        btnAppPolicy=view.findViewById(R.id.btnAppPolicy_Policy);
        btnUserPolicy=view.findViewById(R.id.btnUserPolicy_Policy);
        btnOurJournalPolicy=view.findViewById(R.id.btnOurJournal_Policy);
        imgBackground=view.findViewById(R.id.imgBackground_Policy);
        tvPolicy=view.findViewById(R.id.tvPolicy_Policy);
        btnBack_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Navigation.findNavController(view).navigate(R.id.action_setting_PolicyFragment_to_settingFragment);

            }
        });
        btnAppPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_setting_PolicyFragment_to_applicationPolicyFragment);

            }
        });
        btnUserPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Navigation.findNavController(view).navigate(R.id.action_setting_PolicyFragment_to_userPolicyFragment);

            }
        });
        btnOurJournalPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Navigation.findNavController(view).navigate(R.id.action_setting_PolicyFragment_to_ourJournalPolicyFragment);


            }
        });
        return view;
    }
    /*void setVisibility()
    {
        btnBack_Policy.setVisibility(View.INVISIBLE);
        btnUserPolicy.setVisibility(View.INVISIBLE);
        btnAppPolicy.setVisibility(View.INVISIBLE);
        btnOurJournalPolicy.setVisibility(View.INVISIBLE);
        tvPolicy.setVisibility(View.INVISIBLE);
        imgBackground.setVisibility(View.INVISIBLE);
    }*/
}