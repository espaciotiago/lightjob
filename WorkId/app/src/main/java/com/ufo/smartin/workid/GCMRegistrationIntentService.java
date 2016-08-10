package com.ufo.smartin.workid;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by 'Santiago on 22/7/2016.
 */
public class GCMRegistrationIntentService extends IntentService{

    public static final String REGISTRATION_SUCCESS ="RegistrationSuccess";
    public static final String REGISTRATION_ERROR ="RegistrationError";

    public GCMRegistrationIntentService() {

        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    public void registerGCM(){
        Intent registerComplete=null;
        String token=null;
        try{
            InstanceID instanceID= InstanceID.getInstance(getApplicationContext());
            token=instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            Log.v("GCMRegistrationIntent","token: "+token);
            //notify to UI that registration complete success
            registerComplete = new Intent(REGISTRATION_SUCCESS);
            registerComplete.putExtra("token",token);
        }catch (Exception e){
            Log.v("GCMRegistrationIntent",REGISTRATION_ERROR);
            registerComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registerComplete);

    }
}
