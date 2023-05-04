package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.bumptech.glide.Glide;
import com.example.journal.Adapter.PostAdapter;
import com.example.journal.HelperClass;
import com.example.journal.LoginActivity;
import com.example.journal.MapActivity;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainPageFragment extends Fragment {

    ArrayList<Post> lsPost;
    RecyclerView rvlPost;
    //JOURNAL
    TextView tvOurJournal;
    private String imageAva;

    View topBar;
    private DrawerLayout drawerLayout;
    ImageView imgAvatarUser_toolbar;
    NavigationView navigationView;
    BottomNavigationView menubottom;
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
        tvOurJournal=view.findViewById(R.id.tvOurJournal);
        topBar=view.findViewById(R.id.vTopBar);
        menubottom = view.findViewById(R.id.navMenu);
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
        menubottom.setOnItemSelectedListener(getListener());
        //JOURNAL
        String text = "<font color =#000000>J</font><font color =#71C2CA>o</font><font color=#A5CDA7>u</font><font color=#E8DB7B>r</font><font color=#000000>nal</font>";
        tvOurJournal.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        updateNavHeader();
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
        tvOurJournal.setVisibility(View.INVISIBLE);
        topBar.setVisibility(View.INVISIBLE);
        rvlPost.setVisibility(View.INVISIBLE);
    }
    private NavigationBarView.OnItemSelectedListener getListener(){
        return (new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnHome:
                        //Navigation.findNavController(view).navigate(R.id.);
                        break;
                    case R.id.mnAdd:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_postingFragment);
                        break;
                    case R.id.mnSearch:
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_searchFragment);
                }
                return false;
            }
        });
    }
    void setVisibility_Bottom()
    {
        imgAvatarUser_toolbar.setVisibility(View.INVISIBLE);
        tvOurJournal.setVisibility(View.INVISIBLE);
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
    public void updateNavHeader()
    {
        NavigationView navigationview = (NavigationView) view.findViewById(R.id.drawerView);
        View headerview = navigationview.getHeaderView(0);
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass userProfile = snapshot.getValue(HelperClass.class);
                ImageView navUserPhoto = headerview.findViewById(R.id.imgAvatar_drawermenu);
                TextView navUsername = headerview.findViewById(R.id.tvUserName_drawermenu);
                imageAva= userProfile.image;
                //SHOW
                navUsername.setText(userProfile.fullname);
                //GLIDE SHOW AVATAR
                if(!imageAva.isEmpty())
                {
                    Picasso.get().load(imageAva).placeholder(R.drawable.account_circle).into(navUserPhoto);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}