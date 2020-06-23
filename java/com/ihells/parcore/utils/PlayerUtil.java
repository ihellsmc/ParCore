package com.ihells.parcore.utils;

import org.bukkit.command.CommandSender;


public class PlayerUtil {

    public static void sendMessage(CommandSender sender, String... messages) {
        for (String message : messages) {
            sender.sendMessage(CC.translate(message));
        }
    }

}
