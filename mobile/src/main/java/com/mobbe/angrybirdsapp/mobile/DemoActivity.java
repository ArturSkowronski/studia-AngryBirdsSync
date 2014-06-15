/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobbe.angrybirdsapp.mobile;

import com.angry.web.deviceStateEndpoint.DeviceStateEndpoint;
import com.angry.web.deviceStateEndpoint.model.DeviceState;
import com.angry.web.deviceStateEndpoint.model.DeviceStateCollection;
import com.angry.web.deviceStateEndpoint.model.Level;
import com.angry.web.deviceStateEndpoint.model.Result;
import com.angry.web.deviceStateEndpoint.model.World;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Main UI for the demo app.
 */
public class DemoActivity extends RequestActivity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String REGISTRATION_STORE = "REGISTRATION";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */


    TextView mDisplay;

    Context context;
    private DeviceStateEndpoint myApiService;
    SharedPreferences settings;
    String accountName;
    String regid;
    private ApplicationState state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();
        state=(ApplicationState)getApplication();
        endpointRequestWrapper();



    }

    private void endpointRequestWrapper() {
        requestWrapper = new RequestWrapper(this, new EndpointsAsyncTask());
    }
    AlertDialog alertDialog;
    private void createDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pobieranie danych z serwera");
        alertDialogBuilder.setMessage("Proszę czekać").setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    class EndpointsAsyncTask extends AsyncTask<Object, Void, DeviceState> {
        private DeviceStateEndpoint myApiService;
        private Context context;


        private DeviceStateEndpoint generateApiService(GoogleAccountCredential credential) {
            DeviceStateEndpoint.Builder builder = new DeviceStateEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://angrybirds-sync.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            return builder.build();
        }

        @Override
        protected DeviceState doInBackground(Object... params) {
            android.os.Debug.waitForDebugger();
            Pair pair= (Pair) params[0];
            GoogleAccountCredential credential = (GoogleAccountCredential) params[1];

            context = (Context) pair.first;
            String deviceName = android.os.Build.MODEL;
            String deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            DeviceState deviceState=new DeviceState();
            deviceState.setDeviceId(deviceId);
            deviceState.setDeviceName(deviceName);
            deviceState.setDeviceUser(credential.getSelectedAccountName());

            World world1=new World();
            world1.setId(11);
            World world2=new World();
            world2.setId(22);
            World world3=new World();
            world3.setId(33);

            ArrayList<World> worldArrayList=new ArrayList<World>();
            worldArrayList.add(world1);
            worldArrayList.add(world2);
            worldArrayList.add(world3);


            ArrayList<Level> levelArrayList=new ArrayList<Level>();

            Level level1=new Level();
            Result result1=new Result();
            result1.setValue(100);
            level1.setResult(result1);

            Level level2=new Level();
            Result result2=new Result();
            result2.setValue(100);
            level2.setResult(result1);

            levelArrayList.add(level1);
            levelArrayList.add(level2);

            world1.setLevelArrayList(levelArrayList);
            deviceState.setWorldArrayList(worldArrayList);

            try {
                credential.getToken();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            myApiService = generateApiService(credential);
            try {
                return myApiService.insertDeviceState(deviceState).execute();
            } catch (IOException e) {
                return new DeviceState();
            }
        }

        @Override
        protected void onPostExecute(DeviceState result) {
            alertDialog.dismiss();
            Toast.makeText(context, result.getId(), Toast.LENGTH_LONG).show();
        }
    }

    private void getDeviceStateList() {
        createDialog();
        new AsyncTask<Void, Void, DeviceStateCollection>() {

            private DeviceStateEndpoint myApiService;
            private Context context;


            private DeviceStateEndpoint generateApiService(GoogleAccountCredential credential) {
                DeviceStateEndpoint.Builder builder = new DeviceStateEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://angrybirds-sync.appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                return builder.build();
            }

            @Override
            protected DeviceStateCollection doInBackground(Void... params) {
//                android.os.Debug.waitForDebugger();
                DeviceStateEndpoint deviceStateEndpoint= generateApiService(null);
                try {
                    return deviceStateEndpoint.getDeviceStates().execute();
                } catch (IOException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(DeviceStateCollection result) {
                state.setDeviceStates(result);
                alertDialog.dismiss();
                Intent intent = new Intent(DemoActivity.this, ResultList.class);
                startActivity(intent);
            }

        }.execute(null, null, null);
    }



    // Send an upstream message.
    public void onClick(final View view) {
        if (view == findViewById(R.id.send)) {
            Pair<Context, Object> params = new Pair<Context, Object>(DemoActivity.this, "");
            requestWrapper.execute(params);
            createDialog();

        } else if (view == findViewById(R.id.clear)) {
            getDeviceStateList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */



}
