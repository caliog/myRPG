package org.caliog.npclib;

import org.bukkit.entity.Entity;

public abstract class NMSUtil {

    public abstract void setYaw(Entity entity, float yaw);

    public abstract void pathStep(Moveable a);

    public abstract NPCManager getNPCManager();

    public abstract void nodeUpdate(Node node);
}
