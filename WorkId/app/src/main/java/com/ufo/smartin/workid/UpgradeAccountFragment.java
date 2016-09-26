package com.ufo.smartin.workid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class UpgradeAccountFragment extends Fragment {

    private static final String PATH ="http://lightjob.org/videos/";
    private static final int SELECT_VIDEO = 3;
    private String selectedPath;
    ProgressDialog loading;
    private String userMail;
    private String account;
    //private static final String path ="https://www.youtube.com/watch?v=WC5FdFlUcl0";

    private Button watch_video,upload_video,upgrade;
    public UpgradeAccountFragment() {
        // Required empty public constructor
    }

    public static UpgradeAccountFragment newInstance(String userMail,String account) {
        UpgradeAccountFragment fragment = new UpgradeAccountFragment();
        Bundle args = new Bundle();
        args.putString("mail",userMail);
        args.putString("account",account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userMail= getArguments().getString("mail");
        account= getArguments().getString("account");
        String name=userMail.split("\\.")[0];
        Log.d("TAG NAME",name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_upgrade, container, false);
        watch_video = (Button) view.findViewById(R.id.watch_video);
        upload_video = (Button) view.findViewById(R.id.upload);
        upgrade = (Button) view.findViewById(R.id.upgrade);

        if(account.equals("true")) {
            upgrade.setVisibility(View.GONE);
            watch_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tempname[] = userMail.split("\\@");
                    String mailDomain = tempname[1].split("\\.")[0];
                    String name=tempname[0]+"@"+mailDomain;

                    String path = PATH + name + ".mp4";
                    //String path=PATH+"msantim@hotmail.mp4";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                    intent.setDataAndType(Uri.parse(path), "video/mp4");
                    startActivity(intent);
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));
                }
            });

            upload_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseVideo();
                }
            });

            IntentFilter filter = new IntentFilter();
            filter.addAction(UploadVideoService.ACTION_FIN);
            ProgressReceiver rcv = new ProgressReceiver();
            getActivity().registerReceiver(rcv, filter);
        }else{
            upgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            watch_video.setVisibility(View.GONE);
            upload_video.setVisibility(View.GONE);
        }

        return view;
    }

    private void chooseVideo() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Remplazar video")
                .setMessage("Si ya cuenta con un video, se remplazará con uno nuevo \n ¿Desea remplazar el video?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                uploadVideo();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadVideo() {

        loading = ProgressDialog.show(getActivity(), "Subiendo video...",
                "Puede tardar unos segundos", true, true);
        loading.setCancelable(false);
        String tempname[] = userMail.split("\\@");
        String mailDomain = tempname[1].split("\\.")[0];
        String name=tempname[0]+"@"+mailDomain;

        Intent msgIntent = new Intent(getActivity(), UploadVideoService.class);
        msgIntent.putExtra("path", selectedPath);
        msgIntent.putExtra("name",name);
        getActivity().startService(msgIntent);

    }

    //--------------------------------------------------------------------------------------------

    public class ProgressReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loading.dismiss();
            if (intent.getAction().equals(UploadVideoService.ACTION_FIN)) {
                String message = intent.getStringExtra("message");
                if(message.equals("Could not upload")){
                    Toast.makeText(getActivity(), "Error subiendo el video", Toast.LENGTH_SHORT).show();
                }else if(message.equals("size limit")){

                    new AlertDialog.Builder(context)
                            .setTitle("Limite máximo de envio exedido")
                            .setMessage("El video que desea subir pesa demasiado. Intente comprimirlo antes de enviar")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_delete)
                            .show();

                } else {
                    Toast.makeText(getActivity(), "Video subido", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Error!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
