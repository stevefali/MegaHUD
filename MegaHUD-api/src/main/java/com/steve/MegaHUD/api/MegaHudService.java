package com.steve.MegaHUD.api;

import java.util.UUID;

public interface MegaHudService {

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

    /**
     * Create a sidebar or update the title of an existing one.
     *
     * @param playerId The unique UUID of the player.
     * @param title    The string of the sidebar title.
     */
    void setSidebarTitle(UUID playerId, String title);

    /**
     * Remove the sidebar for the given player
     *
     * @param playerId The unique UUID of the player.
     */
    void removeSidebar(UUID playerId);

}
