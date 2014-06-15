package com.angry.web.entities;

import com.angry.web.modele.DeviceState;
import com.angry.web.modele.Level;
import com.angry.web.modele.Result;
import com.angry.web.modele.World;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Artur on 2014-06-09.
 */
public class DeviceStateEntity {
    public static boolean create(DeviceState deviceState){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity deviceStateEntity = new Entity("DeviceState");
        deviceStateEntity.setProperty("deviceName", deviceState.getDeviceName());
        deviceStateEntity.setProperty("deviceId", deviceState.getDeviceId());
        deviceStateEntity.setProperty("deviceUser", deviceState.getDeviceUser());
        deviceStateEntity.setProperty("date", new Date());
        ArrayList<EmbeddedEntity> worldEntities= new ArrayList<EmbeddedEntity>();

        for(World world: deviceState.getWorldArrayList()){



            for(Level level: world.getLevelArrayList()) {
                EmbeddedEntity World = new EmbeddedEntity();
                World.setProperty("worldId",world.getId());
                World.setProperty("levelId", level.getId());
                World.setProperty("value", level.getResult().getValue());
                worldEntities.add(World);

            }
        }
        deviceStateEntity.setProperty("worlds", worldEntities);
        datastore.put(deviceStateEntity);
        return true;
    }
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    public static ArrayList<DeviceState> index(){
        ArrayList<DeviceState> deviceStates=new ArrayList<DeviceState>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("DeviceState");
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            DeviceState deviceState = new DeviceState();
            deviceState.setDeviceId((String) result.getProperty("deviceId"));
            deviceState.setDeviceName((String) result.getProperty("deviceName"));
            deviceState.setDeviceUser((String) result.getProperty("deviceUser"));
            deviceState.setLastSync((Date) result.getProperty("date"));
            ArrayList<EmbeddedEntity> embeddedEntities= (ArrayList<EmbeddedEntity>) result.getProperty("worlds");
            ArrayList<World> worlds=new ArrayList<World>();
            for(EmbeddedEntity emb: embeddedEntities){
                World world=new World();
                world.setId(safeLongToInt((Long) emb.getProperty("worldId")));
                ArrayList<Level> levels = new ArrayList<Level>();
                ArrayList<EmbeddedEntity> embeddedEntitiesLevels= (ArrayList<EmbeddedEntity>) emb.getProperty("levels");
                if(embeddedEntitiesLevels!=null){
                for(EmbeddedEntity emb1: embeddedEntitiesLevels){
                    Level level=new Level();
                    level.setId(safeLongToInt((Long) emb1.getProperty("levelId")));
                    Result result1=new Result();
                    result1.setId(2);
                    result1.setValue(safeLongToInt((Long) emb1.getProperty("value")));
                    level.setResult(result1);
                }
                }else{

                    Level level1=new Level();
                    Result result1=new Result();
                    result1.setValue(100);
                    level1.setResult(result1);

                    Level level2=new Level();
                    Result result2=new Result();
                    result2.setValue(100);
                    level2.setResult(result1);

                    levels.add(level1);
                    levels.add(level2);
                }
                world.setLevelArrayList(levels);
                worlds.add(world);
            }
            deviceState.setWorldArrayList(worlds);
            deviceStates.add(deviceState);
        }

        return deviceStates;
    }
}
