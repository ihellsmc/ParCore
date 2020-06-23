package com.ihells.parcore.guis;

import com.ihells.parcore.gameplayer.GamePlayer;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventory {

    @Getter
    private static PlayerInventory instance;

    public PlayerInventory() {
        instance = this;
    }

    public void applyInventory(Player player) {

        player.getInventory().clear();
        GamePlayer gamePlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());

        ItemBuilder builder = new ItemBuilder(Material.INK_SACK).localizedName("invPlayers");
        if (gamePlayer.isPlayersHidden()) {
            builder.glassColor(DyeColor.GRAY).name("&7&lPLAYERS HIDDEN");
        } else {
            builder.glassColor(DyeColor.LIME).name("&a&lPLAYERS SHOWN");
        }
        ItemStack players = builder.build();
        ItemStack compass = new ItemBuilder(Material.COMPASS).name("&d&lCHECKPOINT").localizedName("invCompass").build();
        ItemStack barrier = new ItemBuilder(Material.BARRIER).name("&c&lQUIT").localizedName("invBarrier").build();

        player.getInventory().setItem(2, players);
        player.getInventory().setItem(4, compass);
        player.getInventory().setItem(6, barrier);

    }

}
