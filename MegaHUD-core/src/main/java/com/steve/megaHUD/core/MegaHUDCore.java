package com.steve.megaHUD.core;

import com.steve.megaHUD.core.event.BlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MegaHUDCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getServer().getPluginManager().registerEvents(new BlockEvent(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
