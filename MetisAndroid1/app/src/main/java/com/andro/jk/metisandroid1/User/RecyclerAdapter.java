package com.andro.jk.metisandroid1.User;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Saikiran on 4/19/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    public interface OnItemClickListener {
        void onItemClick(HistoryModel item,int position);
    }

    private final OnItemClickListener listener;
    List<HistoryModel> historyModels;
    private Context context;
    String strArea;

    public RecyclerAdapter(List<HistoryModel> historyModels,Context context, OnItemClickListener listener) {
        this.historyModels = historyModels;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout,parent,false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);

        return recyclerHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        holder.bind(historyModels.get(position), listener, position);

    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{



        TextView uhcDate,uhcTitle,uhcStatus;
        ImageView uhcImage;
        String[] splitTime;

        public RecyclerHolder(View itemView) {
            super(itemView);

            uhcDate = (TextView) itemView.findViewById(R.id.uhcDate);
            uhcTitle = (TextView) itemView.findViewById(R.id.uhcTitle);
            uhcStatus = (TextView) itemView.findViewById(R.id.uhcStatus);
            uhcImage = (ImageView) itemView.findViewById(R.id.uhcImage);
        }

        public void bind(final HistoryModel item, final OnItemClickListener listener, final int position) {

            splitTime = item.getTimestamp().toString().split(" ");

            uhcDate.setText(splitTime[1]+" "+ splitTime[2]);
            if(historyModels.get(position).getArea().equals("1")){
                strArea = "Acad";
            }else if(historyModels.get(position).getArea().equals("2")){
                strArea = "Hostel";
            }
            uhcTitle.setText(historyModels.get(position).getTitle());
            uhcStatus.setText(historyModels.get(position).getStatus());
            Picasso.with(context).setLoggingEnabled(true);
            Picasso.with(context).load(context.getResources().getString(R.string.IMAGE_URL) + historyModels.get(position).getImage()).into(uhcImage);


            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    listener.onItemClick(item,position);

                }
            });
        }
    }
}
