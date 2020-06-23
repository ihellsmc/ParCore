package com.ihells.parcore.gameplayer;

import com.ihells.parcore.gamemap.GameMap;
import com.ihells.parcore.gamemap.MapMode;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

@Data
public class GamePlayer {

    private final UUID uuid;
    private String username;
    private boolean inMap = false;
    private GameMap map = null;
    private float currentTime = 0.0f;
    private float personalBest = 0.0f;
    private int timesPlayed = 0;
    private MapMode currentMapMode = null;
    private boolean playersHidden = false;
    private BukkitTask timer = null;
    private Location checkpoint = null;

}
