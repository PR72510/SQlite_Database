package com.example.database;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogFragment extends AppCompatDialogFragment {
    DialogListener dialogListener;
    EditText etName, etAddress, etPhone;
    MainActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.dialog, null);
        activity = MainActivity.getInstance();

        builder.setView(view)           // sets custom view to be the content of dialog
                .setTitle("Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {        // when negative button of dialog is Pressed
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {            // take data from Edit text and pass it
                        String name = etName.getText().toString();
                        String address = etAddress.getText().toString();
                        String phone = etPhone.getText().toString();

                        dialogListener.sendData(name, address, phone);
                        activity.addRow();
                    }
                });

        etName = view.findViewById(R.id.et_Name);
        etAddress = view.findViewById(R.id.et_Address);
        etPhone = view.findViewById(R.id.et_Phone);
        return builder.create();
    }

    public interface DialogListener {
        void sendData(String name, String address, String phone);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Implement DialogListener");
        }
    }
}
