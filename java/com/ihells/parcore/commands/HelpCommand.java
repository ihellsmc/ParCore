package com.ihells.parcore.commands;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.Messages;
import com.ihells.parcore.utils.framework.Command;
import com.ihells.parcore.utils.framework.CommandArgs;
import com.ihells.parcore.utils.framework.CommandFramework;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HelpCommand {

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();

    public HelpCommand(CommandFramework framework) { framework.registerCommands(this); }

    @Command(name = "help", inGameOnly = true)
    public void helpCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = (Player) cmd.getSender();

        if (player.hasPermission("pcore.help")) {
            for (String line : messagesConfig.getStringList("help")) {
                player.sendMessage(CC.translate(line));
            }
        } else {
            player.sendMessage(Messages.NO_PERMISSION);
        }

    }

}
