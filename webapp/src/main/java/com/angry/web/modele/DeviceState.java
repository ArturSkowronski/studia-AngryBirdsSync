package com.angry.web.modele;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by artsko-polaris on 07.06.2014.
 */
public class DeviceState {
    private String deviceUser;

    public DeviceState() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public ArrayList<World> getWorldArrayList() {
        return worldArrayList;
    }

    public void setWorldArrayList(ArrayList<World> worldArrayList) {
        this.worldArrayList = worldArrayList;
    }

    public int id;
    public String deviceId;
    public String deviceName;

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public Date lastSync;
    public ArrayList<World> worldArrayList = new ArrayList<World>();

    public String getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser) {
        this.deviceUser = deviceUser;
    }



}
