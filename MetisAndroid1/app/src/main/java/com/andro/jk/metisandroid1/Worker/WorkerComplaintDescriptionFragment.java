package com.andro.jk.metisandroid1.Worker;



import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WorkerComplaintDescriptionFragment extends Fragment {

    HistoryModel attendedObject;
    TextView wcdDate,wcdSentBy,wcdArea,wcdDescription,wcdAssignedBy,wcdAssignedTo,wcdRating,wcdTitle,wcdLocation,wcdStatus;
    ImageView wcdImage;
    ImageView wcdComplete,wcdCall;
    String[] splitTime;
    ProgressDialog loading;


    public static WorkerComplaintDescriptionFragment newInstance(Object object) {

        WorkerComplaintDescriptionFragment fragment = new WorkerComplaintDescriptionFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("item", (Serializable) object);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attendedObject = (HistoryModel) getArguments().getSerializable("item");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.worker_complaint_description, container, false);
        wcdDate = (TextView) view.findViewById(R.id.wcdDate);
        wcdSentBy = (TextView) view.findViewById(R.id.wcdSentBy);
        //wcdArea = (TextView) view.findViewById(R.id.wcdArea);
        wcdImage = (ImageView) view.findViewById(R.id.wcdImage);
        wcdDescription = (TextView) view.findViewById(R.id.wcdDescription);
        wcdAssignedBy = (TextView) view.findViewById(R.id.wcdAssignedBy);
        wcdAssignedTo = (TextView) view.findViewById(R.id.wcdAssignedTo);
        wcdRating = (TextView) view.findViewById(R.id.wcdRating);
        wcdTitle = (TextView) view.findViewById(R.id.wcdTitle);
        wcdLocation = (TextView) view.findViewById(R.id.wcdLocation);
        wcdStatus = (TextView) view.findViewById(R.id.wcdStatus);


        wcdComplete = (ImageView) view.findViewById(R.id.wcdComplete);
        wcdCall = (ImageView) view.findViewById(R.id.wcdCall);


        splitTime = attendedObject.getTimestamp().toString().split(" ");

        wcdDate.setText(splitTime[1]+" "+ splitTime[2]);



        //wcdId.setText(Integer.toString(attendedObject.getId()));
        wcdSentBy.setText(attendedObject.getUser());
        //wcdArea.setText(attendedObject.getArea());
        wcdDescription.setText("        "+attendedObject.getDescription());
        wcdAssignedTo.setText(attendedObject.getAssignedTo());
        wcdAssignedBy.setText(attendedObject.getAssignedBy());
        wcdTitle.setText(attendedObject.getTitle());
        wcdLocation.setText(attendedObject.getLocation());
        wcdStatus.setText(attendedObject.getStatus());

        Picasso.with(getContext()).load(getResources().getString(R.string.IMAGE_URL)+attendedObject.getImage()).into(wcdImage);

        wcdCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                    callIntent.setData(Uri.parse("tel:919440567195"));
//                    startActivity(callIntent);
//                } catch (ActivityNotFoundException activityException) {
//                    Toast.makeText(getContext(), "Cannot Call", Toast.LENGTH_SHORT).show();
//                    Log.e("Calling a Phone Number", "Call failed", activityException);
//                }

                Toast.makeText(getContext(),"This is under development",Toast.LENGTH_SHORT);
            }


        });

        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(getContext());
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);

        wcdComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext()).setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                loading = new ProgressDialog(getContext());
                                loading.setMessage("Updating Complaint");
                                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                loading.setIndeterminate(true);
                                loading.show();



                                RestClient.get().fixComplaint(s, attendedObject.getId(), "fixed", new Callback<JSONObject>() {
                                    @Override
                                    public void success(JSONObject jsonObject, Response response) {

                                        Intent i = new Intent(getContext(), WorkerComplaintList.class);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Updating Failed, Try After Some Time", Toast.LENGTH_SHORT).show();

                                    }

                                });
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            };
        });

        return  view;

    }
}
