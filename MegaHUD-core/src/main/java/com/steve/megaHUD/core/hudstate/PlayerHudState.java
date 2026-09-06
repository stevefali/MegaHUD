package com.steve.megaHUD.core.hudstate;


import com.steve.megaHUD.core.hudstate.sidebar.SidebarLine;
import org.bukkit.boss.BossBar;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHudState {

    private final UUID playerId;

    private static final String SIDEBAR_OBJECTIVE_NAME = "sidebar_objective";

    private boolean sidebarObjectiveSent = false;

    private final Map<Integer, SidebarLine> sidebarLines = new ConcurrentHashMap<>();



    protected PlayerHudState(UUID playerId) {
        this.playerId = playerId;
    }


    public void cleanup() {

//        bossBar.removeAll();
    }

    public void setSidebarObjectiveSent(boolean isSent) {
        this.sidebarObjectiveSent = isSent;
    }

    public boolean isSidebarObjectiveSent() {
        return sidebarObjectiveSent;
    }


}
