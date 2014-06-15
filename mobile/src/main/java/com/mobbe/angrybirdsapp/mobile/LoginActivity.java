package com.mobbe.angrybirdsapp.mobile;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.

    private SignInButton mPlusSignInButton;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String REGISTRATION_STORE = "REGISTRATION";
    static final int REQUEST_ACCOUNT_PICKER = 2;
    SharedPreferences settings;
    String regid;
    RequestWrapper requestWrapper;
    String SENDER_ID = "469538464821";
    GoogleCloudMessaging gcm;
    Context context;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestWrapper = new RequestWrapper(this);
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        context = getApplicationContext();

        if (supportsGooglePlayServices()) {
            mPlusSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPlayServices()) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                        regid = getRegistrationId(context);
                        if (regid.isEmpty()) {
                            registerInBackground();
                        }
                        else{
                            registerInBackground();
                            settings = getSharedPreferences(REGISTRATION_STORE, 0);
                        }
                }

            }
            });
        } else {
            Intent intent = new Intent(LoginActivity.this, DemoActivity.class);
            startActivity(intent);
            mPlusSignInButton.setVisibility(View.GONE);


        }

    }
    AlertDialog.Builder alertDialogBuilder;
    private void createDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pobieranie danych z serwera");
        alertDialogBuilder.setMessage("Proszę czekać").setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String msg = "";
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                regid = "APA91bGi0fH7QdftVDzTI9PB5Og6gHhvN1c-i_-c2q-zrM8URLsq8l4RqWS0JKiFiUoGTn8qe0QbnDqwKEk1__JL56N3ue12uchqdA9f8s5gASmCX63Fzo6Csr49JrsOki6VC02tKZMtByZcqoGKGvEARAetDCfGFBrZgvz6kbvIQqT1eeBW5dc";
                sendRegistrationIdToBackend(regid);
                storeRegistrationId(context, regid);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (checkPlayServices()) {
                    gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    regid = getRegistrationId(context);

                    if (regid.isEmpty()) {
                        registerInBackground();
                    }
                    else{
                        requestWrapper.validateItent(data);
                        settings = getSharedPreferences(REGISTRATION_STORE, 0);
                    }
                } else {
                    Log.i(TAG, "No valid Google Play Services APK found.");
                }
            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.data=data;
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (checkPlayServices()) {
                    gcm = GoogleCloudMessaging.getInstance(this);
                    regid = getRegistrationId(context);

                    if (regid.isEmpty()) {
                        registerInBackground();
                    }
                    else{

                        requestWrapper.validateItent(data);
                        settings = getSharedPreferences(REGISTRATION_STORE, 0);
                    }
                } else {
                    Log.i(TAG, "No valid Google Play Services APK found.");
                }
                break;
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(DemoActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private void sendRegistrationIdToBackend(String msg) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(LoginActivity.this, "Twoje registration id "+ msg, Toast.LENGTH_LONG).show();
                requestWrapper.passIntent(new Intent(LoginActivity.this, DemoActivity.class));
            }
        }.execute(msg, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }


}



