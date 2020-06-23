package com.ihells.parcore.managers;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.gamemap.GameMap;
import com.ihells.parcore.gamemap.Lobby;
import com.ihells.parcore.gamemap.MapMode;
import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.guis.PlayerInventory;
import com.ihells.parcore.guis.VotingGUI;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.TimerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.ihells.parcore.utils.PlayerUtil.sendMessage;


public class MapManager {

    @Getter
    private static MapManager instance;

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final Set<GameMap> maps = new HashSet<>();

    public MapManager() {
        instance = this;
        init();
    }

    protected void init() {
        Set<String> gameMaps = mapsConfig.getConfigurationSection("maps").getKeys(false);

        for (int i = 0; i < gameMaps.size(); i++) {
            for (String name : gameMaps) {
                maps.add(new GameMap(name));
            }
        }

        for (GameMap gameMap : maps) {
            gameMap.setSpawn(
                    new Location(
                            Bukkit.getWorld(mapsConfig.getString("maps." + gameMap.getName() + ".location.world")),
                            mapsConfig.getDouble("maps." + gameMap.getName() + ".location.x"),
                            mapsConfig.getDouble("maps." + gameMap.getName() + ".location.y"),
                            mapsConfig.getDouble("maps." + gameMap.getName() + ".location.z"),
                            mapsConfig.getInt("maps." + gameMap.getName() + ".location.yaw"),
                            mapsConfig.getInt("maps." + gameMap.getName() + ".location.pitch")
                    )
            );

            gameMap.setCheckpointBlocks(getCheckpointBlocks(gameMap.getName()));
            gameMap.setEndBlocks(getEndBlocks(gameMap.getName()));
            gameMap.setIllegalBlocks(getIllegalBlocks(gameMap.getName()));

        }

    }

    public GameMap getMap(String name) {
        List<GameMap> mapsFound = maps.stream().filter(map -> map.getName().equals(name)).limit(1).collect(Collectors.toList());
        if (mapsFound.size() == 0) {
            return null;
        } else {
            return mapsFound.get(0);
        }
    }

    public void addPlayer(UUID uuid, GameMap map, MapMode mode) {

        // add the player to the player list
        List<UUID> players = map.getPlayers();
        players.add(uuid);
        map.setPlayers(players);

        Player player = Bukkit.getPlayer(uuid);
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(uuid);

        // teleport the player to the arena spawn
        player.teleport(map.getSpawn());

        // apply the map inventory
        PlayerInventory.getInstance().applyInventory(player);

        // start timer if in time trial (from 0)
        if (mode.equals(MapMode.TIME_TRIAL)) {
            TimerUtil.startTimer(uuid);
        }

        float personalBest = SqlManager.getInstance().getTime(player, map.getName());
        int timesPlayed = SqlManager.getInstance().getPlayed(player, map.getName());

        // set GamePlayer data
        gamePlayer.setInMap(true);
        gamePlayer.setMap(map);
        gamePlayer.setPersonalBest(personalBest);
        gamePlayer.setTimesPlayed(timesPlayed);
        gamePlayer.setCurrentMapMode(mode);
        gamePlayer.setCheckpoint(map.getSpawn());

    }

    public void removePlayer(UUID uuid, GameMap map, MapMode mode) {

        // remove the player from the player list
        List<UUID> players = map.getPlayers();
        players.remove(uuid);
        map.setPlayers(players);

        Player player = Bukkit.getPlayer(uuid);
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(uuid);

        // teleport the player back to lobby
        player.teleport(Lobby.getSpawn());

        // show all players if they are hidden
        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            player.showPlayer(ParCore.getInstance(), target);
        }

        // remove map inventory and give back items if they had any
        Bukkit.getScheduler().runTaskLater(ParCore.getInstance(), () -> player.getInventory().clear(), 1L);

        // add the mania if this is their first time playing the map and show voting gui
        if (SqlManager.getInstance().getPlayed(player, map.getName()) == 0) {
            int mania = mapsConfig.getInt("maps." + map.getName() + ".mania-on-completion");
            SqlManager.getInstance().addMania(player, mania);
            sendMessage(player, Messages.ON_MANIA.replace("{mania}", mania + ""));

            VotingGUI.getInstance().applyVotingGUI(player, map.getName());
        } else {
            sendMessage(player, messagesConfig.getString("on-finish"));
        }

        // if they're in time trials, update their time
        if (mode.equals(MapMode.TIME_TRIAL)) {
            float time = gamePlayer.getCurrentTime();
            TimerUtil.stopTimer(uuid);

            // if they set a new record, send a congratulation message
            float oldTime = SqlManager.getInstance().getTime(player, map.getName());
            if (oldTime > time) {
                sendMessage(player, messagesConfig.getString("on-record").replace("{difference}", String.format("%.1f", oldTime-time)));
            }

            SqlManager.getInstance().setTime(player, time, map.getName());
        }

        // update the amount of times they have played by one
        SqlManager.getInstance().updatePlayed(player, map.getName());

        // set GamePlayer data
        gamePlayer.setInMap(false);
        gamePlayer.setMap(null);
        gamePlayer.setPersonalBest(0.0f);
        gamePlayer.setTimesPlayed(0);
        gamePlayer.setCurrentMapMode(null);
        gamePlayer.setCheckpoint(null);

    }

    public void quit(UUID uuid, GameMap map) {

        // remove the player from the player list
        List<UUID> players = map.getPlayers();
        players.remove(uuid);
        map.setPlayers(players);

        Player player = Bukkit.getPlayer(uuid);
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(uuid);

        // teleport the player back to lobby
        player.teleport(Lobby.getSpawn());

        // show all players if they are hidden
        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            player.showPlayer(ParCore.getInstance(), target);
        }

        // if they are in time trials, stop the timer
        if (gamePlayer.getCurrentMapMode().equals(MapMode.TIME_TRIAL)) {
            TimerUtil.stopTimer(player.getUniqueId());
        }

        // remove map inventory and give back items if they had any
        Bukkit.getScheduler().runTaskLater(ParCore.getInstance(), () -> player.getInventory().clear(), 1L);

        // set GamePlayer data
        gamePlayer.setInMap(false);
        gamePlayer.setMap(null);
        gamePlayer.setPersonalBest(0.0f);
        gamePlayer.setTimesPlayed(0);
        gamePlayer.setCurrentMapMode(null);
        gamePlayer.setCheckpoint(null);

    }

    public List<Material> getCheckpointBlocks(String name) {
        List<String> materials = new ArrayList<>(mapsConfig.getStringList("maps." + name + ".blocks.set-checkpoint"));
        List<Material> valid = new ArrayList<>();
        for (String material : materials) {
            if (Material.getMaterial(material.toUpperCase()) != null) {
                valid.add(Material.getMaterial(material.toUpperCase()));
            }
        }
        if (valid.isEmpty()) {
            return null;
        } else {
            return valid;
        }
    }

    public List<Material> getEndBlocks(String name) {
        List<String> materials = new ArrayList<>(mapsConfig.getStringList("maps." + name + ".blocks.complete"));
        List<Material> valid = new ArrayList<>();
        for (String material : materials) {
            if (Material.getMaterial(material.toUpperCase()) != null) {
                valid.add(Material.getMaterial(material.toUpperCase()));
            }
        }
        if (valid.isEmpty()) {
            return null;
        } else {
            return valid;
        }
    }

    public List<Material> getIllegalBlocks(String name) {
        List<String> materials = new ArrayList<>(mapsConfig.getStringList("maps." + name + ".blocks.illegal"));
        List<Material> valid = new ArrayList<>();
        for (String material : materials) {
            if (Material.getMaterial(material.toUpperCase()) != null) {
                valid.add(Material.getMaterial(material.toUpperCase()));
            }
        }
        if (valid.isEmpty()) {
            return null;
        } else {
            return valid;
        }
    }

}
