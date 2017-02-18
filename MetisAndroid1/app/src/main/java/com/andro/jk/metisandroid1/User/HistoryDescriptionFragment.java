package com.andro.jk.metisandroid1.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saikiran on 4/19/2016.
 */
public class HistoryDescriptionFragment extends Fragment {

    int id;
    HistoryModel uHistoryObject;
    ArrayList<HistoryModel> arrayList = new ArrayList<HistoryModel>();
    TextView uhdStatus,uhdArea,uhdLocation,uhdDescription,uhdDate,uhdTitle,uhdComment;
    ImageView uhdImage;
    Button clearComplaint;
    String[] splitTime;

    public static HistoryDescriptionFragment newInstance(Object object) {

        HistoryDescriptionFragment fragment = new HistoryDescriptionFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", (Serializable) object);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uHistoryObject = (HistoryModel) getArguments().getSerializable("item");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_description_fragment, container, false);


        uhdDate = (TextView) view.findViewById(R.id.uhdDate);
        uhdStatus = (TextView) view.findViewById(R.id.uhdStatus);
        uhdDescription = (TextView) view.findViewById(R.id.uhdDescription);
        uhdComment = (TextView) view.findViewById(R.id.uhdComment);
        //uhdArea = (TextView) view.findViewById(R.id.uhdArea);
        uhdLocation = (TextView) view.findViewById(R.id.uhdLocation);
        uhdTitle = (TextView) view.findViewById(R.id.uhdTitle);

        uhdImage = (ImageView) view.findViewById(R.id.uhdImage);
        clearComplaint = (Button) view.findViewById(R.id.clearComplaint);

        if(uHistoryObject.getStatus().equals("dicarded")||uHistoryObject.getStatus().equals("fixed")){
            clearComplaint.setVisibility(View.VISIBLE);
        }

        splitTime = uHistoryObject.getTimestamp().toString().split(" ");

        uhdDate.setText(splitTime[1]+" "+ splitTime[2]);

        uhdLocation.setText(uHistoryObject.getLocation());
        //uhdArea.setText(uHistoryObject.getArea());
        uhdTitle.setText(uHistoryObject.getTitle());
        uhdDescription.setText("           "+uHistoryObject.getDescription());
        uhdStatus.setText(uHistoryObject.getStatus());
        uhdComment.setText("          "+uHistoryObject.getComment());
       // uhdId.setText(Integer.toString(uHistoryObject.getId()));



        clearComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uHistoryObject.getStatus().equals("dicarded")) {
                    clearComplaint.setText("Re-upload Complaint");
                    Intent i = new Intent(getContext(), ComplaintActivity.class);
                    i.putExtra("object",uHistoryObject);
                    startActivity(i);
                } else if (uHistoryObject.getStatus().equals("fixed")) {
                    clearComplaint.setText("Complaint not fixed");
                }
            }
        });



        Picasso.with(getContext()).load(getResources().getString(R.string.IMAGE_URL)+uHistoryObject.getImage()).into(uhdImage);

        return view;

    }




}
