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

import static hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment.FRIEND_LIST;

/**
 * Created by Carolyn on 12/4/16.
 */

public class AddTransactionDialogFragment extends DialogFragment {

    public static final String TAG = "AddCityFragment";

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
        this.context = context;
        super.onAttach(context);

        addTransactionFragmentAnswer = (TransactionFragment) getTargetFragment();
        friendsWhoOwe = new HashMap<String, Integer>();
        friendsList = getArguments().getStringArrayList(FRIEND_LIST);
        addTransactionDialogFragment = this;
    }

    private boolean itemIsEditItem() {
        return (boolean) getArguments().get("IS_EDIT");
    }

    private void setFieldsForEditItem() {

        spinnerTransactionType.setSelection((int) getArguments().get(TransactionFragment.TYPE));

        int payerPosition = 0;
        for (int i = 0; i < friendsList.size(); i++) {
            if (friendsList.get(i).equals(getArguments().get(TransactionFragment.WHO_PAID))){
                payerPosition = i;
            }
        }

        etTransactionName.setText((String) getArguments().get(TransactionFragment.TRANSACTION_TITLE));
        //// TODO: 12/8/16 set Friends who owe upon edit
        //lvFriendsToAdd.set(itemToEdit.getEstimatedPriceString());
    }

    private void checkParentImplementsAddTransactionFragmentAnswer() {
        if (getFragmentManager().findFragmentByTag(TransactionFragment.TAG) instanceof AddTransactionFragmentAnswer) {
            addTransactionFragmentAnswer = (TransactionFragment) getFragmentManager().findFragmentByTag(TransactionFragment.TAG);
        } else {
            throw new RuntimeException("Not implementing addTransactionFragmentAnswer");
        }
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
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle("Add a transaction");
        setPositiveButton(alertDialogBuilder, dialogLayout);
        setNegativeButton(alertDialogBuilder);
    }

    private void setPositiveButton(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
        etTransactionName = (EditText) dialogLayout.findViewById(R.id.etTransactionTitle);
        setUpAddingOwes(dialogLayout);


        spinnerTransactionType = (Spinner) dialogLayout.findViewById(R.id.spinnerTransactionType);
        ArrayList<String> typesOfTransaction = new ArrayList<String>() {{
            add("Food");
            add("Drinks");
            add("Rent");
            add("Travel");
            add("Other");
        }};

        ArrayAdapter<String> adapterTransaction = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, typesOfTransaction);
        spinnerTransactionType.setAdapter(adapterTransaction);

        String positiveButton = "Add transaction";

        if (itemIsEditItem()) {
            setFieldsForEditItem();
            positiveButton = "Save";
        }


        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
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

                    AmountOwedDialogFragment amountOwedDialogFragment = new AmountOwedDialogFragment();
                    amountOwedDialogFragment.setTargetFragment(addTransactionDialogFragment, 1);

                    Bundle bundle = new Bundle();
                    bundle.putString("USER", itemValue);
                    amountOwedDialogFragment.setArguments(bundle);

                    amountOwedDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);

                    currentLVClick = (TextView) view;

                }
            });

        } else {
            Toast.makeText(getContext(), "You have no friends.  Loser.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addFriendWhoOwes(String friend, Integer amt) {
//        int amtInt = Math.round(amt);
        friendsWhoOwe.put(friend, amt);
        currentLVClick.setText(friend+" owes "+amt + " Ft");
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
                    handleAddTransactionButtonClick();
                }
            });
        }
    }

    private void handleAddTransactionButtonClick() {
        if (TextUtils.isEmpty(etTransactionName.getText())) {
            etTransactionName.setError("Required");
        } else {
            String transactionName = etTransactionName.getText().toString();
            Transaction.TransactionType transactionType = Transaction.TransactionType.fromInt(spinnerTransactionType.getSelectedItemPosition());

            Transaction toAdd = new Transaction(transactionName, transactionType, friendsList.get(0), friendsWhoOwe);

            if (itemIsEditItem()) {
                addTransactionFragmentAnswer.addEditTransaction(toAdd, (String) getArguments().get("EDIT_INDEX"));
                dismiss();
            } else {
                addTransactionFragmentAnswer.addTransaction(toAdd);
                dismiss();
            }
        }
    }
}
