package com.sidharth.android.navigationdrawer.view;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sidharth.android.navigationdrawer.R;
import com.sidharth.android.navigationdrawer.controller.DatabaseHelper;
import com.sidharth.android.navigationdrawer.controller.NoteAdapter;
import com.sidharth.android.navigationdrawer.controller.RecyclerItemClickListener;
import com.sidharth.android.navigationdrawer.fragment.CalenderFragment;
import com.sidharth.android.navigationdrawer.fragment.NoteFragment;
import com.sidharth.android.navigationdrawer.fragment.SearchFragment;
import com.sidharth.android.navigationdrawer.fragment.SettingFragment;
import com.sidharth.android.navigationdrawer.model.Note;

import java.util.ArrayList;
import java.util.List;

public class  MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private TextView noNotesView;
    private DatabaseHelper db;
    private FloatingActionButton fab;
    private List<Note> noteList = new ArrayList<>();
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    private Toolbar toolbar;

    private Handler mHandler;
    private NavigationView navigationView;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private String[] activityTitles;

    // tags used to attach the fragments
    private static final String TAG_NOTE = "note";
    private static final String TAG_CALENDER = "calender";
    private static final String TAG_SEARCH = "search";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_NOTE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);
        mHandler = new Handler();

        navigationView = findViewById(R.id.nav_view);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        db = new DatabaseHelper(this);
        noteList.addAll(db.getAllNotes());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        adapter = new NoteAdapter(this,noteList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        //update and delete
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                Intent intent = new Intent(MainActivity.this, UpdateNoteActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("title",v.getTag(position)+"");
                Log.d("tit hiiiiiiiiii",v.getTag(position)+"");

                startActivity(intent);
                Toast.makeText(getApplicationContext(),"only clicked",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getApplicationContext(),"an long pressed request"+position,Toast.LENGTH_SHORT).show();
            }
        }));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //to keep the navigation bar active
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_NOTE;
            loadHomeFragment();
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_note:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_NOTE;
                        break;
                    case R.id.nav_calender:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CALENDER;
                        break;
                    case R.id.nav_all:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SEARCH;
                        break;
                    case R.id.nav_manage:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
       // setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frames,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                NoteFragment noteFragment = new NoteFragment();
                return noteFragment;
            case 1:
                // photos
                CalenderFragment calenderFragment = new CalenderFragment();
                return calenderFragment;
            case 2:
                // movies fragment
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 3:
                // settings fragment
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;
            default:
                return new NoteFragment();
        }
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void toggleFab(){
        if(navItemIndex == 0) {
            fab.show();
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            fab.hide();
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_note) {
            ft.replace(R.id.frames,new NoteFragment());
            ft.commitAllowingStateLoss();

        } else if (id == R.id.nav_calender) {
            ft.replace(R.id.frames,new CalenderFragment());
            ft.commitAllowingStateLoss();

        } else if (id == R.id.nav_all) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
