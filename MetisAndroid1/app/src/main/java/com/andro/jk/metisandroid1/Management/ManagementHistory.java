package com.andro.jk.metisandroid1.Management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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


public class ManagementHistory extends AppCompatActivity {


    ArrayList<HistoryModel> discardedArrayList = new ArrayList<HistoryModel>();
    ArrayList<HistoryModel> fixedArrayList = new ArrayList<HistoryModel>();
    ArrayList<HistoryModel> pendingArrayList = new ArrayList<HistoryModel>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_history_activity);

        Toast.makeText(this, "This is under development", Toast.LENGTH_SHORT);

        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(this);
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);

        RestClient.get().getCategoryComplaints(s, new Callback<List<HistoryModel>>() {
            @Override
            public void success(List<HistoryModel> unattendedCards, Response response) {

                for (HistoryModel unattendedCard : unattendedCards) {
                    if (unattendedCard.getStatus().equals("rejected")) {
                        discardedArrayList.add(unattendedCard);
                    } else if (unattendedCard.getStatus().equals("fixed")){
                        fixedArrayList.add(unattendedCard);
                    }else if (unattendedCard.getStatus().equals("pending")){
                        pendingArrayList.add(unattendedCard);
                    }

                }

                ViewPager mhViewpager = (ViewPager) findViewById(R.id.mhViewpager);
                setupViewPager(mhViewpager);

                TabLayout mhSlidingTabs = (TabLayout) findViewById(R.id.mhSlidingTabs);
                mhSlidingTabs.setupWithViewPager(mhViewpager);

                Log.d("avi",discardedArrayList.toString());
                Log.d("avi2",fixedArrayList.toString());
                Log.d("avi3",fixedArrayList.toString());

            }

            @Override
            public void failure(RetrofitError error) {

                Log.d("retrofitError", error.toString());

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(ListFragment.newInstance(discardedArrayList,"history"), "rejected");
        adapter.addFrag(ListFragment.newInstance(pendingArrayList,"history"), "pending");
        adapter.addFrag(ListFragment.newInstance(fixedArrayList,"history"), "fixed");
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

}
