package com.andro.jk.metisandroid1.Worker;

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

/**
 * Created by Saikiran on 8/5/2016.
 */
public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.RecyclerHolder>{

    public interface OnItemClickListener {
        void onItemClick(HistoryModel item, int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<HistoryModel> arrayList;
    Context context;


    public WorkerAdapter(ArrayList<HistoryModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_piece,parent,false);
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

        TextView wccDate,wccArea,wccAssignedBy,wccPriority;
        ImageView wccImage;
        String[] splitTime;

        public RecyclerHolder(View itemView) {
            super(itemView);

            wccPriority = (TextView) itemView.findViewById(R.id.wccPriority);

            wccDate = (TextView) itemView.findViewById(R.id.wccDate);
            wccArea = (TextView) itemView.findViewById(R.id.wccArea);
            wccImage = (ImageView) itemView.findViewById(R.id.wccImage);
            wccAssignedBy = (TextView) itemView.findViewById(R.id.wccAssignedBy);
        }

        public void bind(final HistoryModel item, final OnItemClickListener listener, final int position) {

            splitTime = item.getTimestamp().toString().split(" ");

            wccDate.setText(splitTime[1]+" "+ splitTime[2]);

            //wccId.setText(Integer.toString(item.getId()));
            wccArea.setText(item.getArea());
            wccAssignedBy.setText("       "+item.getAssignedBy());
            Picasso.with(context).load(context.getResources().getString(R.string.IMAGE_URL)+item.getImage()).into(wccImage);


            if (item.getPriority() == 1) {
                wccPriority.setText("Routine");
            }else if (item.getPriority() == 0) {
                wccPriority.setText("Routine");
            }else if (item.getPriority() == 2) {
                wccPriority.setText("Important");
            }else if (item.getPriority() == 3) {
                wccPriority.setText("Very Important");
            }

            itemView.setOnClickListener(new View.OnClickListener() {


                @Override public void onClick(View v) {
                    listener.onItemClick(item,position);

                }
            });
        }
    }


}
