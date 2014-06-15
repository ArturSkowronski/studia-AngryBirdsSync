package com.mobbe.angrybirdsapp.mobile;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Artur on 2014-06-09.
 */
public class RequestActivity extends Activity {
    static final int REQUEST_ACCOUNT_PICKER = 2;
    RequestWrapper requestWrapper;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                requestWrapper.validateRequest(data);

                break;
        }
    }
}
