package com.ufo.smartin.workid;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import utilities.Upload;

/**
 * Created by 'Santiago on 3/7/2016.
 */
public class UploadVideoService extends IntentService {

    public static final String ACTION_FIN ="UFO.Mobile.intent.action.FIN";
    private String path;
    private String name;

    public UploadVideoService() {
        super("UploadVideoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        path = intent.getStringExtra("path");
        name = intent.getStringExtra("name");
        String a= uploadVideo();
        Log.d("MENSAJE",a);
        Intent bcIntent = new Intent();
        bcIntent.setAction(ACTION_FIN);
        bcIntent.putExtra("message",a);
        sendBroadcast(bcIntent);

    }

    private String uploadVideo()
    {
        String msg="Error";
        Upload u = new Upload();
        //String msg = u.uploadVideo(selectedPath);
        if(path!=null && !path.equals("")) {
            msg = u.uploadVideo(path,name);
        }
        return msg;
    }
}
