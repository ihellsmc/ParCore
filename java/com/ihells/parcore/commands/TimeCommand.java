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
import java.util.List;

public class TimeCommand {

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();

    public TimeCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "time", inGameOnly = true)
    public void timeCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();
        if (player.hasPermission("pcore.admin.time")) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
                    if (maps.contains(args[1].toLowerCase())) {
                        if (SqlManager.getInstance().getTime(target, args[1].toLowerCase()) != 0.0) {
                            String time = Float.toString(SqlManager.getInstance().getTime(target, args[1].toLowerCase()));
                            String map = mapsConfig.getString("maps."+args[1].toLowerCase()+".name");
                            String toOutput = messagesConfig.getString("best-time");
                            toOutput = toOutput.replace("{player}", target.getName()).replace("{map}", map).replace("{time}", time);
                            player.sendMessage(CC.translate(toOutput));
                        } else {
                            player.sendMessage(CC.translate("&d"+target.getName()+" &fhas not played time trials on this map yet!"));
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
