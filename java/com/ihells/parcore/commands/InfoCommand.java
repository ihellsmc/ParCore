package com.ihells.parcore.commands;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.managers.SqlManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand {

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();

    public InfoCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "info", inGameOnly = true)
    public void infoCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();

        if (player.hasPermission("pcore.maps.info")) {
            if (args.length == 1) {
                String map = args[0];
                List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                if (maps.contains(map.toLowerCase())) {

                    String name = mapsConfig.getString("maps."+map.toLowerCase()+".name");
                    String best = SqlManager.getInstance().getTopTimes(map.toLowerCase()).get(0).getItemMeta().getLocalizedName();
                    String difficulty; String time; String played;
                    if (SqlManager.getInstance().getAverageVotes(map.toLowerCase()) != null) {
                        difficulty = SqlManager.getInstance().getAverageVotes(map);
                    } else { difficulty = "Not rated yet"; }
                    if (SqlManager.getInstance().getTime(player, map.toLowerCase()) != 0.0) {
                        time = String.format("%.1f", SqlManager.getInstance().getTime(player, map.toLowerCase()));
                    } else { time = "None"; }
                    if (SqlManager.getInstance().getPlayed(player, map.toLowerCase()) != 0) {
                        played = SqlManager.getInstance().getPlayed(player, map.toLowerCase())+"";
                    } else { played = "None"; }

                    for (String line : messagesConfig.getStringList("map-info")) {
                        line = line.replace("{name}", name);
                        line = line.replace("{difficulty}", difficulty);
                        line = line.replace("{played}", played);
                        line = line.replace("{top}", best);
                        line = line.replace("{pb}", time);
                        player.sendMessage(CC.translate(line));
                    }

                } else {
                    player.sendMessage(Messages.MAP_NOT_FOUND);
                }
            } else {
                player.sendMessage(Messages.INVALID_COMMAND);
            }
        } else {
            player.sendMessage(Messages.NO_PERMISSION);
        }

    }

}
