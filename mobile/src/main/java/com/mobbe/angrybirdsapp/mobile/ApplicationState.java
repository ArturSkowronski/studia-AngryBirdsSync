package com.mobbe.angrybirdsapp.mobile;

import android.app.Application;

import com.angry.web.deviceStateEndpoint.model.DeviceStateCollection;

/**
 * Created by Artur on 2014-06-12.
 */
public class ApplicationState extends Application {

    private DeviceStateCollection deviceStates;

    public void setDeviceStates(DeviceStateCollection deviceStates) {
        this.deviceStates = deviceStates;
    }

    public DeviceStateCollection getDeviceStates() {
        return deviceStates;
    }
}
