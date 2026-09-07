package com.steve.megaHUD.core.hudstate;


import com.steve.megaHUD.core.hudstate.sidebar.SidebarLine;
import com.steve.megaHUD.core.implementation.HudServiceImpl;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHudState {

    private final UUID playerId;

    private boolean sidebarObjectiveSent = false;
    private Component sidebarTitle;

    private final Map<Integer, SidebarLine> sidebarLines = new ConcurrentHashMap<>();


    protected PlayerHudState(UUID playerId) {
        this.playerId = playerId;
    }


    public void cleanup() {
        HudServiceImpl.clearSidebar(playerId, this);
    }

    public void setSidebarObjectiveSent(boolean isSent) {
        this.sidebarObjectiveSent = isSent;
    }

    public boolean isSidebarObjectiveSent() {
        return sidebarObjectiveSent;
    }

    public void setSidebarTitle(Component sidebarTitle) {
        this.sidebarTitle = sidebarTitle;
    }

    public Component getSidebarTitle() {
        return this.sidebarTitle;
    }

}
