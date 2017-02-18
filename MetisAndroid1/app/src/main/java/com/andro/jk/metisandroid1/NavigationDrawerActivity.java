package com.andro.jk.metisandroid1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.andro.jk.metisandroid1.Management.ManagementHistory;
import com.andro.jk.metisandroid1.User.History;


public class NavigationDrawerActivity extends AppCompatActivity{

    FrameLayout mFrameLayout;
    int id;
    Fragment flFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_activity);

        id = getIntent().getIntExtra("id",1);

        Log.d("id",Integer.toString(id));

        mFrameLayout = (FrameLayout)findViewById(R.id.flContent);
        Log.d("nav2",SaveSharedPreference.getAccessType(this).toString());

        if(id==2){
            if(SaveSharedPreference.getAccessType(this).equals("student")){
                Log.d("nav","came here");
                Intent intent = new Intent(this, History.class);
                startActivity(intent);

            }else if (SaveSharedPreference.getAccessType(this).equals("management")){
                Log.d("nav", "came here");
                Intent intent = new Intent(this, ManagementHistory.class);
                startActivity(intent);

            }
        }else if(id==1){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        else {


            flFragment = getFragment(id);
            if (flFragment != null) {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, flFragment).commit();
            }
        }
    }

    public Fragment getFragment(int id){

        Fragment fragment = null;

        switch(id) {
            case 3:
                //Logout Activity
                if(SaveSharedPreference.getAccessToken(this).equals("missing")){
                    Log.d("SharedPrefNotSaved", "true");
                }
                else {
                    SaveSharedPreference.removeAccessToken(this);
                    Intent i = new Intent(this,LoginPreferenceManager.class);
                    startActivity(i);
                }
        }

        return null;
    }




}
