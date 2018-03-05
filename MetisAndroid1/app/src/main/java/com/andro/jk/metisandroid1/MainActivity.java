package com.andro.jk.metisandroid1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.jk.metisandroid1.GCM.RegisterationIntentService;
import com.andro.jk.metisandroid1.Management.ManagementMainActivity;
import com.andro.jk.metisandroid1.User.FirstActivity;
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.andro.jk.metisandroid1.Worker.WorkerLogin;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

// Editing this activity using codeanywhere.

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    TextView appTitle;
    TextView id, email, token, name,workerLogin;



    boolean b = true;
    Context c;
    String mtoken;
    HashMap<String, String> fbLoginMap = new HashMap<String, String>();
    String CLIENT_ID = "8W1Z7BwwAfxx9OnxzKp4JbuXce3QsPHIa8oiIWwo";
    String CLIENT_SECRET = "z7RtqeE910eg6CIZsk4ZO0Lt1s151SUUig8oMuUFVqrxXo3VzpeZ0EynQk0nolsy9CvRGyzz4ldDqj4m2KUT9udgCqtAcf3vswhrPZqwP8CY8mLnTtR7oQVNUOUJ4RE8";

    Button userLogin, managerLogin;

    String userEmail;

    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = (TextView) findViewById(R.id.appTitle);
//        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/orator.ttf");
//        appTitle.setTypeface(typeFace);

        workerLogin = (TextView) findViewById(R.id.workerLogin);

        workerLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WorkerLogin.class);
                startActivity(i);

                finish();
            }
        });

        c = this;


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestServerAuthCode(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 0) {
            System.out.println(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("msg", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            userEmail = acct.getEmail().toString();
            if (!userEmail.contains("iitgn.ac.in")){
                signOut();
                return;
            }
            new RetrieveTokenTask().execute(acct.getEmail());


            fbLoginMap.put("grant_type", "convert_token");
            fbLoginMap.put("client_id", CLIENT_ID);
            fbLoginMap.put("client_secret", CLIENT_SECRET);
            fbLoginMap.put("backend", "google-oauth2");


        } else {

            Toast.makeText(getApplicationContext(), "Login Failed, Try again", Toast.LENGTH_SHORT).show();
            Log.d("msg", "Login Failed -- G");
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("msg7", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), 55664);
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mtoken = s;
            fbLoginMap.put("token", mtoken);
//                    Log.d("token", loginResult.getAccessToken().getToken());


            RestClient.get().loginSocialUser(fbLoginMap, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {

                    String auth_token = String.valueOf(jsonObject.get("token"));
                    String type = jsonObject.get("type").toString();

                    SaveSharedPreference.setAccessToken(c, auth_token);
                    String mtype = type.replace("\"", "");
                    SaveSharedPreference.setAccessType(c, mtype);
                    if (mtype.equals("student")) {
                        Log.d("test", "Success");
                        Intent p = new Intent(getApplicationContext(), RegisterationIntentService.class);
                        startService(p);
                        SaveSharedPreference.setUserEmail(c, userEmail);
                        loading.dismiss();
                        Intent i = new Intent(c, FirstActivity.class);
                        startActivity(i);
                    } else if (mtype.equals("management")) {
                        Intent p = new Intent(getApplicationContext(), RegisterationIntentService.class);
                        startService(p);
                        SaveSharedPreference.setUserEmail(c, userEmail);
                        loading.dismiss();
                        Intent i = new Intent(c, ManagementMainActivity.class);
                        startActivity(i);
                    }

                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    loading.dismiss();
                    Toast.makeText(c, "Login Failed, Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected void onPreExecute() {
            loading = new ProgressDialog(MainActivity.this);

            loading.setMessage("Logging In");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setIndeterminate(true);
            loading.show();
        }
    }

//    @Override
//    public void onBackPressed() {
//
//            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
//                    .setMessage("Are you sure?")
//                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }).setNegativeButton("no", null).show();
//        }


    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Use a valid IITGN email",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}






