package com.steve.MegaHUD.api;

import java.util.Map;
import java.util.UUID;

public interface MegaHudService {

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

    /**
     * Add or update a {@link SidebarLine} at the given line index.
     *
     * @param playerId    The unique UUID of the player.
     * @param lineIndex   The (0-based) position of the {@link SidebarLine}.
     * @param sidebarLine The contents of the new or updated line.
     */
    void setSidebarLine(UUID playerId, int lineIndex, SidebarLine sidebarLine);


    /**
     * Remove a {@link SidebarLine} at the given line index.
     *
     * @param playerId  The unique UUID of the player.
     * @param lineIndex The (0-based) position of the {@link SidebarLine}.
     */
    void removeSidebarLine(UUID playerId, int lineIndex);


    /**
     * Set all the lines in the sidebar. Any existing lines that are not present in the new {@link Map} will be removed.
     *
     * @param playerId The unique UUID of the player.
     * @param allLines A {@link Map} of {@link SidebarLine}s where each key represents the index position of the line.
     */
    void setAllSidebarLines(UUID playerId, Map<Integer, SidebarLine> allLines);


}
