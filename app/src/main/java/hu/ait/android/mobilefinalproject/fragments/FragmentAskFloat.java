package hu.ait.android.mobilefinalproject.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Carolyn on 12/6/16.
 */

public class FragmentAskFloat extends DialogFragment {
    private Context context;
    private EditText amt;
    private Bundle args;
    private AddClumpDialogFragment addClumpDialogFragment;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.addClumpDialogFragment = (AddClumpDialogFragment) getTargetFragment();
        this.args = getArguments();
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.fragment_ask_float, null);

        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);

        return alertDialogBuilder.create();
    }

    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle("Add amount this user owes");

        amt = (EditText) dialogLayout.findViewById(R.id.etAmt);

        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amtString = amt.getText().toString();
                if (! amtString.isEmpty()) {
                    Float amtFloat = Float.parseFloat(amtString);
                    addClumpDialogFragment.addFriendWhoOwes(args.getString("USER"), amtFloat);
                }

                addClumpDialogFragment.addFriendWhoOwes(args.getString("USER"),Float.parseFloat(amt.getText().toString()) );
                dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }


}
