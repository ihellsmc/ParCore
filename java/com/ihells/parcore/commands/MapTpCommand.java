package com.ihells.parcore.commands;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MapTpCommand {

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();

    public MapTpCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "maptp", inGameOnly = true)
    public void mapTpCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();

        if (player.hasPermission("pcore.admin.tp")) {
            if (args.length == 1) {
                String map = args[0].toLowerCase();
                List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                if (maps.contains(map)) {
                    player.teleport(MapManager.getInstance().getMap(map).getSpawn());
                    player.sendMessage(CC.translate("&aTeleporting..."));
                } else {
                    player.sendMessage(Messages.MAP_NOT_FOUND);
                }
            } else if (args.length == 2) {
                if (player.hasPermission("pcore.admin.tpothers")) {
                    String map = args[0].toLowerCase();
                    List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                    if (maps.contains(map)) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            target.teleport(MapManager.getInstance().getMap(map).getSpawn());
                            target.sendMessage(CC.translate("&aTeleporting..."));
                        } else {
                            player.sendMessage(Messages.PLAYER_NOT_FOUND);
                        }
                    } else {
                        player.sendMessage(Messages.MAP_NOT_FOUND);
                    }
                } else {
                    player.sendMessage(Messages.NO_PERMISSION);
                }
            } else {
                player.sendMessage(Messages.INVALID_COMMAND);
            }
        } else {
            player.sendMessage(Messages.NO_PERMISSION);
        }

    }

}
