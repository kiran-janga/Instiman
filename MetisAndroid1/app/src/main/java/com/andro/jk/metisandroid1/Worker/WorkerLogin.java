package com.andro.jk.metisandroid1.Worker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andro.jk.metisandroid1.GCM.RegisterationIntentService;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WorkerLogin extends AppCompatActivity{

    Button login;
    EditText wEtNumber;
    String strNumber;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_login);
        c = this;

        login  = (Button) findViewById(R.id.wLogin);
        wEtNumber = (EditText) findViewById(R.id.wEtNumber);

        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                strNumber = wEtNumber.getText().toString();

                Log.d("username", strNumber);
                Log.d("password", strNumber);

                HashMap<String,String> loginMap = new HashMap<String, String>();
                loginMap.put("username",strNumber);
                loginMap.put("password",strNumber);



                RestClient.get().loginUser(loginMap, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        String auth_token = String.valueOf(jsonObject.get("token"));
                        String type = jsonObject.get("type").toString();

                        SaveSharedPreference.setAccessToken(c, auth_token);

                        String mtype = type.replace("\"", "");
                        SaveSharedPreference.setAccessType(c, mtype);


                        Intent p = new Intent(getApplicationContext(), RegisterationIntentService.class);
                        startService(p);

                        Intent i = new Intent(c, WorkerComplaintList.class);
                        startActivity(i);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //Toast.makeText()

                    }
                });
            }


        });
    }


}
