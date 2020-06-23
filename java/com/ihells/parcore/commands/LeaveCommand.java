package com.ihells.parcore.commands;

import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.entity.Player;

public class LeaveCommand {

    public LeaveCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "leave", inGameOnly = true)
    public void leaveCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());
        if (player.hasPermission("pcore.leave")) {
            if (gamePlayer.isInMap()) {
                MapManager.getInstance().quit(player.getUniqueId(), gamePlayer.getMap());
            } else {
                player.sendMessage(CC.translate("&cYou are not in a map!"));
            }
        } else {
            player.sendMessage(Messages.INVALID_COMMAND);
        }
    }

}
