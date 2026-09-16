package com.steve.MegaHUD.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fires when {@code MegaHUD-core} is finished loading and its internal player registry is ready, such as
 * after {@code MegaHUD-core} reloads.
 * Plugins consuming {@link MegaHudService} should listen for this event and re-send HUD data for each player.
 */
public class MegaHudServiceReadyEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
