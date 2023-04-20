package com.example.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.journal.Adapter.PostAdapter;
import com.example.journal.Model.Post;
import com.example.journal.fragment.FriendFragment;
import com.example.journal.fragment.FriendRequestFragment;
import com.example.journal.fragment.PostingFragment;
import com.example.journal.fragment.SearchFragment;
import com.example.journal.fragment.SettingFragment;
import com.example.journal.fragment.UserpageFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    ArrayList<Post> lsPost;
    RecyclerView rvlPost;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imgOurJournal;
    View topBar;
    private DrawerLayout drawerLayout;
    ImageView imgAvatarUser_toolbar;
    NavigationView naviView;
    AHBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        imgAvatarUser_toolbar = findViewById(R.id.imgUser);
        drawerLayout=findViewById(R.id.trangchu);
        imgOurJournal=findViewById(R.id.imgOurJournal);
        topBar=findViewById(R.id.vTopBar);
        bottomNavigation=(AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setBottomNavigation(bottomNavigation);
        initMenu();
        rvlPost=findViewById(R.id.rvPost);
        LoadData();
        PostAdapter postAdapter=new PostAdapter(lsPost);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rvlPost.setAdapter(postAdapter);
        rvlPost.setLayoutManager(linearLayoutManager);
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
                naviView=findViewById(R.id.drawerView);
                naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.nav_Friends:
                                LoadFragment(new FriendFragment());
                                return true;
                            case R.id.nav_FriendsRequests:
                                LoadFragment(new FriendRequestFragment());
                                return true;
                            case R.id.nav_Logout:
                                startActivity(new Intent(MainPageActivity.this, LoginActivity.class)); //thay bằng trang đăng nhập
                                return true;
                            case R.id.nav_Map:
                                startActivity(new Intent(MainPageActivity.this, MapActivity.class)); //thay bằng trang map
                                return true;
                            case R.id.nav_Setting:
                                LoadFragment(new SettingFragment());
                                return true;
                            case R.id.nav_UserPage:
                                LoadFragment(new UserpageFragment());
                                return true;
                        }
                        return true;
                    }
                });
            }
        });
    }
    void LoadFragment(Fragment fmNew){
        setVisibility();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
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
                .setBackgroundColor(ContextCompat.getColor(MainPageActivity.this, R.color.red))
                .setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.white))
                .build();
        bottomNavigation.setNotification(notification, 1);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position)
                {
                    case 0:
                    {
                        Intent i=new Intent(MainPageActivity.this,MainPageActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 1:
                    {
                        setVisibility_Bottom();
                        Fragment fragment=new PostingFragment();
                        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
                        fmTran.replace(R.id.trangchinh,fragment);
                        fmTran.addToBackStack(null);
                        fmTran.commit();
                        break;
                    }
                    default: {
                        setVisibility_Bottom();
                        Fragment fragment = new SearchFragment();
                        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
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
        lsPost.add(new Post("lily.png","lily","hi, im lily"));
        lsPost.add(new Post("haewon.png","haewon","hi, im haewon"));
        lsPost.add(new Post("jinni.png","jinni","hi, im jinni"));
        lsPost.add(new Post("bae.png","bae","hi, im bae"));
        lsPost.add(new Post("kyujin.png","kyujin","hi, im kyujin"));
        lsPost.add(new Post("sullyoon.png","sullyoon","hi, im sullyoon"));
        lsPost.add(new Post("jiwoo.png","jiwoo","hi, im jiwoo"));
    }
}