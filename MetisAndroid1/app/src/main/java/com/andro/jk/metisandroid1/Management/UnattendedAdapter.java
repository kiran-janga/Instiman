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


public class UnattendedAdapter extends RecyclerView.Adapter<UnattendedAdapter.RecyclerHolder> {

    public interface OnItemClickListener {
        void onItemClick(HistoryModel item, int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<HistoryModel> arrayList;
    Context context;

    public UnattendedAdapter(ArrayList<HistoryModel> arrayList,Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unattended_piece,parent,false);
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



        TextView mucDate,mucTitle;
        ImageView mucImage;

        String[] splitTime;

        public RecyclerHolder(View itemView) {
            super(itemView);

            mucDate = (TextView) itemView.findViewById(R.id.mucDate);
            mucTitle = (TextView) itemView.findViewById(R.id.mucTitle);
            mucImage = (ImageView) itemView.findViewById(R.id.mucImage);
        }

        public void bind(final HistoryModel item, final OnItemClickListener listener, final int position) {

            splitTime = item.getTimestamp().toString().split(" ");

            mucDate.setText(splitTime[1]+" "+ splitTime[2]);
            mucTitle.setText(item.getTitle());
            Picasso.with(context).load(context.getResources().getString(R.string.IMAGE_URL)+item.getImage()).into(mucImage);



            itemView.setOnClickListener(new View.OnClickListener() {


                @Override public void onClick(View v) {
                    listener.onItemClick(item,position);

                }
            });
        }
    }

}
