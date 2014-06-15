package com.mobbe.angrybirdsapp.mobile;

import com.angry.web.deviceStateEndpoint.model.Level;
import com.angry.web.deviceStateEndpoint.model.World;

/**
 * Created by Artur on 2014-06-12.
 */
public class ResultWrapper {
    private World world;
    private Level level;

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
