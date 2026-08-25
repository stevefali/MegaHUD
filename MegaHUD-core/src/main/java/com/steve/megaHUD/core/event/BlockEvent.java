package com.steve.megaHUD.core.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;

import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.numbers.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Optional;


public class BlockEvent implements Listener {

    private static Scoreboard scoreboard = new Scoreboard();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Material blockType = event.getBlock().getType();
        Player player = event.getPlayer();

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();


        if (blockType == Material.BLUE_WOOL) {

            FixedFormat fixedFormat = new FixedFormat(Component.literal("§dFixedFormat Text"));

            Objective objective = new Objective(
                    scoreboard,
                    "test",
                    ObjectiveCriteria.DUMMY,
                    Component.literal("§3§lMega Randomizer"),
                    ObjectiveCriteria.RenderType.INTEGER,
                    true,
//                    BlankFormat.INSTANCE
                    fixedFormat
            );

            ClientboundSetObjectivePacket setObjectivePacket = new ClientboundSetObjectivePacket(objective, 0);


            ClientboundSetDisplayObjectivePacket setDisplayObjectivePacket = new ClientboundSetDisplayObjectivePacket(
                    DisplaySlot.SIDEBAR, objective);

            serverPlayer.connection.send(setObjectivePacket);
            serverPlayer.connection.send(setDisplayObjectivePacket);
        }


        if (blockType == Material.YELLOW_WOOL) {
            ClientboundSetScorePacket setScorePacket = new ClientboundSetScorePacket(
                    "line_0",
                    "test",
                    3,
                    Optional.of(Component.literal("§aScore Display")),
//                    Optional.of(new FixedFormat(Component.literal("§eScoreFixedFormatText")))
                    Optional.of(new FixedFormat(Component.literal("§6Steve")))
            );

            Style testStyle = Style.EMPTY.withBold(true);
            ClientboundSetScorePacket styledScorePacket = new ClientboundSetScorePacket(
                    "line_1",
                    "test",
                    17875,
                    Optional.of(Component.literal("§aScore Display")),
                    Optional.of(new StyledFormat(testStyle))
            );

            ClientboundSetScorePacket blankLinePacket = new ClientboundSetScorePacket(
                    "line_2",
                    "test",
                    0,
                    Optional.of(Component.empty()),
                    Optional.of(BlankFormat.INSTANCE)
            );

            ClientboundSetScorePacket multilineScorepacket = new ClientboundSetScorePacket(
                    "line_3",
                    "test",
                    0,
                    Optional.of(Component.literal("§7ThirdLine: §d" + 41)),
                    Optional.of(BlankFormat.INSTANCE)
            );

            serverPlayer.connection.send(setScorePacket);
            serverPlayer.connection.send(styledScorePacket);
            serverPlayer.connection.send(blankLinePacket);
            serverPlayer.connection.send(multilineScorepacket);
        }

        if (blockType == Material.RED_WOOL) {
            ClientboundResetScorePacket resetScorePacket = new ClientboundResetScorePacket("line_0", "test");

            serverPlayer.connection.send(resetScorePacket);
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_1", "test"));
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_2", "test"));
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_3", "test"));
        }


    }

}
