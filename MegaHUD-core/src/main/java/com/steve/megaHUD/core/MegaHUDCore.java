package com.steve.megaHUD.core;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.MegaHudServiceReadyEvent;
import com.steve.megaHUD.core.event.PlayerEvents;
import com.steve.megaHUD.core.hudstate.HudStateManager;
import com.steve.megaHUD.core.implementation.HudServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MegaHUDCore extends JavaPlugin {

    private final HudStateManager hudStateManager = new HudStateManager();

    @Override
    public void onEnable() {
        // Plugin startup logic

        HudServiceImpl hudServiceImpl = new HudServiceImpl(hudStateManager);
        Bukkit.getServicesManager().register(MegaHudService.class, hudServiceImpl, this, ServicePriority.Normal);

        getServer().getPluginManager().registerEvents(new PlayerEvents(hudStateManager), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            hudStateManager.registerPlayer(player.getUniqueId());
        }

        Bukkit.getPluginManager().callEvent(new MegaHudServiceReadyEvent());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        hudStateManager.clear();

        HandlerList.unregisterAll(this);

        Bukkit.getServicesManager().unregisterAll(this);
    }
}
