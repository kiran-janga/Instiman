package com.andro.jk.metisandroid1.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.andro.jk.metisandroid1.Management.DescriptionActivity;
import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class History extends AppCompatActivity {
    ProgressDialog loading;
    List<HistoryModel> historyModels;
    Context c;

    RecyclerView uhRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history);
        c = this;

        uhRecyclerView = (RecyclerView) findViewById(R.id.uhRecyclerView);

        loading = new ProgressDialog(History.this);

        loading.setMessage("Loding Your History");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(History.this);
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);

        layoutManager = new LinearLayoutManager(this);
        uhRecyclerView.setHasFixedSize(true);
        uhRecyclerView.setLayoutManager(layoutManager);


        RestClient.get().getComplaints(s, new Callback<List<HistoryModel>>() {
                    @Override
                    public void success(List<HistoryModel> historyModels, Response response) {
                        Log.d("history", response.toString());

                        loading.dismiss();

                        uhRecyclerView.setAdapter(new RecyclerAdapter(historyModels, c,new RecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HistoryModel item, int position) {
                                Intent i = new Intent(getApplicationContext(), DescriptionActivity.class);
                                i.putExtra("item",item);
                                startActivity(i);


                                Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                            }
                        }));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loading.dismiss();
                    }
                });
        
    }


}
