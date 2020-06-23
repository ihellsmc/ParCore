package com.ihells.parcore.utils;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.UUID;


public class TimerUtil {

    public static void startTimer(UUID uuid) {
        GamePlayer player = PlayerManager.getInstance().getPlayer(uuid);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                player.setCurrentTime(player.getCurrentTime() + 0.1f);

                String time = String.format("%.1f", player.getCurrentTime());

                Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(CC.translate("&d&lTIME: &f" + time)));
            }
        };

        player.setTimer(
                Bukkit.getScheduler().runTaskTimerAsynchronously(
                        ParCore.getInstance(),
                        runnable,
                        0L, 2L
                )
        );

    }

    public static void pauseTimer(UUID uuid) {
        GamePlayer player = PlayerManager.getInstance().getPlayer(uuid);
        player.getTimer().cancel();
    }

    public static void stopTimer(UUID uuid) {
        GamePlayer player = PlayerManager.getInstance().getPlayer(uuid);
        player.getTimer().cancel();
        player.setCurrentTime(0.0f);
    }

}
