package com.ufo.smartin.workid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by smartin on 07/03/2016.
 */
public class ProfessionalExperienceDialogFragment extends DialogFragment {

    private Button submit;
    private EditText place;
    private EditText position;
    private EditText iniDate;
    private EditText endDate;
    private CheckBox still;
    OnaAddSelectedP mListener;
    private int year=1994, month=0, day=1;

    public interface OnaAddSelectedP {
        public void onArticleSelectedListenerP(String place, String position,String inDate, String endDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view=inflater.inflate(R.layout.dialog_add_professional_experience, container, false);
        place=(EditText)view.findViewById(R.id.place);
        position=(EditText)view.findViewById(R.id.position);
        iniDate=(EditText)view.findViewById(R.id.start_date);
        iniDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("TOUCH", "inidate");
                    new DatePickerDialog(getActivity(), myDateListener, year, month, day).show();
                    return true;
                }
                return false;
            }
        });
        endDate=(EditText)view.findViewById(R.id.end_date);
        endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("TOUCH", "inidate");
                    new DatePickerDialog(getActivity(), myDateListenerEnd, year, month, day).show();
                    return true;
                }
                return false;
            }
        });
        still=(CheckBox)view.findViewById(R.id.still);
        still.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (still.isChecked()) {
                    endDate.setText(getString(R.string.still));
                    endDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                }else {
                    endDate.setText("Fecha fin");
                    endDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                new DatePickerDialog(getActivity(), myDateListenerEnd, year, month, day).show();
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
        });
        submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!place.getText().toString().equals("") && !position.getText().toString().equals("") &&
                        !iniDate.getText().toString().equals("") && !endDate.getText().toString().equals("")) {
                    mListener.onArticleSelectedListenerP(place.getText().toString(),
                            position.getText().toString(), iniDate.getText().toString(), endDate.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Debe llenar todos los campos para continuar",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnaAddSelectedP) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
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
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        iniDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private DatePickerDialog.OnDateSetListener myDateListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDateEnd(arg1, arg2+1, arg3);
        }
    };

    private void showDateEnd(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}
