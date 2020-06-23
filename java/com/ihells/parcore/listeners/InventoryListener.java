package com.ihells.parcore.listeners;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.gamemap.MapMode;
import com.ihells.parcore.guis.MapGUI;
import com.ihells.parcore.guis.ModeGUI;
import com.ihells.parcore.guis.TopTimesGUI;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.SqlManager;
import com.ihells.parcore.utils.CC;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(CC.translate(messagesConfig.getString("rating-gui.title")))) { // rating gui

            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case STAINED_GLASS_PANE: {
                    break;
                } case BARRIER: {
                    player.closeInventory();
                    break;
                } case WOOL: {
                    String map = e.getView().getItem(15).getItemMeta().getLocalizedName();
                    if (e.getSlot() == 11) {
                        sendVoteMessage(player, "very-easy");
                        SqlManager.getInstance().setVote(player, map, messagesConfig.getString("rating-gui.very-easy.name"));
                    } else if (e.getSlot() == 12) {
                        sendVoteMessage(player, "easy");
                        SqlManager.getInstance().setVote(player, map, messagesConfig.getString("rating-gui.easy.name"));
                    } else if (e.getSlot() == 13) {
                        sendVoteMessage(player, "medium");
                        SqlManager.getInstance().setVote(player, map, messagesConfig.getString("rating-gui.medium.name"));
                    } else if (e.getSlot() == 14) {
                        sendVoteMessage(player, "hard");
                        SqlManager.getInstance().setVote(player, map, messagesConfig.getString("rating-gui.hard.name"));
                    } else {
                        sendVoteMessage(player, "insane");
                        SqlManager.getInstance().setVote(player, map, messagesConfig.getString("rating-gui.insane.name"));
                    }
                }
            }
            player.closeInventory();

        } else if (e.getView().getTitle().equals(CC.translate(messagesConfig.getString("mode-selection-gui.title")))) { // mode gui

            e.setCancelled(true);
            String map = e.getView().getItem(15).getItemMeta().getLocalizedName();

            switch (e.getCurrentItem().getType()) {
                case STAINED_GLASS_PANE: {
                    break;
                } case BARRIER: {
                    player.closeInventory();
                    break;
                } case ARROW: {
                    MapGUI.getInstance().applyMapsGUI(player, 1);
                    break;
                } case COMPASS: {
                    if (player.hasPermission("pcore.timetrials")) {
                        MapManager.getInstance().addPlayer(player.getUniqueId(), MapManager.getInstance().getMap(map), MapMode.TIME_TRIAL);
                        player.sendMessage(CC.translate("&aTeleporting..."));
                    } else {
                        player.sendMessage(CC.translate(messagesConfig.getString("mode-selection-gui.time-trials.no-permission")));
                    }
                    break;
                } case CAKE: {
                    if (player.hasPermission("pcore.casual")) {
                        MapManager.getInstance().addPlayer(player.getUniqueId(), MapManager.getInstance().getMap(map), MapMode.CASUAL);
                        player.sendMessage(CC.translate("&aTeleporting..."));
                    } else {
                        player.sendMessage(CC.translate(messagesConfig.getString("mode-selection-gui.casual.no-permission")));
                    }
                    break;
                }
            }

        } else if (e.getView().getTitle().equals(CC.translate(messagesConfig.getString("maps-gui.title")))) { // map selection

            e.setCancelled(true);
            int page = Integer.parseInt(e.getView().getItem(22).getItemMeta().getLocalizedName());
            switch (e.getCurrentItem().getType()) {
                case STAINED_GLASS_PANE: {
                    break;
                } case BARRIER: {
                    player.closeInventory();
                    break;
                } case ARROW: {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(CC.translate("&fNext"))) {
                        MapGUI.getInstance().applyMapsGUI(player, page+1);
                    } else {
                        MapGUI.getInstance().applyMapsGUI(player, page-1);
                    }
                    break;
                } default: {
                    String map = e.getCurrentItem().getItemMeta().getLocalizedName();
                    if (e.getClick().equals(ClickType.LEFT)) {
                        ModeGUI.getInstance().applyModeGUI(player, map);
                    } else if (e.getClick().equals(ClickType.RIGHT)) {
                        if (player.hasPermission("pcore.showtop")) {
                            TopTimesGUI.getInstance().applyTopTimesGUI(player, map);
                        }
                    }
                }
            }

        } else if (e.getView().getTitle().contains(CC.translate(messagesConfig.getString("top-times-gui.title").replace("{map}", "")))) {

            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case STAINED_GLASS_PANE:
                case SKULL_ITEM: {
                    break;
                } case BARRIER: {
                    player.closeInventory();
                    break;
                } case ARROW: {
                    MapGUI.getInstance().applyMapsGUI(player, 1);
                    break;
                }
            }

        } else {

            e.setCancelled(e.getCurrentItem().getItemMeta().hasLocalizedName());

        }

    }

    private void sendVoteMessage(Player player, String vote) {
        String difficulty = messagesConfig.getString("rating-gui." + vote + ".name");
        List<String> messages = new ArrayList<>(messagesConfig.getStringList("rating-gui.on-submission"));
        for (String message : messages) {
            player.sendMessage(CC.translate(message.replace("{difficulty}", difficulty)));
        }
    }

}
