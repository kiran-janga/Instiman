package com.andro.jk.metisandroid1.GCM;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        Intent intent = new Intent(this,RegisterationIntentService.class);
        startService(intent);

    }
}
