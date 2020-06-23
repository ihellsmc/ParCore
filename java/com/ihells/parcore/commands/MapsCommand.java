package com.ihells.parcore.commands;

import com.ihells.parcore.guis.MapGUI;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.entity.Player;

public class MapsCommand {

    public MapsCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "maps", inGameOnly = false)
    public void mapsCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();

        if (args.length == 0) {
            if (player.hasPermission("pcore.maps")) {
                if (!PlayerManager.getInstance().getPlayer(player.getUniqueId()).isInMap()) {
                    MapGUI.getInstance().applyMapsGUI(player, 1);
                } else {
                    player.sendMessage(CC.translate("&cYou are already in a map!"));
                }
            } else {
                player.sendMessage(Messages.NO_PERMISSION);
            }
        } else {
            player.sendMessage(Messages.INVALID_COMMAND);
        }

    }

}
