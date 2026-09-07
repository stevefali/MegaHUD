package com.steve.megaHUD.core.hudstate;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HudStateManager {

    private static final Map<UUID, PlayerHudState> hudStates = new ConcurrentHashMap<>();

    public PlayerHudState getPlayerHudState(UUID playerId) {
        return hudStates.get(playerId);
    }

    public PlayerHudState getOrCreateHudState(UUID playerId) {
        if (hudStates.get(playerId) == null) {
            registerPlayer(playerId);
        }
        return hudStates.get(playerId);
    }

    public void registerPlayer(UUID playerId) {

        hudStates.put(playerId, new PlayerHudState(playerId));
    }

    public void unregisterPlayer(UUID playerId) {
        PlayerHudState playerHudState = hudStates.remove(playerId);
        if (playerHudState != null) {
            playerHudState.cleanup();
        }
    }

    public void clear() {
        for (PlayerHudState playerHudState : hudStates.values()) {
            if (playerHudState != null) {
                playerHudState.cleanup();
            }
        }
        hudStates.clear();
    }

}
