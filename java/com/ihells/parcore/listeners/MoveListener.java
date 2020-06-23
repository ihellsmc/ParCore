package com.ihells.parcore.listeners;

import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.CC;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onLand(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());
        Block standingOn = player.getLocation().getBlock().getRelative(0, -1, 0);
        Location blockCenter = standingOn.getLocation().add(0.5, 1, 0.5);

        if (gamePlayer.isInMap()) {

            //  ILLEGAL
            if (gamePlayer.getMap().getIllegalBlocks().contains(standingOn.getType())) {
                player.teleport(gamePlayer.getCheckpoint());
            }

            // END
            if (gamePlayer.getMap().getEndBlocks().contains(standingOn.getType())) {
                MapManager.getInstance().removePlayer(player.getUniqueId(), gamePlayer.getMap(), gamePlayer.getCurrentMapMode());
            }

        }

    }

    @EventHandler
    public void onCheckpoint(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        Player player = e.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());
        Block standingOn = player.getLocation().getBlock().getRelative(0, -1, 0);
        Location blockCenter = standingOn.getLocation().add(0.5, 1, 0.5);
        blockCenter.setYaw(player.getLocation().getYaw()); blockCenter.setPitch(player.getLocation().getPitch());

        if (gamePlayer.isInMap()) {

            // CHECKPOINT
            if (gamePlayer.getMap().getCheckpointBlocks().contains(standingOn.getType())) {
                if (!(gamePlayer.getCheckpoint().getBlock().equals(player.getLocation().getBlock()))) {
                    gamePlayer.setCheckpoint(blockCenter);
                    player.sendMessage(CC.translate("&a&lCheckpoint set!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 0);
                }
            }

        }
    }

}
