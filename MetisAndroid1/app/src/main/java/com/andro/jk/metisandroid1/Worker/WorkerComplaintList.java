package com.andro.jk.metisandroid1.Worker;


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
import com.andro.jk.metisandroid1.Worker.WorkerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WorkerComplaintList extends AppCompatActivity {


    ProgressDialog loading;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<HistoryModel> arrayList = new ArrayList<HistoryModel>();
    String type;
    Context c;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_list);
        //arrayList = (ArrayList<HistoryModel>) getIntent().getSerializableExtra("item");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        c = this;

        loading = new ProgressDialog(WorkerComplaintList.this);

        loading.setMessage("Loading Complaints");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        //adapter = new SectorRecyclerAdapter(arrayList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(this);
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);


        RestClient.get().getWorkerComplaints(s, new Callback<List<HistoryModel>>() {
            @Override
            public void success(List<HistoryModel> list, Response response) {

                for (HistoryModel historyModel : list) {

                    if(historyModel.getStatus().equals("fixed")){

                    }else {
                        arrayList.add(historyModel);
                    }
                }

                loading.dismiss();

                    recyclerView.setAdapter(new WorkerAdapter(arrayList, c, new WorkerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(HistoryModel item, int position) {
                            Intent i = new Intent(getApplicationContext(), DescriptionActivity.class);
                            i.putExtra("item", item);
                            startActivity(i);


                        }
                    }));

                }



            @Override
            public void failure(RetrofitError error) {

                loading.dismiss();

                Toast.makeText(getApplicationContext(), "Error Loading", Toast.LENGTH_LONG).show();

            }
        });


    }
}
