package com.example.journal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.journal.Adapter.PostAdapter;
import com.example.journal.HelperClass;
import com.example.journal.LoginActivity;
import com.example.journal.MapActivity;
import com.example.journal.Model.FriendsList;
import com.example.journal.Model.FriendsRequest;
import com.example.journal.Model.Post;
import com.example.journal.R;
import com.example.journal.StarterActivity;
import com.example.journal.ultils.TranslateAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainPageFragment extends Fragment {
    public static TextView tv_count_friend;
    ArrayList<Post> lsPost;
    RecyclerView rvlPost;
    //JOURNAL
    TextView tvOurJournal;
    private String imageAva;
    Toolbar toolbar;
    View topBar;
    private DrawerLayout drawerLayout;
    ImageView imgAvatarUser_toolbar;
    NavigationView navigationView;
    BottomNavigationView menubottom;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    FirebaseDatabase database;
    FirebaseFirestore db;
    View view;
    PostAdapter postAdapter;
    ProgressBar mainpage_progressbar;
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
        database=FirebaseDatabase.getInstance();
        toolbar=view.findViewById(R.id.toolbar);
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
        db = FirebaseFirestore.getInstance();
        //  INFORMATION
        final TextView user_name = (TextView) view.findViewById(R.id.tvUserName_drawermenu);
        initMenu();
        ClickButtonDrawerMenu();
        updateNavHeader();
        lsPost=new ArrayList<>();
        rvlPost=view.findViewById(R.id.rvPost);
        postAdapter=new PostAdapter(lsPost);
        mainpage_progressbar = view.findViewById(R.id.mainpage_progressbar);
        LoadData();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rvlPost.setAdapter(postAdapter);
        rvlPost.setLayoutManager(linearLayoutManager);
        menubottom.setOnItemSelectedListener(getListener());
        //JOURNAL
        String text = "<font color =#000000>J</font><font color =#71C2CA>o</font><font color=#A5CDA7>u</font><font color=#E8DB7B>r</font><font color=#000000>nal</font>";
        tvOurJournal.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        rvlPost.setOnTouchListener(new TranslateAnimation(getActivity(),  menubottom));
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        tv_count_friend=(TextView) layoutInflater.inflate(R.layout.counter_friend,null);
        navigationView.getMenu().findItem(R.id.nav_FriendsRequests).setActionView(tv_count_friend);
        ShowCounter();
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
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_self);
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
                        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember","false");
                        editor.apply();
                        Intent i=new Intent(getActivity(), StarterActivity.class);
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
    public void ShowCounter()
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("FriendReceive").document(userID).collection("FriendRequest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot:task.getResult())
                        {
                            int count=0;
                            count = task.getResult().size();
                            if(count>0)
                            {
                                tv_count_friend.setText(Integer.toString(count));
                            }
                            else if (count ==0){
                                tv_count_friend.setText("0");
                            }
                        }
                    }
                });
    }
    private void LoadData()
    {
        //DatabaseReference ref = database.getReference("Posts");
      //  DatabaseReference ref1 = ref.getRef();
        lsPost.clear();
        reference=database.getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userPosts:snapshot.getChildren()){
                    for(DataSnapshot posts : userPosts.getChildren()){
                        Post p = posts.getValue(Post.class);
                        lsPost.add(p);
                    }
                }
                mainpage_progressbar.setVisibility(View.GONE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ListFriendID(String userID)
    {
        db.collection("MyID").document(userID).collection("MyFriendsID").get();

    }

}