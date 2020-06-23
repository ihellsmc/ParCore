package com.ihells.parcore.listeners;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().hasLocalizedName()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(e.getPlayer().getUniqueId());
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem().getItemMeta().hasLocalizedName()) {
                switch (e.getItem().getItemMeta().getLocalizedName()) {
                    case "invCompass": {
                        if (e.getPlayer().hasPermission("pcore.checkpoint")) {
                            e.getPlayer().teleport(gamePlayer.getCheckpoint());
                        }
                        break;
                    } case "invPlayers": {
                        if (e.getPlayer().hasPermission("pcore.toggleplayers")) {
                            if (gamePlayer.isPlayersHidden()) { // if players are hidden
                                ItemStack shown = new ItemBuilder(Material.INK_SACK).localizedName("invPlayers").glassColor(DyeColor.LIME).name("&a&lPLAYERS SHOWN").build();
                                gamePlayer.setPlayersHidden(false);
                                e.getPlayer().setItemInHand(shown);

                                for (Player target : Bukkit.getServer().getOnlinePlayers()) { e.getPlayer().showPlayer(ParCore.getInstance(), target); }
                                e.getPlayer().sendMessage(CC.translate("&a&lYou can now see players!"));

                            } else { // if players are shown
                                ItemStack hidden = new ItemBuilder(Material.INK_SACK).localizedName("invPlayers").glassColor(DyeColor.GRAY).name("&7&lPLAYERS HIDDEN").build();
                                gamePlayer.setPlayersHidden(true);
                                e.getPlayer().setItemInHand(hidden);

                                for (Player target : Bukkit.getServer().getOnlinePlayers()) { e.getPlayer().hidePlayer(ParCore.getInstance(), target); }
                                e.getPlayer().sendMessage(CC.translate("&c&lYou can no longer see players!"));

                            }
                        }
                        break;
                    } case "invBarrier": {
                        ItemStack confirm = new ItemBuilder(Material.STAINED_GLASS_PANE).glassColor(DyeColor.ORANGE).name("&c&lAre you sure?").localizedName("invConfirm").build();
                        e.getPlayer().setItemInHand(confirm);
                        break;
                    } case "invConfirm": {
                        MapManager.getInstance().quit(e.getPlayer().getUniqueId(), gamePlayer.getMap());
                        break;
                    } default: { return; }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.setCancelled(PlayerManager.getInstance().getPlayer(e.getPlayer().getUniqueId()).isInMap());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        e.setCancelled(PlayerManager.getInstance().getPlayer(e.getPlayer().getUniqueId()).isInMap());
    }

}
