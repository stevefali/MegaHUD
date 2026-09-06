package com.steve.MegaHUD.api;

import java.util.UUID;

public interface HudService {

    /**
     * Add the given player to the MegaHUD registry.
     *
     * @param playerId The unique UUID of the player.
     */
    void registerPlayer(UUID playerId);

    /**
     * Remove the given player from the MegaHUD registry.
     *
     * @param playerId The unique UUID of the player.
     */
    void unregisterPlayer(UUID playerId);

    /**
     * Remove and cleanup all MegaHUD elements and unregister all players.
     */
    void clearAll();

}
