package com.angry.web.endpoints;

import com.angry.web.entities.DeviceStateEntity;
import com.angry.web.modele.DeviceState;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(name = "deviceStateEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "web.angry.com", ownerName = "web.angry.com", packagePath=""))
public class DeviceStateEndpoint {

    // Make sure to add this endpoint to your web.xml file if this is a web application.

    private static final Logger LOG = Logger.getLogger(DeviceStateEndpoint.class.getName());

    /**
     * This method gets the <code>DeviceState</code> object associated with the specified <code>id</code>.
     * @param id The id of the object to be returned.
     * @return The <code>DeviceState</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getDeviceStates")
    public ArrayList<DeviceState> getDeviceStates(User user) {
        return DeviceStateEntity.index();
    }

    /**
     * This inserts a new <code>DeviceState</code> object.
     * @param deviceState The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertDeviceState")
    public DeviceState insertDeviceState(DeviceState deviceState, User user) {
        LOG.info("Calling insertDeviceState method");
        DeviceStateEntity.create(deviceState);
        return deviceState;
    }
}