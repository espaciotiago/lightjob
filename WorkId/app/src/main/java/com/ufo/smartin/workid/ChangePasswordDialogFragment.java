package com.ufo.smartin.workid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import utilities.DatabaseHelper;
import utilities.JSONParser;
import utilities.User;

/**
 * Created by smartin on 07/03/2016.
 */
public class ChangePasswordDialogFragment extends DialogFragment {

    private EditText oldPass;
    private EditText newPass;
    private EditText rePass;
    private Button done;
    private String userMail,userPassword;
    DatabaseHelper db;

    public ChangePasswordDialogFragment(){

    }

    public static ChangePasswordDialogFragment newInstance(String userMail,String userPassword) {
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("mail",userMail);
        args.putString("pass",userPassword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userMail= getArguments().getString("mail");
        userPassword= getArguments().getString("pass");
        String name=userMail.split("\\.")[0];
        Log.d("TAG NAME",name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view=inflater.inflate(R.layout.dialog_change_password, container, false);
        db=new DatabaseHelper(getActivity());
        oldPass=(EditText)view.findViewById(R.id.old_pass);
        newPass=(EditText)view.findViewById(R.id.new_pass);
        rePass=(EditText)view.findViewById(R.id.re_pass);
        done=(Button)view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oldPass.getText().toString().equals("") &&
                        !newPass.getText().toString().equals("") &&
                        !rePass.getText().toString().equals("")) {
                    if (oldPass.getText().toString().equals(userPassword)) {
                        if (samePass(newPass.getText().toString(), rePass.getText().toString())) {
                            new Http_chgPass(userMail, newPass.getText().toString()).execute();
                        } else {
                            Toast.makeText(getActivity(), "Las contraseñas no coinciden",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Contraseña antigua incorrecta",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Debe llenar todos los campos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public boolean samePass(String pass1, String pass2){
        if(pass1.equals(pass2)){
            return true;
        }else{
            return false;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // AUTHENTICATE USER
    //------------------------------------------------------------------------------------------------------------------
    private class Http_chgPass extends AsyncTask<Void, Void, String>
    {
        String mail;
        String pass;
        ProgressDialog loading;

        public Http_chgPass(String mail, String pass){
            this.mail=mail;
            this.pass=pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Cambiando contraseña...",
                    "Puede tardar unos segundos", true, true);
        }

        @Override
        protected String doInBackground(Void... params)
        {   String ret=null;
            JSONParser jp= new JSONParser();
            try
            {   String url=LaunchActivity.IP+"/changePassword.php";
                String us=jp.changePassword(url,mail,pass);
                ret=us;
                return ret;

            } catch (Exception e)
            {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String info)
        {
            loading.dismiss();
            if(info!=null){
                db.updatePassUser(mail,pass);
                Toast.makeText(getActivity(), "Su contraseña ha sido cambiada",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Error cambiando la contraseña, intentelo más tarde",
                        Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }

}
