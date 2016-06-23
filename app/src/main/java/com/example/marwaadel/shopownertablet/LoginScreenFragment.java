package com.example.marwaadel.shopownertablet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marwaadel.shopownertablet.model.OfferDataModel;
import com.example.marwaadel.shopownertablet.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginScreenFragment extends Fragment {
    Firebase mFirebaseRef;
    ProgressDialog mAuthProgressDialog;
    Button LoginBtn;

    EditText mEditTextEmailInput, mEditTextPasswordInput;
    // String mEncodedEmail;
    FirebaseListAdapter<OfferDataModel> mOfferAdapter;
    private Query mQuery;
    SharedPreferences preferences;
Firebase listsRef;
    String mListId;
    public LoginScreenFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_screen, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (preferences.getString("login", "no").equals("yes")) {
//            AuthData authData = listsRef.getAuth();
//            Log.i("uidbgrb: " , authData.getUid());
//
//            mListId = authData.getUid();
//            Log.i("uidbgrb2: " , mListId);


         Intent i = new Intent(getContext(), MainActivity.class);
//           i.putExtra("UID",mListId);



            startActivity(i);
            getActivity().finish();
            // finishActivity(0);
        } else {
            //login value is no, so start loginactivity
            mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

//
//            AuthData authData = mFirebaseRef.getAuth();
//            Log.i("uidbgrb: " , authData.getUid());
//
//            String mListId = authData.getUid();
//            Log.i("uidbgrb2: " , mListId);
//            mFirebaseRef = new Firebase(Constants.FIREBASE_URL).child("OfferList").child(mListId);
//
//

            /**
             * Link layout elements from XML and setup progress dialog
             */
            mEditTextEmailInput = (EditText) rootView.findViewById(R.id.edit_text_email);
            mEditTextPasswordInput = (EditText) rootView.findViewById(R.id.edit_text_password);
            LoginBtn = (Button) rootView.findViewById(R.id.login_with_password);
            mAuthProgressDialog = new ProgressDialog(getContext());
            mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
            mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
            mAuthProgressDialog.setCancelable(false);

            LoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInPassword();
                }
            });
            /**
             * Call signInPassword() when user taps "Done" keyboard action
             */
            mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                    if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        signInPassword();
                    }
                    return true;
                }
            });}
        return rootView;
    } public void signInPassword() {
        // TODO On the client side check that the user has entered something in both the
        // email and password.

        // TODO Show mAuthProgressDialog to let the user know you're logging in.
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        // TODO Use the Firebase method to attempt the actual log in
        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        //hena
        mFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", "yes");
        editor.commit();
    }
    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication call setAuthenticatedUser if it was not already
         * called in
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();
            Log.i("LOG", provider + " " + getString(R.string.log_message_auth_successful));
            authData = mFirebaseRef.getAuth();
            //hena

            if (authData != null &&  authData.getAuth() != null &&isConnected(getActivity().getApplicationContext())) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor spe = sp.edit();
                // Log.i("uid: " , authData.getUid());


                if (authData.getProvider().equals(Constants.PASSWORD_PROVIDER)) {
                    setAuthenticatedUserPasswordProvider(authData);

                }
                /* Save provider name and encodedEmail for later use and start showoffers */
                spe.putString(Constants.KEY_PROVIDER, authData.getProvider()).apply();

                /* Go to showoffers activity */
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//            finish();
            }
//            if (authData != null && isConnected(getActivity().getApplicationContext())) {
//                               /* Go to main activity */
//                Intent intent = new Intent(getActivity(), showoffers.class);
//                //Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//
//            }
            else {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("No Internet connection.");
                alertDialog.setMessage("You have no internet connection");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

        public boolean isConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cm.getActiveNetworkInfo();
            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                    return true;
                else return false;
            } else return false;
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            /**
             * Use utility method to check the network connection state
             * Show "No network connection" if there is no connection
             * Show Firebase specific error message otherwise
             */
            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPasswordInput.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;
                default:
                    showErrorToast(firebaseError.toString());
            }
        }
    }

    private void setAuthenticatedUserPasswordProvider(AuthData authData) {
        final String unprocessedEmail = authData.getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();
        /**
         * Encode user email replacing "." with ","
         * to be able to use it as a Firebase db key
         */
        //  mEncodedEmail = Utils.encodeEmail(unprocessedEmail);
    }

    /**
     * Show error toast to users
     */
    private void showErrorToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}