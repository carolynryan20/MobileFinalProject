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
//    private AddTransactionFragmentAnswer addClumpFragmentAnswer = null;
    private EditText etLocation;
    private LoginActivity loginActivity;
    private SetLocationDialogFragment setLocationDialogFragment;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.loginActivity = (LoginActivity) getActivity();

        super.onAttach(context);

//        addClumpFragmentAnswer = (TransactionFragment) getTargetFragment();
//        friendsWhoOwe = new HashMap<String, Float>();
//        friendsList = getArguments().getStringArrayList(FRIEND_LIST);
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
//        alertDialogBuilder.setTitle("Where are you from?");

        etLocation = (EditText) dialogLayout.findViewById(R.id.etLocation);

        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleOnClickPositive();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    private void handleOnClickPositive() {
//        Toast.makeText(getContext(), "in on positive", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(etLocation.getText())) {
            etLocation.setError("Required");
//            Toast.makeText(getContext(), "no text", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "gave text", Toast.LENGTH_SHORT).show();
            String location = etLocation.getText().toString();
            if (! location.isEmpty()) {
//                String amtFloat = location; // want to send back to loginActivity
                loginActivity.addUserLocation(location);

//                setLocationDialogFragment.addFriendWhoOwes(args.getString("USER"), amtFloat);
            }

//            setLocationDialogFragment.addFriendWhoOwes(args.getString("USER"),Float.parseFloat(amt.getText().toString()) );
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            /* I override here because otherwise an error of required city couldn't be set as the dialog
             * fragment exits automatically upon click of positive button, but now it will not
             */
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
