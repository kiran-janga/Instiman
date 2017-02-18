package com.andro.jk.metisandroid1.Management;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.squareup.picasso.Picasso;

public class ManagementHistoryDescriptionFragment extends Fragment {

    HistoryModel historyObject;
    TextView mhdDate,mhdSentBy,mhdArea,mhdDescription,mhdTitle,mhdLocation,
            mhdAssignedTo,mhdRating,mhdAssignedBy,mhdStatus,mhdFixedBy,mhdComment;


    RelativeLayout mhdaRelativeLayout,mhdwRelativeLayout;
    ImageView mhdImage;
    String[] splitTime;

    String token;
    String access_token;
    String access_token_wo_quotes;
    String s;

    public static ManagementHistoryDescriptionFragment newInstance(HistoryModel object) {

        ManagementHistoryDescriptionFragment fragment = new ManagementHistoryDescriptionFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("item",object);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyObject = (HistoryModel) getArguments().getSerializable("item");
        token = "Token ";
        access_token = SaveSharedPreference.getAccessToken(getContext());
        access_token_wo_quotes = access_token.replace("\"", "");
        s = token.concat(access_token_wo_quotes);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.management_history_description, container, false);


        mhdDate = (TextView) view.findViewById(R.id.mhdDate);
        mhdSentBy = (TextView) view.findViewById(R.id.mhdSentBy);
        mhdArea = (TextView) view.findViewById(R.id.mhdArea);
        mhdImage = (ImageView) view.findViewById(R.id.mhdImage);
        mhdDescription = (TextView) view.findViewById(R.id.mhdDescription);
        mhdTitle = (TextView) view.findViewById(R.id.mhdTitle);
        mhdLocation = (TextView) view.findViewById(R.id.mhdLocation);
        mhdAssignedTo = (TextView) view.findViewById(R.id.mhdAssignedTo);
        mhdRating = (TextView) view.findViewById(R.id.mhdRating);
        mhdAssignedBy = (TextView) view.findViewById(R.id.mhdAssignedBy);
        mhdStatus = (TextView) view.findViewById(R.id.mhdStatus);
        mhdFixedBy = (TextView) view.findViewById(R.id.mhdFixedBy);
        mhdComment = (TextView) view.findViewById(R.id.mhdComment);

        mhdaRelativeLayout = (RelativeLayout) view.findViewById(R.id.mhdaRelativeLayout);
        mhdwRelativeLayout = (RelativeLayout) view.findViewById(R.id.mhdwRelativeLayout);


        mhdTitle.setText(historyObject.getTitle());

        splitTime = historyObject.getTimestamp().toString().split(" ");
        mhdDate.setText(splitTime[1]+" "+ splitTime[2]);

        mhdSentBy.setText(historyObject.getUser());
        mhdArea.setText(historyObject.getArea());
        mhdDescription.setText("        "+historyObject.getDescription());
        mhdLocation.setText(historyObject.getLocation());
        mhdComment.setText(historyObject.getComment());
        Picasso.with(getContext()).load(getResources().getString(R.string.IMAGE_URL) + historyObject.getImage()).into(mhdImage);

        if(historyObject.getStatus().equals("fixed")){

            mhdaRelativeLayout.setVisibility(View.VISIBLE);
            mhdwRelativeLayout.setVisibility(View.VISIBLE);

            mhdAssignedTo.setText(historyObject.getAssignedTo());
            if (historyObject.getPriority() == 1) {
                mhdRating.setText("Routine");
            }else if (historyObject.getPriority() == 0) {
                mhdRating.setText("Routine");
            }else if (historyObject.getPriority() == 2) {
                mhdRating.setText("Important");
            }else if (historyObject.getPriority() == 3) {
                mhdRating.setText("Very Important");
            }

            mhdAssignedBy.setText(historyObject.getAssignedBy());
            mhdStatus.setText(historyObject.getStatus());




        }else if(historyObject.getStatus().equals("pending")) {
            mhdaRelativeLayout.setVisibility(View.VISIBLE);
            mhdFixedBy.setText("...........");

        }

        return view;

    }

}
