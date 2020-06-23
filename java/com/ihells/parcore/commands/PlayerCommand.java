package com.ihells.parcore.commands;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.managers.SqlManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerCommand {

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();

    public PlayerCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "player", inGameOnly = true)
    public void playerCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();

        if (player.hasPermission("pcore.admin.players")) {
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(target.getUniqueId());

                    String playerName = target.getName();
                    String mapName; String mapMode; String currentTime; String bestTime; String timesPlayed; String hasHidden;
                    if (gamePlayer.isInMap()) {
                        mapName = mapsConfig.getString("maps."+gamePlayer.getMap().getName()+".name");
                        mapMode = gamePlayer.getCurrentMapMode().toString();
                        currentTime = String.format("%.1f", gamePlayer.getCurrentTime());
                        if (currentTime.equals("0.0")) { currentTime = "None"; }
                        if (SqlManager.getInstance().getTime(target, gamePlayer.getMap().getName()) != 0.0) {
                            bestTime = String.format("%.1f", SqlManager.getInstance().getTime(target, gamePlayer.getMap().getName()));
                        } else { bestTime = "None"; }
                        if (SqlManager.getInstance().getPlayed(target, gamePlayer.getMap().getName()) != 0) {
                            timesPlayed = SqlManager.getInstance().getPlayed(target, gamePlayer.getMap().getName())+"";
                        } else { timesPlayed = "None"; }
                        if (gamePlayer.isPlayersHidden()) { hasHidden = "&aYes"; } else { hasHidden = "&cNo"; }
                    } else {
                        mapName = "None"; mapMode = "None"; currentTime = "None"; bestTime = "None"; timesPlayed = "None"; hasHidden = "&cNo";
                    }

                    for (String line : messagesConfig.getStringList("player-info")) {
                        line = line.replace("{player}", playerName);
                        line = line.replace("{map}", mapName);
                        line = line.replace("{mode}", mapMode);
                        line = line.replace("{time}", currentTime);
                        line = line.replace("{pb}", bestTime);
                        line = line.replace("{played}", timesPlayed);
                        line = line.replace("{hidden}", hasHidden);
                        player.sendMessage(CC.translate(line));
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
