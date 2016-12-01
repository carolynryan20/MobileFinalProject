//package hu.ait.android.mobilefinalproject.fragments;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ToggleButton;
//
//import hu.ait.android.weatherinfo.CityListActivity;
//import hu.ait.android.weatherinfo.R;
//
///**
// * SettingsFragment.java
// *
// * Created by Carolyn Ryan
// * 11/29/2016
// *
// * A class to define the settings fragment with toggle unit button
// */
//public class SettingsFragment extends DialogFragment {
//
//    public static final String TAG = "SettingsFragment";
//
////    private ToggleButton tbSystemSwitch;
//    /**********************************************************************************************/
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        checkActivityImplementsSettingsFragmentAnswer(context);
//    }
//
//    private void checkActivityImplementsSettingsFragmentAnswer(Context context) {
//        if (context instanceof AddCityFragmentAnswer) {
//            settingsFragmentAnswer = (SettingsFragmentAnswer) context;
//        } else {
//            throw new RuntimeException(getString(R.string.not_implement_settings_frag_ans));
//        }
//    }
//
//    /**********************************************************************************************/
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View dialogLayout = inflater.inflate(R.layout.settings_fragment, null);
//
//        setUpAlertDialogBuilder(alertDialogBuilder, dialogLayout);
//
//        return alertDialogBuilder.create();
//    }
//
//    private void setUpAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder, View dialogLayout) {
//        alertDialogBuilder.setView(dialogLayout);
//        alertDialogBuilder.setTitle(R.string.settings);
//        setToggleButton(dialogLayout);
//        setPositiveButton(alertDialogBuilder);
//        setNegativeButton(alertDialogBuilder);
//    }
//
//    private void setToggleButton(View dialogLayout) {
//        String system = getArguments().getString(CityListActivity.KEY_SYSTEM_FRAGMENT);
//        tbSystemSwitch = (ToggleButton) dialogLayout.findViewById(R.id.tbSystemSwitch);
//        if ((system != null) && (system.equals(CityListActivity.IMPERIAL))) {
//            tbSystemSwitch.setChecked(true);
//        } else {
//            tbSystemSwitch.setChecked(false);
//        }
//    }
//
//    private void setPositiveButton(AlertDialog.Builder alertDialogBuilder) {
//        alertDialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                if (tbSystemSwitch.isChecked()) {
//                    settingsFragmentAnswer.setImperial();
//                } else {
//                    settingsFragmentAnswer.setMetric();
//                }
//            }
//        });
//    }
//
//    private void setNegativeButton(AlertDialog.Builder alertDialogBuilder) {
//        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//    }
//}
