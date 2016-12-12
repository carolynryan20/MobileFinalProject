package hu.ait.android.mobilefinalproject.fragments.friends;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.User;

/**
 * Created by Morgan on 12/7/2016.
 */
public class AddFriendDialogFragment extends DialogFragment {

    public static final String TAG = "AddFriendFragment";

    private AddFriendFragmentAnswer addFriendFragmentAnswer = null;
    private EditText etFriendUsername;
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

        addFriendFragmentAnswer = (AddFriendFragmentAnswer) getTargetFragment();
    }

    private void checkParentImplementsAddTransactionFragmentAnswer() {
        if (getFragmentManager().findFragmentByTag(FriendsFragment.TAG) instanceof AddFriendFragmentAnswer) {
            addFriendFragmentAnswer = (AddFriendFragmentAnswer) getFragmentManager().findFragmentByTag(FriendsFragment.TAG);
        } else {
            throw new RuntimeException(context.getString(R.string.noImplementFriendFragAns));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.fragment_add_friend_dialog, null);

        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);

        return alertDialogBuilder.create();
    }

    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle(context.getString(R.string.addAFriend));
        setPositiveButton(alertDialogBuilder, dialogLayout);
        setNegativeButton(alertDialogBuilder);
    }

    private void setPositiveButton(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        etFriendUsername = (EditText) dialogLayout.findViewById(R.id.etAddFriendUsername);

        alertDialogBuilder.setPositiveButton(context.getString(R.string.addFriend), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing because the button is overridden in onStart()
            }
        });
    }

    private void setNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            setupPositiveButton(alertDialog);
        }
    }

    private void setupPositiveButton(AlertDialog alertDialog) {
        Button positiveButton = (Button) alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddFriendToTransactionButtonClick();
            }
        });
    }

    private void handleAddFriendToTransactionButtonClick() {
        if (TextUtils.isEmpty(etFriendUsername.getText())) {
            etFriendUsername.setError(context.getString(R.string.required));
        } else {
            addFriendFragmentAnswer.addFriend(new Friend(
                    etFriendUsername.getText().toString(), 100, 500, User.UserIcon.CAROLYN2
            ));
            dismiss();
        }
    }
}
