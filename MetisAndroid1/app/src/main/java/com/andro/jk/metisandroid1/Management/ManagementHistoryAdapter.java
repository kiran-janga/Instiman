package com.andro.jk.metisandroid1.Management;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ManagementHistoryAdapter extends RecyclerView.Adapter<ManagementHistoryAdapter.RecyclerHolder> {

    public interface OnItemClickListener {
        void onItemClick(HistoryModel item, int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<HistoryModel> arrayList;
    Context context;

    public ManagementHistoryAdapter(ArrayList<HistoryModel> arrayList, Context context,OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_history_piece,parent,false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);

        return recyclerHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        holder.bind(arrayList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{



        TextView mhcDate,mhcTitle,mhcAssignedBy,mhcStatus,mhcPriority;
        ImageView mhcImage;
        String[] splitTime;

        public RecyclerHolder(View itemView) {
            super(itemView);

            mhcDate = (TextView) itemView.findViewById(R.id.mhcDate);
            mhcTitle = (TextView) itemView.findViewById(R.id.mhcTitle);
            mhcStatus = (TextView) itemView.findViewById(R.id.mhcStatus);
            mhcAssignedBy = (TextView) itemView.findViewById(R.id.mhcAssignedBy);
            mhcPriority = (TextView) itemView.findViewById(R.id.mhcPriority);
            mhcImage = (ImageView) itemView.findViewById(R.id.mhcImage);
        }

        public void bind(final HistoryModel item, final OnItemClickListener listener, final int position) {

//            mhcDate.setText(Integer.toString(item.getId()));
            mhcTitle.setText(item.getTitle());
            mhcAssignedBy.setText(item.getAssignedBy());
            mhcStatus.setText(item.getStatus());

            splitTime = item.getTimestamp().toString().split(" ");

            mhcDate.setText(splitTime[1]+" "+ splitTime[2]);

            Picasso.with(context).load(context.getResources().getString(R.string.IMAGE_URL)+item.getImage()).resize(350,350).centerInside().into(mhcImage);

            if (item.getPriority() == 1) {
                mhcPriority.setText("Routine");
            }else if (item.getPriority() == 0) {
                mhcPriority.setText("Routine");
            }else if (item.getPriority() == 2) {
                mhcPriority.setText("Important");
            }else if (item.getPriority() == 3) {
                mhcPriority.setText("Very Important");
            }
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {
                    listener.onItemClick(item,position);

                }
            });
        }
    }
}
