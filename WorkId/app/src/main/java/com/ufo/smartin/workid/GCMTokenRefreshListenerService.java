package com.ufo.smartin.workid;

import android.content.Intent;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by 'Santiago on 22/7/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService{

    //When token refresh, start service
    @Override
    public void onTokenRefresh() {
        Intent intent=new Intent(this,GCMRegistrationIntentService.class);
        startService(intent);
    }
}
