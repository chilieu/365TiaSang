package me.chilieu.app.tiasang;
import android.app.Dialog;

import android.content.Context;
import android.content.IntentSender;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MyGooglePlay {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 8302;
    private ActionBarActivity activity;
    private FragmentManager fragManager;

    public MyGooglePlay(ActionBarActivity activity) {
        this.activity = activity;
        this.fragManager = activity.getSupportFragmentManager();
    }

    public static boolean isGooglePlay(Context context) {
        return (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
    }

    public boolean isGooglePlay() {
        if (isGooglePlay(activity)) {
            return true;
        } else {
            return checkGooglePlay();
        }
    }

    private static final String DIALOG_ERROR = "dialog_error";

    public ErrorDialogFragment errorFragment;

    private boolean checkGooglePlay() {
        int mIsGooglePlayServicesAvailable = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);

        switch (mIsGooglePlayServicesAvailable) {
            case ConnectionResult.SUCCESS:
                return true;
            default:

                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                        mIsGooglePlayServicesAvailable, activity,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                // If Google Play services can provide an error dialog
                if (errorDialog != null) {
                    // Create a new DialogFragment for the error dialog
                    errorFragment = ErrorDialogFragment.newInstance();
                    // Set the dialog in the DialogFragment
                    errorFragment.setDialog(errorDialog);

                    // Show the error dialog in the DialogFragment
                    errorFragment.show(fragManager, "LocationUpdates");
                }
                // case ConnectionResult.SERVICE_MISSING:
                // case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                // case ConnectionResult.SERVICE_DISABLED:
                // case ConnectionResult.SERVICE_INVALID:
                // case ConnectionResult.DATE_INVALID:
        }

        return false;
    }

    public void dismissMe() {
        DialogFragment frag = (DialogFragment) fragManager
                .findFragmentByTag("LocationUpdates");
        if (frag != null) {
            frag.dismissAllowingStateLoss();
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;




        static ErrorDialogFragment newInstance() {
            ErrorDialogFragment d = new ErrorDialogFragment();
            return d;
        }

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }


        public void onPause(){
            super.onPause();
            this.dismissAllowingStateLoss();
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the user with
             * the error.
             */
            showErrorDialog(connectionResult.getErrorCode(), activity);
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode, ActionBarActivity activity) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment
                .show(activity.getSupportFragmentManager(), "errordialog");
    }

}