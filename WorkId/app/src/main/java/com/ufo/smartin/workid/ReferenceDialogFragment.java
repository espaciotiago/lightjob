package com.ufo.smartin.workid;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by smartin on 07/04/2016.
 */
public class ReferenceDialogFragment extends DialogFragment {

    private Button submit;
    private Spinner type;
    private EditText name;
    private EditText relation;
    private EditText ocupation;
    private EditText contact;
    OnaAddSelected mListener;

    public interface OnaAddSelected {
        public void onArticleSelectedListener(int type, String name,String relation, String ocupation,String contact);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view=inflater.inflate(R.layout.dialog_add_reference, container, false);
        name=(EditText)view.findViewById(R.id.name);
        relation=(EditText)view.findViewById(R.id.relation);
        ocupation=(EditText)view.findViewById(R.id.occupation);
        contact=(EditText)view.findViewById(R.id.contact);
        type=(Spinner)view.findViewById(R.id.type);
        String[] types =getResources().getStringArray(R.array.references);
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, types);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(degreeAdapter);
        submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ty=type.getSelectedItemPosition()-1;

                if(ty!=-1 && !name.getText().toString().equals("") &&
                        !relation.getText().toString().equals("") &&
                        !ocupation.getText().toString().equals("") &&
                        !contact.getText().toString().equals("")) {
                    mListener.onArticleSelectedListener(ty, name.getText().toString(),
                            relation.getText().toString(), ocupation.getText().toString(), contact.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Debe llenar todos los campos para continuar",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnaAddSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
