package com.andro.jk.metisandroid1.Management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<HistoryModel> arrayList = new ArrayList<HistoryModel>();
    String type;
    Context c;


    public static ListFragment newInstance(ArrayList<HistoryModel> arrayList,String type) {

        ListFragment listFragment = new ListFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("item",arrayList);
        bundle.putString("type",type);
        listFragment.setArguments(bundle);
        return listFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = (ArrayList<HistoryModel>) getArguments().getSerializable("item");
        type = getArguments().getString("type");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.complaint_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        c = getActivity();


        //adapter = new SectorRecyclerAdapter(arrayList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        if(type.equals("unattended")) {
            recyclerView.setAdapter(new UnattendedAdapter(arrayList, c, new UnattendedAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(HistoryModel item, int position) {
                    Intent i = new Intent(getContext(), DescriptionActivity.class);
                    i.putExtra("type", "unattended");
                    i.putExtra("item", item);
                    startActivity(i);


                }
            }));
        }else if(type.equals("attended")) {
            recyclerView.setAdapter(new AttendedAdapter(arrayList, getContext(), new AttendedAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(HistoryModel item, int position) {
                    Intent i = new Intent(getContext(), DescriptionActivity.class);
                    i.putExtra("type", "attended");
                    i.putExtra("item", item);
                    startActivity(i);

                }
            }));
        }else if(type.equals("history")) {

            Log.d("avi3", "getting fired");
            recyclerView.setAdapter(new ManagementHistoryAdapter(arrayList, getContext(), new ManagementHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(HistoryModel item, int position) {
                    Intent i = new Intent(getContext(), DescriptionActivity.class);
                    i.putExtra("type", "history");
                    i.putExtra("item",item);
                    startActivity(i);

                }
            }));
        }

        Log.d("preinfo", arrayList.toString());

        return view;

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d("avi11", "getting fired");
//    }
}
