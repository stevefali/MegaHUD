package com.steve.megaHUD.core.implementation;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.megaHUD.core.MegaHUDCore;
import com.steve.megaHUD.core.hudstate.HudStateManager;
import com.steve.megaHUD.core.hudstate.PlayerHudState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.FixedFormat;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HudServiceImpl implements MegaHudService {

    private static final Plugin plugin = JavaPlugin.getPlugin((MegaHUDCore.class));
    private final HudStateManager hudStateManager;

    public static final String SIDEBAR_OBJECTIVE_NAME = "sidebar_objective";

    public HudServiceImpl(HudStateManager hudStateManager) {
        this.hudStateManager = hudStateManager;
    }


    @Override
    public void removeAllSidebarLines(UUID playerId) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer != null) {
            PlayerHudState playerHudState = hudStateManager.getPlayerHudState(playerId);
            if (playerHudState != null) {
                for (Integer lineIndex : playerHudState.getSidebarLineIndices()) {
                    serverPlayer.connection.send(new ClientboundResetScorePacket(
                            getIndexString(lineIndex),
                            SIDEBAR_OBJECTIVE_NAME
                    ));
                }
                playerHudState.clearSidebarLineIndices();
            }
        }
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

    @Override
    public void setSidebarLine(UUID playerId, int lineIndex, SidebarLine sidebarLine) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer != null) {
            serverPlayer.connection.send(convertSidebarLine(lineIndex, sidebarLine));
            hudStateManager.getOrCreateHudState(playerId).setSidebarLineIndex(lineIndex);
        }
    }

    @Override
    public void removeSidebarLine(UUID playerId, int lineIndex) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer != null) {
            serverPlayer.connection.send(new ClientboundResetScorePacket(
                    getIndexString(lineIndex),
                    SIDEBAR_OBJECTIVE_NAME
            ));
        }
        PlayerHudState playerHudState = hudStateManager.getPlayerHudState(playerId);
        if (playerHudState != null) {
            playerHudState.removeSidebarLineIndex(lineIndex);
        }
    }

    @Override
    public void setAllSidebarLines(UUID playerId, Map<Integer, SidebarLine> sidebarLineMap) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer != null) {
            PlayerHudState playerHudState = hudStateManager.getOrCreateHudState(playerId);
            for (Integer lineIndex : sidebarLineMap.keySet()) {
                serverPlayer.connection.send(convertSidebarLine(lineIndex, sidebarLineMap.get(lineIndex)));
            }
            // Remove stray lines
            for (Integer stateLineIndex : playerHudState.getSidebarLineIndices()) {
                if (!sidebarLineMap.containsKey(stateLineIndex)) {
                    serverPlayer.connection.send(new ClientboundResetScorePacket(
                            getIndexString(stateLineIndex),
                            SIDEBAR_OBJECTIVE_NAME
                    ));
                }
            }
            playerHudState.clearSidebarLineIndices();
            playerHudState.setAllSidebarLineIndices(sidebarLineMap.keySet());
        }
    }

    public static void clearSidebar(UUID playerId, PlayerHudState playerHudState) {
        ServerPlayer serverPlayer = getServerPlayer(playerId);
        if (serverPlayer == null) {
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

    private static ClientboundSetScorePacket convertSidebarLine(int index, SidebarLine sidebarLine) {
        SidebarLine.RightSideDisplay rightSideDisplay = sidebarLine.getRightSideDisplay();

        if (rightSideDisplay.equals(SidebarLine.RightSideDisplay.BLANK)) {
            return new ClientboundSetScorePacket(
                    getIndexString(index),
                    SIDEBAR_OBJECTIVE_NAME,
                    getLineScore(index),
                    Optional.of(Component.literal(sidebarLine.getLeftText())),
                    Optional.of(BlankFormat.INSTANCE)
            );

        } else {
            return new ClientboundSetScorePacket(
                    getIndexString(index),
                    SIDEBAR_OBJECTIVE_NAME,
                    getLineScore(index),
                    Optional.of(Component.literal(sidebarLine.getLeftText())),
                    Optional.of(new FixedFormat(Component.literal(sidebarLine.getRightText())))
            );
        }
    }

    private static String getIndexString(int index) {
        return "line_" + index;
    }

    private static int getLineScore(int index) {
        return 1000 - index;
    }
}
