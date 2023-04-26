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

import com.example.journal.R;
public class OurJournalPolicyFragment extends Fragment {
    ImageView btnBack_OurJounalPolicy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chinh_sach_ourjournal, container, false);
        btnBack_OurJounalPolicy=view.findViewById(R.id.Policyourjournal_ivbtback);
        btnBack_OurJounalPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_PolicyFragment();
                if(fragment!=null)
                {
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });
        return view;
    }
}
