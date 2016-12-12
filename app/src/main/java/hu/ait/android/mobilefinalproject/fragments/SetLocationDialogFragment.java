package hu.ait.android.mobilefinalproject.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import hu.ait.android.mobilefinalproject.LoginActivity;
import hu.ait.android.mobilefinalproject.R;

/**
 * Created by ssheppe on 12/10/16.
 */

public class SetLocationDialogFragment extends DialogFragment {

    public static final String TAG = "SetLocationFragment";
    private Context context;
    private EditText etLocation;
    private LoginActivity loginActivity;
    private SetLocationDialogFragment setLocationDialogFragment;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.loginActivity = (LoginActivity) getActivity();
        super.onAttach(context);

        setLocationDialogFragment = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.fragment_ask_location, null);

        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);

        return alertDialogBuilder.create();
    }

    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        alertDialogBuilder.setView(dialogLayout);
        etLocation = (EditText) dialogLayout.findViewById(R.id.etLocation);

        alertDialogBuilder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleOnClickPositive();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    private void handleOnClickPositive() {
        if (TextUtils.isEmpty(etLocation.getText())) {
            etLocation.setError(getString(R.string.required));
        } else {
            String location = etLocation.getText().toString();
            if (! location.isEmpty()) {
                loginActivity.addUserLocation(location);
            }
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button positiveButton = (Button) alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClickPositive();
                }
            });
        }
    }
}
