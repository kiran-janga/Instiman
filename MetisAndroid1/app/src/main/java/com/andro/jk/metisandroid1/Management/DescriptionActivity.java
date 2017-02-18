package com.andro.jk.metisandroid1.Management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.User.HistoryDescriptionFragment;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.Worker.WorkerComplaintDescriptionFragment;


public class DescriptionActivity extends AppCompatActivity {

    FrameLayout mFrameLayout;
    String type;
    HistoryModel item;
    Fragment flFragment;
    String mtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_description_activity);
        mFrameLayout = (FrameLayout) findViewById(R.id.f2Content);

        mtype = SaveSharedPreference.getAccessType(this);

        if (mtype.equals("management")) {
            type = getIntent().getStringExtra("type");
            item = (HistoryModel) getIntent().getSerializableExtra("item");


            if (type.equals("attended")) {

                flFragment = AttendedDescriptionFragment.newInstance(item);

            } else if (type.equals("unattended")) {

                flFragment = UnattendedDescriptionFragment.newInstance(item);
            }else if (type.equals("history")) {

                flFragment = ManagementHistoryDescriptionFragment.newInstance(item);
            }
        } else if (mtype.equals("student")) {

                item = (HistoryModel) getIntent().getSerializableExtra("item");
                flFragment = HistoryDescriptionFragment.newInstance(item);


        }else if (mtype.equals("worker")) {

            Log.d("msg10", "this is fired");

        item = (HistoryModel) getIntent().getSerializableExtra("item");
        flFragment = WorkerComplaintDescriptionFragment.newInstance(item);


    }


            if (flFragment != null) {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.f2Content, flFragment).commit();
            }
        }

}

