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


public class AttendedAdapter extends RecyclerView.Adapter<AttendedAdapter.RecyclerHolder>{

    public interface OnItemClickListener {
        void onItemClick(HistoryModel item, int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<HistoryModel> arrayList;
    Context context;


    public AttendedAdapter(ArrayList<HistoryModel> arrayList, Context context,OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attended_piece,parent,false);
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



        TextView macDate,macTitle,macAssignedTo,macPriority;
        ImageView macImage;

        String[] splitTime;

        public RecyclerHolder(View itemView) {
            super(itemView);

            macDate = (TextView) itemView.findViewById(R.id.macDate);
            macTitle = (TextView) itemView.findViewById(R.id.macTitle);
            macAssignedTo = (TextView) itemView.findViewById(R.id.macAssignedTo);
            macPriority = (TextView) itemView.findViewById(R.id.macPriority);
            macImage = (ImageView) itemView.findViewById(R.id.macImage);
        }

        public void bind(final HistoryModel item, final OnItemClickListener listener, final int position) {

            splitTime = item.getTimestamp().toString().split(" ");

            macDate.setText(splitTime[1]+" "+ splitTime[2]);

            macTitle.setText(item.getTitle());
            macAssignedTo.setText("      "+item.getAssignedTo());

            Picasso.with(context).load(context.getResources().getString(R.string.IMAGE_URL)+item.getImage()).into(macImage);

            if (item.getPriority() == 1) {
                macPriority.setText("Routine");
            }else if (item.getPriority() == 0) {
                macPriority.setText("Routine");
            }else if (item.getPriority() == 2) {
                macPriority.setText("Important");
            }else if (item.getPriority() == 3) {
                macPriority.setText("Very Important");
            }
            itemView.setOnClickListener(new View.OnClickListener() {


                @Override public void onClick(View v) {
                    listener.onItemClick(item,position);

                }
            });
        }
    }
}
