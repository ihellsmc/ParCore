package com.ihells.parcore.guis;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.utils.CC;
import com.ihells.parcore.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ModeGUI {

    @Getter
    private static ModeGUI instance;

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).glassColor(DyeColor.SILVER).name("&f").build();

    public ModeGUI() {
        instance = this;
    }

    public void applyModeGUI(Player player, String map) {

        Inventory gui = Bukkit.createInventory(player, 27, CC.translate(messagesConfig.getString("mode-selection-gui.title")));

        ItemBuilder timeTrials = new ItemBuilder(Material.COMPASS);
        ItemBuilder casual = new ItemBuilder(Material.CAKE);

        timeTrials.name(messagesConfig.getString("mode-selection-gui.time-trials.name"));
        casual.name(messagesConfig.getString("mode-selection-gui.casual.name"));
        casual.localizedName(map);

        timeTrials.lore(messagesConfig.getStringList("mode-selection-gui.time-trials.lore"));
        casual.lore(messagesConfig.getStringList("mode-selection-gui.casual.lore"));

        ItemStack cancel = new ItemBuilder(Material.BARRIER).name(messagesConfig.getString("mode-selection-gui.cancel")).build();
        ItemStack back = new ItemBuilder(Material.ARROW).name("&fBack").build();

        gui.setItem(11, timeTrials.build());
        gui.setItem(15, casual.build());
        gui.setItem(22, cancel);
        gui.setItem(18, back);
        for (int i = 0; i < 27; i++) {
            if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                gui.setItem(i, glass);
            }
        }

        player.openInventory(gui);

    }

}
