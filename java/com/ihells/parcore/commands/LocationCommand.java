package com.ihells.parcore.commands;

import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.entity.Player;

public class LocationCommand {

    public LocationCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "loc", inGameOnly = true)
    public void locCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();
        String message = "&dYour current position is:\n&fX: "+player.getLocation().getX()+"\nY: "+player.getLocation().getY()+"\n"+
                "Z: "+player.getLocation().getZ()+"\nYaw: "+player.getLocation().getYaw()+"\nPitch: "+player.getLocation().getPitch();

        if (player.hasPermission("pcore.admin.location")) {
            player.sendMessage(CC.translate(message));
        } else {
            player.sendMessage(Messages.NO_PERMISSION);
        }
    }

}
