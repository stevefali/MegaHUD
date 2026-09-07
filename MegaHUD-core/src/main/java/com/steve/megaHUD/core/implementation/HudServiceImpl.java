package com.steve.megaHUD.core.implementation;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.megaHUD.core.MegaHUDCore;
import com.steve.megaHUD.core.hudstate.HudStateManager;
import com.steve.megaHUD.core.hudstate.PlayerHudState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class HudServiceImpl implements MegaHudService {

    private static final Plugin plugin = JavaPlugin.getPlugin((MegaHUDCore.class));
    private final HudStateManager hudStateManager;

    public static final String SIDEBAR_OBJECTIVE_NAME = "sidebar_objective";

    public HudServiceImpl(HudStateManager hudStateManager) {
        this.hudStateManager = hudStateManager;
    }

    @Override
    public void registerPlayer(UUID playerId) {
        hudStateManager.registerPlayer(playerId);
    }

    @Override
    public void unregisterPlayer(UUID playerId) {
        hudStateManager.unregisterPlayer(playerId);
    }

    @Override
    public void clearAll() {
        hudStateManager.clear();
    }

    @Override
    public void setSidebarTitle(UUID playerId, String title) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer == null) {
            plugin.getLogger().warning("Error: Can't update sidebar: player is null");
            return;
        }

        PlayerHudState playerHudState = hudStateManager.getOrCreateHudState(playerId);

        Objective objective = new Objective(
                new Scoreboard(),
                SIDEBAR_OBJECTIVE_NAME,
                ObjectiveCriteria.DUMMY,
                Component.literal(title),
                ObjectiveCriteria.RenderType.INTEGER,
                true,
                BlankFormat.INSTANCE
        );

        int method = playerHudState.isSidebarObjectiveSent() ? 2 : 0;
        serverPlayer.connection.send(new ClientboundSetObjectivePacket(objective, method));

        if (!playerHudState.isSidebarObjectiveSent()) {
            serverPlayer.connection.send(new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, objective));
            playerHudState.setSidebarObjectiveSent(true);
        }
    }

    @Override
    public void removeSidebar(UUID playerId) {
        clearSidebar(playerId, hudStateManager.getPlayerHudState(playerId));
    }

    public static void clearSidebar(UUID playerId, PlayerHudState playerHudState) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer == null) {
//            plugin.getLogger().warning("Error removing sidebar: player is null");
            return;
        }

        Objective objective = new Objective(
                new Scoreboard(),
                SIDEBAR_OBJECTIVE_NAME,
                ObjectiveCriteria.DUMMY,
                Component.empty(),
                ObjectiveCriteria.RenderType.INTEGER,
                true,
                BlankFormat.INSTANCE
        );

        serverPlayer.connection.send(new ClientboundSetObjectivePacket(objective, 1));
        if (playerHudState != null) {
            playerHudState.setSidebarObjectiveSent(false);
        }
    }


    private static ServerPlayer getServerPlayer(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            return ((CraftPlayer) player).getHandle();
        }
        return null;
    }
}
