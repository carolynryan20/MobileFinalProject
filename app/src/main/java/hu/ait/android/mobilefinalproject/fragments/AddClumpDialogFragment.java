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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;

import static hu.ait.android.mobilefinalproject.fragments.ClumpFragment.FRIEND_LIST;

/**
 * Created by Carolyn on 12/4/16.
 */

public class AddClumpDialogFragment extends DialogFragment {

    public static final String TAG = "AddCityFragment";

    private AddClumpFragmentAnswer addClumpFragmentAnswer = null;
    private EditText etClumpName;
    private ListView lvFriendsToAdd;
    private Spinner spinnerClumpType;
    private Spinner spinnerWhoPaid;
    private Context context;
    private Map<String, Float> friendsWhoOwe;
    private AddClumpDialogFragment addClumpDialogFragment;
    private ArrayList<String> friendsList;
    private TextView currentLVClick;


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

        addClumpFragmentAnswer = (ClumpFragment) getTargetFragment();
        friendsWhoOwe = new HashMap<String, Float>();
        friendsList = getArguments().getStringArrayList(FRIEND_LIST);
        addClumpDialogFragment = this;
    }

    private boolean itemIsEditItem() {
        return (boolean) getArguments().get("IS_EDIT");
    }

    private void setFieldsForEditItem() {

        spinnerClumpType.setSelection((int) getArguments().get(ClumpFragment.TYPE));

        int payerPosition = 0;
        for (int i = 0; i < friendsList.size(); i++) {
            if (friendsList.get(i).equals(getArguments().get(ClumpFragment.WHO_PAID))){
                payerPosition = i;
            }
        }
        spinnerWhoPaid.setSelection(payerPosition);

        etClumpName.setText((String) getArguments().get(ClumpFragment.CLUMP_TITLE));
        //// TODO: 12/8/16 set Friends who owe upon edit
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
        ArrayList<String> typesOfClump = new ArrayList<String>() {{
            add("Food");
            add("Drinks");
            add("Rent");
            add("Travel");
            add("Other");
        }};

        setSpinnerChoices(spinnerClumpType, typesOfClump);

        spinnerWhoPaid = (Spinner) dialogLayout.findViewById(R.id.spinnerWhoPaid);
        setSpinnerChoices(spinnerClumpType, getArguments().getStringArrayList(FRIEND_LIST));

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

        List<String> friendsList = getArguments().getStringArrayList(FRIEND_LIST);
        if ((friendsList != null) && (!friendsList.isEmpty())) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, friendsList);


            lvFriendsToAdd.setAdapter(adapter);
            lvFriendsToAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    String itemValue = (String) lvFriendsToAdd.getItemAtPosition(position);

                    FragmentAskFloat fragmentAskFloat = new FragmentAskFloat();
                    fragmentAskFloat.setTargetFragment(addClumpDialogFragment, 1);
                    fragmentAskFloat.show(getFragmentManager(), AddClumpDialogFragment.TAG);
                    Bundle bundle = new Bundle();
                    bundle.putString("USER", itemValue);
                    fragmentAskFloat.setArguments(bundle);

                    currentLVClick = (TextView) view;

                }
            });

        } else {
            Toast.makeText(getContext(), "You have no friends.  Loser.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addFriendWhoOwes(String friend, Float amt) {
        friendsWhoOwe.put(friend, amt);
        currentLVClick.setText(friend+" owes $"+amt);
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

    private void setSpinnerChoices(Spinner spinner, ArrayList<String> choices) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, choices);
        spinner.setAdapter(adapter);
    }

    private void handleAddClumpButtonClick() {
        if (TextUtils.isEmpty(etClumpName.getText())) {
            etClumpName.setError("Required");
        } else {
            String clumpName = etClumpName.getText().toString();
            String userWhoPaid = spinnerClumpType.getSelectedItem().toString();
            Clump.ClumpType clumpType = Clump.ClumpType.fromInt(spinnerClumpType.getSelectedItemPosition());

            Clump toAdd = new Clump(clumpName, clumpType, userWhoPaid, friendsWhoOwe);

            if (itemIsEditItem()) {
                addClumpFragmentAnswer.addEditClump(toAdd, (String) getArguments().get("EDIT_INDEX"));
                dismiss();
            } else {
                addClumpFragmentAnswer.addClump(toAdd);
                dismiss();
            }
        }
    }
}
