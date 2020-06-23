package com.ihells.parcore.gamemap;

import com.ihells.parcore.ParCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lobby {

    private final static YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();

    @Getter
    private final static Location spawn = new Location(
            Bukkit.getWorld(mapsConfig.getString("lobby.location.world")),
            mapsConfig.getDouble("lobby.location.x"),
            mapsConfig.getDouble("lobby.location.y"),
            mapsConfig.getDouble("lobby.location.z"),
            mapsConfig.getInt("lobby.location.yaw"),
            mapsConfig.getInt("lobby.locations.pitch")
    );

}
