package com.ihells.parcore.managers;

import com.ihells.parcore.gameplayer.GamePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {

    private static final Set<GamePlayer> players = new HashSet<>();
    @Getter
    private static PlayerManager instance;

    public PlayerManager() {
        instance = this;
        init();
    }

    public static void init() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (getInstance().getPlayer(player.getUniqueId()) == null) {
                GamePlayer gamePlayer = new GamePlayer(player.getUniqueId());
                players.add(gamePlayer);

                gamePlayer.setUsername(player.getName());
                gamePlayer.setInMap(false);
            }
        }
    }

    public GamePlayer getPlayer(UUID uuid) {
        List<GamePlayer> playersFound = players.stream().filter(map -> map.getUuid().equals(uuid)).limit(1).collect(Collectors.toList());
        if (playersFound.size() == 0) {
            return null;
        } else {
            return playersFound.get(0);
        }
    }

    public void removePlayer(UUID uuid) {
        players.remove(getPlayer(uuid));
    }

}
