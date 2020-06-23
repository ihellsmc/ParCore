package com.ihells.parcore.guis;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.managers.SqlManager;
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

import java.util.List;

public class TopTimesGUI {

    @Getter
    private static TopTimesGUI instance;

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).glassColor(DyeColor.SILVER).name("&f").build();;

    public TopTimesGUI() {
        instance = this;
    }

    public void applyTopTimesGUI(Player player, String map) {

        String mapName = mapsConfig.getString("maps." + map + ".name");
        Inventory gui = Bukkit.createInventory(player, 54, CC.translate(messagesConfig.getString("top-times-gui.title").replace("{map}", mapName)));

        List<ItemStack> topTimes = SqlManager.getInstance().getTopTimes(map);

        ItemStack cancel = new ItemBuilder(Material.BARRIER).name(messagesConfig.getString("top-times-gui.cancel")).build();
        ItemStack back = new ItemBuilder(Material.ARROW).name("&fBack").build();

        gui.setItem(13, topTimes.get(0));
        gui.setItem(21, topTimes.get(1));
        gui.setItem(23, topTimes.get(2));
        gui.setItem(29, topTimes.get(3));
        gui.setItem(31, topTimes.get(4));
        gui.setItem(33, topTimes.get(5));
        gui.setItem(49, cancel);
        gui.setItem(45, back);
        for (int i = 0; i < 54; i++) {
            if (i != 22 && i != 30 && i != 32) {
                if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                    gui.setItem(i, glass);
                }
            }
        }

        player.openInventory(gui);

        topTimes.clear();

    }

}
