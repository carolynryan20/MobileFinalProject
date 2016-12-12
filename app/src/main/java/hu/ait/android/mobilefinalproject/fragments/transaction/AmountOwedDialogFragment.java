package hu.ait.android.mobilefinalproject.fragments.transaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hu.ait.android.mobilefinalproject.R;

import static hu.ait.android.mobilefinalproject.fragments.transaction.AddTransactionDialogFragment.USER;

/**
 * AmountOwedDialogFragment.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Amoutn Owed Dialog Fragment for adding what individual friends owe
 */
public class AmountOwedDialogFragment extends DialogFragment {
    private Context context;
    private EditText amt;
    private Bundle args;
    private AddTransactionDialogFragment addTransactionDialogFragment;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.addTransactionDialogFragment = (AddTransactionDialogFragment) getTargetFragment();
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
        alertDialogBuilder.setTitle(getString(R.string.add_amt_user_owes));

        amt = (EditText) dialogLayout.findViewById(R.id.etAmt);

        alertDialogBuilder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleOnClickPositive();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    private void handleOnClickPositive() {
        if (TextUtils.isEmpty(amt.getText())) {
            amt.setError(getString(R.string.required));
        } else {
            String amtString = amt.getText().toString();
            if (! amtString.isEmpty()) {
                int amtInt = Integer.parseInt(amtString);
                addTransactionDialogFragment.addFriendWhoOwes(args.getString(USER), amtInt);
            }

            addTransactionDialogFragment.addFriendWhoOwes(args.getString(USER),Integer.parseInt(amt.getText().toString()) );
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
