package com.ihells.parcore.gamemap;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GameMap {

    private final String name;
    private List<UUID> players = new ArrayList<>();
    private Location spawn;
    private List<Material> checkpointBlocks;
    private List<Material> endBlocks;
    private List<Material> illegalBlocks;

}
