package com.steve.megaHUD.core.event;

import net.minecraft.network.chat.Component;
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
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;


public class BlockEvent implements Listener {


    private Plugin plugin;

    private static Objective objective = new Objective(
            new Scoreboard(),
            "test",
            ObjectiveCriteria.DUMMY,
            Component.literal("§3§lMega Randomizer"),
            ObjectiveCriteria.RenderType.INTEGER,
            true,
            BlankFormat.INSTANCE
//                    fixedFormat
    );

    public BlockEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Material blockType = event.getBlock().getType();
        Player player = event.getPlayer();

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();


        if (blockType == Material.BLUE_WOOL) {

            FixedFormat fixedFormat = new FixedFormat(Component.literal("§dFixedFormat Text"));


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

        if (blockType == Material.ORANGE_WOOL) {
            objective.setDisplayName(Component.literal("§dChanged Objective"));
            serverPlayer.connection.send(new ClientboundSetObjectivePacket(objective, 2));
        }

        if (blockType == Material.RED_WOOL) {
            ClientboundResetScorePacket resetScorePacket = new ClientboundResetScorePacket("line_0", "test");

            serverPlayer.connection.send(resetScorePacket);
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_1", "test"));
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_2", "test"));
            serverPlayer.connection.send(new ClientboundResetScorePacket("line_3", "test"));

            serverPlayer.connection.send(new ClientboundSetObjectivePacket(objective, 1));
        }


        // TODO: **Note: Create the BosBar like this! (With no NamespacedKey) **
        if (blockType == Material.PURPLE_WOOL) {
            BossBar bossBar = Bukkit.createBossBar("§bNon-persistent", BarColor.RED, BarStyle.SOLID);
            bossBar.addPlayer(player);
        }


        // TODO **NOTE: Do NOT create BossBar like this (using NamespacedKey) because it will be persisted by the server. **
        if (blockType == Material.GREEN_WOOL) {
            BossBar bossBar = Bukkit.createBossBar(
                    NamespacedKey.fromString("boss_key", plugin),
                    "Reach the next Goal",
                    BarColor.GREEN,
                    BarStyle.SEGMENTED_12
            );
            bossBar.setProgress((double) 1 / 12);
            bossBar.addPlayer(player);


            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        bossBar.removeAll();
                    }, 20L * 60
            );

        }

        if (blockType == Material.LIME_WOOL) {
            NamespacedKey key = NamespacedKey.fromString("boss_key", plugin);
            BossBar bossBar = Bukkit.getBossBar(key);

            if (bossBar != null) {
                if (!bossBar.getPlayers().contains(player)) {
                    bossBar.addPlayer(player);
                    plugin.getLogger().info("does not conatin player");
                    bossBar.setProgress((double) 1 / 12);
                }
                double progress = bossBar.getProgress();
                if (progress < 1) {
                    double nextProgress = progress + ((double) 1 / 12);
                    if (nextProgress >= 1) {
                        bossBar.removeAll();
                        return;
                    }
                    bossBar.setProgress(nextProgress);
                    if (nextProgress >= (double) 10 / 12) {
                        bossBar.setTitle("§7Nearly there!");
                        bossBar.setStyle(BarStyle.SOLID);
                    }
                } else {
                    bossBar.removeAll();
                }
            } else {
                plugin.getLogger().info("Boss bar is null");
            }

        }

        if (blockType == Material.WHITE_WOOL) {
            NamespacedKey key = NamespacedKey.fromString("boss_key", plugin);
            BossBar bossBar = Bukkit.getBossBar(key);
            if (bossBar != null) {

                Bukkit.removeBossBar(key);
            } else {
                plugin.getLogger().info("The bar is null");
            }
        }



    }

}
