package com.mobbe.angrybirdsapp.mobile;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Pair;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by Artur on 2014-06-09.
 */
public class RequestWrapper {
    private GoogleAccountCredential credential;
    private SharedPreferences settings;
    private Activity context;
    AsyncTask asyncTask;
    Pair objects;

    private static final String REGISTRATION_STORE = "REGISTRATION";
    static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final String PREF_ACCOUNT_NAME = "USER_ACCOUNT";
    private String accountName;
    private String KEY_DATA="469538464821-hsd1qh2pt58bigmjkh6nkib8r00vkm0f.apps.googleusercontent.com";
    public RequestWrapper(Activity context,AsyncTask asyncTask) {
        this.context = context;
        this.asyncTask = asyncTask;
        settings = context.getSharedPreferences(REGISTRATION_STORE, 0);
        credential = GoogleAccountCredential.usingAudience(context,"server:client_id:"+KEY_DATA);
    }

    public RequestWrapper(Activity context) {
        this.context = context;
        settings = context.getSharedPreferences(REGISTRATION_STORE, 0);
        credential = GoogleAccountCredential.usingAudience(context,"server:client_id:"+KEY_DATA);
    }

    void chooseAccount() {
        context.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private void setSelectedAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        this.accountName = accountName;
    }


    public void passIntent(Intent i){
        this.objects = objects;
        setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        if (credential.getSelectedAccountName() != null) {
        context.startActivity(i);
        } else {
            chooseAccount();
        }
    }


    public void execute(Pair<Context, Object> objects){
        this.objects = objects;
        setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        if (credential.getSelectedAccountName() != null) {
            asyncTask.execute(objects,credential);
        } else {
            chooseAccount();
        }
    }

    public void validateRequest(Intent data){
        if (data != null && data.getExtras() != null) {
            String accountName =
                    data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                setSelectedAccountName(accountName);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, accountName);
                editor.commit();
                execute(objects);
            }
        }
    }

    public void validateItent(Intent data){
        if (data != null && data.getExtras() != null) {
            String accountName =
                    data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                setSelectedAccountName(accountName);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, accountName);
                editor.commit();
                execute(objects);
            }
        }
    }



}
