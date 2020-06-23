package com.ihells.parcore.utils;

import com.ihells.parcore.ParCore;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {

    private static final YamlConfiguration messages = ParCore.getInstance().getMessagesConfig().getConfiguration();

    public static final String NO_PERMISSION = CC.translate(messages.getString("no-permission"));
    public static final String INVALID_COMMAND = CC.translate(messages.getString("invalid-command"));
    public static final String MAP_NOT_FOUND = CC.translate(messages.getString("map-not-found"));
    public static final String PLAYER_NOT_FOUND = CC.translate(messages.getString("player-not-found"));
    public static final String ON_MANIA = CC.translate(messages.getString("on-mania"));

}
