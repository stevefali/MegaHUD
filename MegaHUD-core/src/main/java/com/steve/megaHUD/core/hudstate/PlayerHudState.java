package com.steve.megaHUD.core.hudstate;


import com.steve.megaHUD.core.implementation.HudServiceImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerHudState {

    private final UUID playerId;

    private boolean sidebarObjectiveSent = false;

    private Set<Integer> sidebarLineIndices = new HashSet<>();


    protected PlayerHudState(UUID playerId) {
        this.playerId = playerId;
    }


    public void cleanup() {
        HudServiceImpl.clearSidebar(playerId, this);
        sidebarLineIndices.clear();
    }

    public void setSidebarObjectiveSent(boolean isSent) {
        this.sidebarObjectiveSent = isSent;
    }

    public boolean isSidebarObjectiveSent() {
        return sidebarObjectiveSent;
    }

    public Set<Integer> getSidebarLineIndices() {
        return sidebarLineIndices;
    }

    public void setAllSidebarLineIndices(Set<Integer> allLines) {
        this.sidebarLineIndices = allLines;
    }

    public void setSidebarLineIndex(int index) {
        this.sidebarLineIndices.add(index);
    }

    public void removeSidebarLineIndex(int index) {
        sidebarLineIndices.remove(index);
    }

    public void clearSidebarLineIndices() {
        sidebarLineIndices.clear();
    }

}
