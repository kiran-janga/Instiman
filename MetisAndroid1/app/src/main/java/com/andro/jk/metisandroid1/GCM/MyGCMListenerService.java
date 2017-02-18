package com.andro.jk.metisandroid1.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andro.jk.metisandroid1.Management.ManagementMainActivity;
import com.andro.jk.metisandroid1.User.History;
import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.Worker.WorkerComplaintList;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

public class MyGCMListenerService extends GcmListenerService {


    int mId;
    String mtype;
    HistoryModel historyModel;
    PendingIntent pIntent;
    Intent i;


    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d("message", data.toString());


        Gson gson = new Gson();
        historyModel = gson.fromJson(data.get("complaint").toString(),HistoryModel.class);


//        final int requestID = (int) System.currentTimeMillis();
//        final int flags = PendingIntent.FLAG_CANCEL_CURRENT;

        mtype = SaveSharedPreference.getAccessType(this);

        if (mtype.equals("student")) {

//            i = new Intent(getApplicationContext(), History.class);
//            i.putExtra("historyModel",historyModel);
//            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            pIntent = PendingIntent.getActivity(getApplicationContext(), requestID, i, flags);

            int iconRes = R.drawable.logo;

            createNotification(0,iconRes,historyModel.getTitle(),"Status changed to - "+historyModel.getStatus());


        } else if (mtype.equals("management")) {

            if (historyModel.getAssignedTo()==null) {

//                i = new Intent(getApplicationContext(), ManagementMainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                i.putExtra("key",0);
//
//                pIntent = PendingIntent.getActivity(getApplicationContext(), requestID, i, flags);

                int iconRes =  R.drawable.logo;

                createNotification(0,iconRes,"New Complaint",historyModel.getTitle());

            }else {

//                i = new Intent(getApplicationContext(), ManagementMainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                pIntent = PendingIntent.getActivity(getApplicationContext(), requestID, i, flags);

                int iconRes =  R.drawable.logo;

                if(historyModel.getStatus().equals("In Process")) {

                    createNotification(0, iconRes, historyModel.getTitle(),"Woker Changed to-"+historyModel.getAssignedTo());
                }else{
                    createNotification(0, iconRes, historyModel.getTitle(),"Status-"+historyModel.getStatus());
                }

            }
        }else if (mtype.equals("worker")) {

//            i = new Intent(getApplicationContext(), WorkerComplaintList.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            pIntent = PendingIntent.getActivity(getApplicationContext(), requestID, i, flags);
            int iconRes =  R.drawable.logo;

            createNotification(0,iconRes,historyModel.getTitle(),"Status-"+historyModel.getStatus());


        }

    }

    private void createNotification(int mId, int iconRes, String title, String body) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
