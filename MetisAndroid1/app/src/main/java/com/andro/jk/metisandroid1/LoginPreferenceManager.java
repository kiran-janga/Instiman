package com.andro.jk.metisandroid1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andro.jk.metisandroid1.Management.ManagementMainActivity;
import com.andro.jk.metisandroid1.User.FirstActivity;
import com.andro.jk.metisandroid1.Worker.WorkerComplaintList;


public class LoginPreferenceManager extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);



        if (SaveSharedPreference.getAccessToken(this) != "missing"){
            Log.d("Access_token", SaveSharedPreference.getAccessToken(LoginPreferenceManager.this));

            String mtype = SaveSharedPreference.getAccessType(this);
            if (mtype.equals("student")) {
                Log.d("test", "Success");
                Intent i = new Intent(this, FirstActivity.class);
                startActivity(i);
            }else if (mtype.equals("management")){
                Intent i = new Intent(this, ManagementMainActivity.class);
                startActivity(i);
            }else if (mtype.equals("worker")){
                Intent i = new Intent(this, WorkerComplaintList.class);
                startActivity(i);
            }else {

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }

        }
        else{
            Log.d("LoggedIn", "false");
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }



    }
}
