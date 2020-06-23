package com.ihells.parcore.commands;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.managers.SqlManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VotesCommand {

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();

    public VotesCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "votes", inGameOnly = true)
    public void VotesCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();
        if (player.hasPermission("pcore.admin.votes")) {

            if (args.length == 1) { // votes <player>
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                    HashMap<String, String> votes = new HashMap<>();
                    List<String> toOutput = new ArrayList<>();
                    for (String map : maps) {
                        if (SqlManager.getInstance().getVote(target, map) != null) {
                            votes.put(map, SqlManager.getInstance().getVote(target, map));
                        }
                    }
                    for (int i = 0; i < votes.size(); i++) {
                        String format = messagesConfig.getString("player-votes.format");
                        String mapName = mapsConfig.getString("maps."+votes.keySet().toArray()[i]+".name");
                        String vote = votes.get(votes.keySet().toArray()[i]);
                        format = format.replace("{map_name}", mapName);
                        format = format.replace("{vote}", vote);
                        toOutput.add(CC.translate(format));
                    }
                    for (String line : messagesConfig.getStringList("player-votes.all")) {
                        if ((!line.equals("{format}"))) {
                            player.sendMessage(CC.translate(line.replace("{player}", target.getName())));
                        } else {
                            for (String output : toOutput) {
                                player.sendMessage(output);
                            }
                        }
                    }
                } else {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND);
                }
            } else if (args.length == 2) { // votes <player> <map>
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                    if (maps.contains(args[1].toLowerCase())) {
                        String map = args[1].toLowerCase(); String vote;
                        if (SqlManager.getInstance().getVote(target, map) != null) {
                            vote = SqlManager.getInstance().getVote(target, map);
                        } else { vote = "None"; }
                        String map_name = mapsConfig.getString("maps."+map+".name");
                        String toOutput = messagesConfig.getString("player-votes.format").replace("{map_name}", map_name)
                                .replace("{vote}", vote);
                        for (String line : messagesConfig.getStringList("player-votes.specific")) {
                            if (!(line.equals("{format}"))) {
                                player.sendMessage(CC.translate(line.replace("{player}", target.getName()).replace("{map}", map_name)));
                            } else {
                                player.sendMessage(CC.translate(toOutput));
                            }
                        }
                    } else {
                        player.sendMessage(Messages.MAP_NOT_FOUND);
                    }
                } else {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND);
                }
            } else {
                player.sendMessage(Messages.INVALID_COMMAND);
            }

        } else {
            player.sendMessage(Messages.NO_PERMISSION);
        }
    }

}
