package hu.ait.android.mobilefinalproject.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;

/**
 * Created by Carolyn on 12/4/16.
 */

public class AddClumpDialogFragment extends DialogFragment {

    public static final String TAG = "AddCityFragment";

    private AddClumpFragmentAnswer addClumpFragmentAnswer = null;
    private EditText etClumpName;
    private EditText etWhoPaid;
    private ListView lvFriendsToAdd;
    private Spinner spinnerClumpType;
    private Context context;
    private Map<String, Float> friendsWhoOwe;
    private AddClumpDialogFragment addClumpDialogFragment;


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

        addClumpFragmentAnswer = (ClumpFragment)getTargetFragment();
        friendsWhoOwe = new HashMap<String,Float>();
        addClumpDialogFragment = this;
    }

    private boolean itemIsEditItem() {
        return (boolean) getArguments().get("IS_EDIT");
    }

    private void setFieldsForEditItem() {
        spinnerClumpType.setSelection((int)getArguments().get(ClumpFragment.TYPE));
        etClumpName.setText((String) getArguments().get(ClumpFragment.CLUMP_TITLE));
        etWhoPaid.setText((String)getArguments().get(ClumpFragment.WHO_PAID));
        //lvFriendsToAdd.set(itemToEdit.getEstimatedPriceString());
    }

    private void checkParentImplementsAddClumpFragmentAnswer() {
        if (getFragmentManager().findFragmentByTag(ClumpFragment.TAG) instanceof AddClumpFragmentAnswer) {
            addClumpFragmentAnswer = (ClumpFragment) getFragmentManager().findFragmentByTag(ClumpFragment.TAG);
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
        etClumpName = (EditText) dialogLayout.findViewById(R.id.etClumpTitle);
        setUpAddingOwes(dialogLayout);


        spinnerClumpType = (Spinner) dialogLayout.findViewById(R.id.spinnerClumpType);
        setSpinnerChoices();
        etWhoPaid = (EditText) dialogLayout.findViewById(R.id.etWhoPaid);

        if (itemIsEditItem()) {
            setFieldsForEditItem();
        }

        alertDialogBuilder.setPositiveButton("Add clump", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing because the button is overridden in onStart()
            }
        });
    }

    private void setUpAddingOwes(View dialogLayout) {
        lvFriendsToAdd = (ListView) dialogLayout.findViewById(R.id.lvFriendsToAdd);

        List<String> friendsWhoCouldBeAdded = new ArrayList<>();
        friendsWhoCouldBeAdded.add("moroz");
        friendsWhoCouldBeAdded.add("ryanc2");
        friendsWhoCouldBeAdded.add("ssheppe");
        friendsWhoCouldBeAdded.add("exampleuser");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, friendsWhoCouldBeAdded);

        lvFriendsToAdd.setAdapter(adapter);
        lvFriendsToAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String  itemValue = (String) lvFriendsToAdd.getItemAtPosition(position);

                FragmentAskFloat fragmentAskFloat = new FragmentAskFloat();
                fragmentAskFloat.setTargetFragment(addClumpDialogFragment, 1);
                fragmentAskFloat.show(getFragmentManager(), AddClumpDialogFragment.TAG);
                Bundle bundle = new Bundle();
                bundle.putString("USER", itemValue);
                fragmentAskFloat.setArguments(bundle);
            }
        });
    }

    public void addFriendWhoOwes(String friend, Float amt) {
        friendsWhoOwe.put(friend, amt);
        Toast.makeText(context, "Added Friends to Owe Map", Toast.LENGTH_SHORT).show();
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

    private void setSpinnerChoices() {
        String[] items = new String[]{"Food", "Drinks", "Rent", "Travel", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerClumpType.setAdapter(adapter);
    }

    private void handleAddClumpButtonClick() {
        if (TextUtils.isEmpty(etClumpName.getText())) {
            etClumpName.setError("Required");
        } else if (TextUtils.isEmpty(etWhoPaid.getText())){
            etWhoPaid.setError("Required");
        } else {
            //Todo get actual info from dialog
            String clumpName = etClumpName.getText().toString();
            String userWhoPaid = etWhoPaid.getText().toString();
            Clump.ClumpType clumpType = Clump.ClumpType.fromInt(spinnerClumpType.getSelectedItemPosition());

            Clump toAdd = new Clump(clumpName, clumpType, userWhoPaid, friendsWhoOwe);

            if (itemIsEditItem()) {
                addClumpFragmentAnswer.addEditClump(toAdd, (int)getArguments().get("EDIT_INDEX"));
                dismiss();
            } else {
                addClumpFragmentAnswer.addClump(toAdd);
                dismiss();
            }
        }
    }
}
