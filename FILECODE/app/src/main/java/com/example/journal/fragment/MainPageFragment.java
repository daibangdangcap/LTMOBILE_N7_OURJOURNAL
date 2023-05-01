package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.journal.Adapter.PostAdapter;
import com.example.journal.HelperClass;
import com.example.journal.LoginActivity;
import com.example.journal.MapActivity;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainPageFragment extends Fragment {

    ArrayList<Post> lsPost;
    RecyclerView rvlPost;
    ImageView imgOurJournal;
    View topBar;
    private DrawerLayout drawerLayout;
    ImageView imgAvatarUser_toolbar;
    NavigationView navigationView;
    AHBottomNavigation bottomNavigation;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_main_page, container, false);
        imgAvatarUser_toolbar = view.findViewById(R.id.imgUser);
        drawerLayout=view.findViewById(R.id.trangchu);
        imgOurJournal=view.findViewById(R.id.imgOurJournal);
        topBar=view.findViewById(R.id.vTopBar);
        bottomNavigation=(AHBottomNavigation) view.findViewById(R.id.bottom_navigation);
        setBottomNavigation(bottomNavigation);
        navigationView = view.findViewById(R.id.drawerView);
        //FIREBASE
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        //  INFORMATION
        final TextView user_name = (TextView) view.findViewById(R.id.tvUserName_drawermenu);
        initMenu();
        ClickButtonDrawerMenu();
        rvlPost=view.findViewById(R.id.rvPost);
        LoadData();
        PostAdapter postAdapter=new PostAdapter(lsPost);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvlPost.setAdapter(postAdapter);
        rvlPost.setLayoutManager(linearLayoutManager);

        return view;
    }
    void initMenu()
    {
        imgAvatarUser_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
                drawerLayout.openDrawer(GravityCompat.END);

            }
        });
    }
    void LoadFragment(Fragment fmNew){
        setVisibility();
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.trangchinh, fmNew);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.END);
    }
    void setVisibility()
    {
        imgAvatarUser_toolbar.setVisibility(View.INVISIBLE);
        imgOurJournal.setVisibility(View.INVISIBLE);
        topBar.setVisibility(View.INVISIBLE);
        bottomNavigation.setVisibility(View.INVISIBLE);
        rvlPost.setVisibility(View.INVISIBLE);
    }
    void setBottomNavigation(AHBottomNavigation bottomNavigation)
    {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.bt_home, R.color.yellow);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.bt_add, R.color.cyan);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.bt_search, R.color.dark_blue);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setColored(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE_FORCE);

        //notification
        AHNotification notification = new AHNotification.Builder()
                .setText("")
                .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                .setTextColor(ContextCompat.getColor(getContext(), R.color.white))
                .build();
        bottomNavigation.setNotification(notification, 1);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position)
                {
                    case 0:
                    {
                        if(wasSelected==false)
                        {
                            setVisibility_Bottom();
                            Fragment fragment=new MainPageFragment();
                            FragmentTransaction fmTran = getActivity().getSupportFragmentManager().beginTransaction();
                            fmTran.replace(R.id.trangchinh,fragment);
                            fmTran.addToBackStack(null);
                            fmTran.commit();
                        }
                        break;
                    }
                    case 1:
                    {

                        setVisibility_Bottom();
                        Fragment fragment=new PostingFragment();
                        FragmentTransaction fmTran = getActivity().getSupportFragmentManager().beginTransaction();
                        fmTran.replace(R.id.trangchinh,fragment);
                        fmTran.addToBackStack(null);
                        fmTran.commit();
                        break;
                    }
                    default: {
                        setVisibility_Bottom();
                        Fragment fragment = new SearchFragment();
                        FragmentTransaction fmTran = getActivity().getSupportFragmentManager().beginTransaction();
                        fmTran.replace(R.id.trangchinh, fragment);
                        fmTran.addToBackStack(null);
                        fmTran.commit();
                        break;
                    }
                }
                return true;
            }
        });

    }
    void setVisibility_Bottom()
    {
        imgAvatarUser_toolbar.setVisibility(View.INVISIBLE);
        imgOurJournal.setVisibility(View.INVISIBLE);
        topBar.setVisibility(View.INVISIBLE);
        rvlPost.setVisibility(View.INVISIBLE);
    }
    void LoadData()
    {
        lsPost=new ArrayList<>();
        lsPost.add(new Post("lily.png","lily","hi, im lily","lily.png"));
        lsPost.add(new Post("haewon.png","haewon","hi, im haewon","haewon.png"));
        lsPost.add(new Post("jinni.png","jinni","hi, im jinni","jinni.png"));
        lsPost.add(new Post("bae.png","bae","hi, im bae","bae.png"));
        lsPost.add(new Post("kyujin.png","kyujin","hi, im kyujin","kyujin.png"));
        lsPost.add(new Post("sullyoon.png","sullyoon","hi, im sullyoon","sullyoon.png"));
        lsPost.add(new Post("jiwoo.png","jiwoo","hi, im jiwoo","jiwoo.png"));
    }
    void ClickButtonDrawerMenu()
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.nav_Friends:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_friendFragment);
                        break;
                    case R.id.nav_FriendsRequests:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_friendRequestFragment);
                        break;
                    case R.id.nav_UserPage:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_userpageFragment);
                        break;
                    case R.id.nav_Logout:
                        Intent i=new Intent(getActivity(),LoginActivity.class);
                        startActivity(i);
                        break;
                    case R.id.nav_Map:
                        Intent intent=new Intent(getActivity(),MapActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_Setting:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_settingFragment);
                        break;
                }
                return false;
            }
        });
    }
}