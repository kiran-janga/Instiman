package com.andro.jk.metisandroid1.Management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.jk.metisandroid1.GCM.RegisterationIntentService;
import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.NavigationDrawerActivity;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ManagementMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    int key = 0;
    ArrayList<HistoryModel> attendedArrayList = new ArrayList<HistoryModel>();
    ArrayList<HistoryModel> unAttendedArrayList = new ArrayList<HistoryModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_drawer_included);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.registerDevice2);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), RegisterationIntentService.class);
//                startService(i);
//            }
//        });


        key = getIntent().getIntExtra("key",0);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar2,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.userEmail);
        username.setText(SaveSharedPreference.getUserEmail(this));

        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(this);
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);

        RestClient.get().getCategoryComplaints(s, new Callback<List<HistoryModel>>() {
            @Override
            public void success(List<HistoryModel> unattendedCards, Response response) {

                for (HistoryModel unattendedCard : unattendedCards) {
                    if (unattendedCard.getStatus().equals("Submitted")) {
                        unAttendedArrayList.add(unattendedCard);
                    } else if(unattendedCard.getStatus().equals("In Process")){
                        attendedArrayList.add(unattendedCard);
                    }

                }

                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager);
                viewPager.setCurrentItem(key);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void failure(RetrofitError error) {

                Log.d("retrofitError", error.toString());

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(ListFragment.newInstance(unAttendedArrayList,"unattended"), "Complaints");
        adapter.addFrag(ListFragment.newInstance(attendedArrayList,"attended"), "In Process");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int nav_id = 1;

        if (id == R.id.about) {
            nav_id = 1;


        } else if (id == R.id.history) {
            nav_id = 2;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Intent i = new Intent(ManagementMainActivity.this, NavigationDrawerActivity.class);
        i.putExtra("id", nav_id);
        startActivity(i);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        finish();
        startActivity(intent);

//        String key1 = intent.getStringExtra("key1");
//        String key2 = intent.getStringExtra("key2");
//        HistoryModel object = (HistoryModel) intent.getSerializableExtra("object");
//        if(key1.equals("Submitted") || key2.equals("In Process")){
//            unAttendedArrayList.remove(object);
//            attendedArrayList.add(object);
//        }else if(key1.equals("Submitted") || key2.equals("other")){
//            unAttendedArrayList.remove(object);
//        }else if(key1.equals("In Process") || key2.equals("In Process")){
//
//        }else if(key1.equals("In Process") || key2.equals("other")){
//            attendedArrayList.remove(object);
//        }

    }


}
