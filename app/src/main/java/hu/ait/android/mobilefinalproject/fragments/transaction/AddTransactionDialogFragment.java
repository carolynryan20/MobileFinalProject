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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.model.Transaction;

import static hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment.EDIT_INDEX;
import static hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment.FRIEND_LIST;
import static hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment.IS_EDIT;


/**
 * AddTransactionDialogFragment.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Dialog Fragment for adding individual transactions
 */
public class AddTransactionDialogFragment extends DialogFragment {

    public static final String TAG = "AddCityFragment";
    public static final String USER = "USER";

    private AddTransactionFragmentAnswer addTransactionFragmentAnswer = null;
    private EditText etTransactionName;
    private ListView lvFriendsToAdd;
    private Spinner spinnerTransactionType;
    private Context context;
    private Map<String, Integer> friendsWhoOwe;
    private AddTransactionDialogFragment addTransactionDialogFragment;
    private ArrayList<String> friendsList;
    private TextView currentLVClick;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        addTransactionFragmentAnswer = (TransactionFragment) getTargetFragment();
        friendsWhoOwe = new HashMap<>();
        friendsList = getArguments().getStringArrayList(FRIEND_LIST);
        addTransactionDialogFragment = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.fragment_add_transaction_dialog, null);

        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);

        return alertDialogBuilder.create();
    }

    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        setUpDialogView(alertDialogBuilder, dialogLayout);
        setPositiveButton(alertDialogBuilder, dialogLayout);
        setNegativeButton(alertDialogBuilder);
    }

    private void setUpDialogView(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle(R.string.add_a_transaction);

        etTransactionName = (EditText) dialogLayout.findViewById(R.id.etTransactionTitle);
        setUpAddingOwes(dialogLayout);
        setTransactionTypeSpinner(dialogLayout);
    }

    private void setPositiveButton(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        String positiveButton = getString(R.string.add_transaction);
        positiveButton = checkItemIsEditItem(positiveButton);
        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing because the button is overridden in onStart()
            }
        });
    }

    private String checkItemIsEditItem(String positiveButton) {
        if (itemIsEditItem()) {
            spinnerTransactionType.setSelection((int) getArguments().get(TransactionFragment.TYPE));
            positiveButton = getString(R.string.save);
        }
        return positiveButton;
    }

    private boolean itemIsEditItem() {
        return getArguments().getBoolean(IS_EDIT);
    }

    private void setTransactionTypeSpinner(View dialogLayout) {
        spinnerTransactionType = (Spinner) dialogLayout.findViewById(R.id.spinnerTransactionType);
        ArrayList<String> typesOfTransaction = new ArrayList<String>() {{
            add(getString(R.string.food));
            add(getString(R.string.drink));
            add(getString(R.string.rent));
            add(getString(R.string.travel));
            add(getString(R.string.other));
        }};

        ArrayAdapter<String> adapterTransaction = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typesOfTransaction);
        spinnerTransactionType.setAdapter(adapterTransaction);
    }

    private void setUpAddingOwes(View dialogLayout) {
        lvFriendsToAdd = (ListView) dialogLayout.findViewById(R.id.lvFriendsToAdd);
        if ((friendsList != null) && (!friendsList.isEmpty())) {
            setArrayAdapterUsingFriends(friendsList);
        } else {
            Toast.makeText(getContext(), R.string.no_friends, Toast.LENGTH_SHORT).show();
        }
    }

    private void setArrayAdapterUsingFriends(List<String> friendsList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, friendsList);
        setUpFriendsToAdd(adapter);
    }

    private void setUpFriendsToAdd(ArrayAdapter<String> adapter) {
        lvFriendsToAdd.setAdapter(adapter);
        lvFriendsToAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentLVClick = (TextView) view;
                String itemValue = (String) lvFriendsToAdd.getItemAtPosition(position);
                openAmountOwedDialogFragment(itemValue);
            }
        });
    }

    private void openAmountOwedDialogFragment(String itemValue) {
        AmountOwedDialogFragment amountOwedDialogFragment = new AmountOwedDialogFragment();
        addBundleToAmtOwed(itemValue, amountOwedDialogFragment);
        amountOwedDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);
    }

    private void addBundleToAmtOwed(String itemValue, AmountOwedDialogFragment amountOwedDialogFragment) {
        amountOwedDialogFragment.setTargetFragment(addTransactionDialogFragment, 1);
        Bundle bundle = new Bundle();
        bundle.putString(USER, itemValue);
        amountOwedDialogFragment.setArguments(bundle);
    }

    public void addFriendWhoOwes(String friend, Integer amt) {
        friendsWhoOwe.put(friend, amt);
        currentLVClick.setText(friend + getString(R.string.owes) + amt  + getString(R.string.ft));
    }

    private void setNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                    handleAddTransactionButtonClick();
                }
            });
        }
    }

    private void handleAddTransactionButtonClick() {
        if (TextUtils.isEmpty(etTransactionName.getText())) {
            etTransactionName.setError(getString(R.string.required));
        } else {
            String transactionName = etTransactionName.getText().toString();
            Transaction.TransactionType transactionType = Transaction.TransactionType.fromInt(spinnerTransactionType.getSelectedItemPosition());

            Transaction toAdd = new Transaction(transactionName, transactionType, friendsList.get(0), friendsWhoOwe);

            if (itemIsEditItem()) {
                addTransactionFragmentAnswer.addEditTransaction(toAdd, (String) getArguments().get(EDIT_INDEX));
                dismiss();
            } else {
                addTransactionFragmentAnswer.addTransaction(toAdd);
                dismiss();
            }
        }
    }
}
