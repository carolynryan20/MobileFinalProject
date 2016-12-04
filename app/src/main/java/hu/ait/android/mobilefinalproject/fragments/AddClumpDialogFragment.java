package hu.ait.android.mobilefinalproject.fragments;

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
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;

/**
 * Created by Carolyn on 12/4/16.
 */

public class AddClumpDialogFragment extends DialogFragment {

    public static final String TAG = "AddCityFragment";

    private AddClumpFragmentAnswer addClumpFragmentAnswer = null;
    private EditText etClumpName;
    private ListView lvFriendsToAdd;
    private Spinner spinnerClumpType;
    private Context context;


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

        addClumpFragmentAnswer = (ClumpSummaryFragment) getParentFragment();
        //checkParentImplementsAddClumpFragmentAnswer(clumpFragment);
    }

    private void checkParentImplementsAddClumpFragmentAnswer(ClumpSummaryFragment clumpFragment) {
        if (context instanceof AddClumpFragmentAnswer) {
            addClumpFragmentAnswer = (AddClumpFragmentAnswer) clumpFragment;
        } else {
            throw new RuntimeException("Not implementing addClumpFragmentAnswer");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.fragment_add_clump_dialog, null);

        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);

        return alertDialogBuilder.create();
    }

    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle("Add a clump");
        setPositiveButton(alertDialogBuilder, dialogLayout);
        setNegativeButton(alertDialogBuilder);
    }

    private void setPositiveButton(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        etClumpName = (EditText) dialogLayout.findViewById(R.id.etClumpName);
        lvFriendsToAdd = (ListView) dialogLayout.findViewById(R.id.lvFriendsToAdd);
        spinnerClumpType = (Spinner) dialogLayout.findViewById(R.id.spinnerClumpType);

        alertDialogBuilder.setPositiveButton("Add clump", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing because the button is overridden in onStart()
            }
        });
    }

    private void setNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
            /* I override here because otherwise an error of required city couldn't be set as the dialog
             * fragment exits automatically upon click of positive button, but now it will not
             */
            Button positiveButton = (Button) alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAddClumpButtonClick();
                }
            });
        }
    }

    private void handleAddClumpButtonClick() {
        if (TextUtils.isEmpty(etClumpName.getText())) {
            etClumpName.setError("Required");
        } else {
            //Todo get actual info from dialog
            String clumpName = etClumpName.getText().toString();
            short clumpType = (short)spinnerClumpType.getBaseline();
            Friend friend = (Friend) lvFriendsToAdd.getSelectedItem();
            List<Friend> friendList = new ArrayList<>();
            //friendList.add(friend);

            addClumpFragmentAnswer.addClump(clumpName, clumpType, friendList);
            dismiss();
        }
    }

}
