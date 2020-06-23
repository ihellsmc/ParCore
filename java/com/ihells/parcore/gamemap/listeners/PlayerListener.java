package com.ihells.parcore.gamemap.listeners;

import com.ihells.parcore.gamemap.MapMode;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.TimerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerManager.init();
        if (PlayerManager.getInstance().getPlayer(player.getUniqueId()).isInMap()) {
            if (PlayerManager.getInstance().getPlayer(player.getUniqueId()).getCurrentMapMode().equals(MapMode.TIME_TRIAL)) {
                TimerUtil.startTimer(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        MapManager.getInstance().quit(e.getPlayer().getUniqueId(), PlayerManager.getInstance().getPlayer(e.getPlayer().getUniqueId()).getMap());
    }

}
