package com.steve.megaHUD.core.event;

import com.steve.megaHUD.core.hudstate.HudStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    private final HudStateManager hudStateManager;

    public PlayerEvents(HudStateManager hudStateManager) {
        this.hudStateManager = hudStateManager;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hudStateManager.registerPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        hudStateManager.unregisterPlayer(event.getPlayer().getUniqueId());
    }

}
