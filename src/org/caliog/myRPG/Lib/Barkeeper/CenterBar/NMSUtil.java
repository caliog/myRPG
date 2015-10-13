package org.caliog.myRPG.Lib.Barkeeper.CenterBar;

import org.bukkit.entity.Player;

public abstract class NMSUtil {

    public abstract void sendBar(Player player, String title, String subtitle, int fadein, int active, int fadeout);
}
