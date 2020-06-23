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

import java.util.ArrayList;
import java.util.List;

import static com.ihells.parcore.utils.PageUtil.getPageItems;
import static com.ihells.parcore.utils.PageUtil.isPageValid;

public class MapGUI {

    @Getter
    private static MapGUI instance;

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).glassColor(DyeColor.SILVER).name("&f").build();

    public MapGUI() {
        instance = this;
    }

    public void applyMapsGUI(Player player, int page) {

        // create inventory
        Inventory gui = Bukkit.createInventory(player, 27, CC.translate(messagesConfig.getString("maps-gui.title")));

        // list of all maps the player should be able to see
        List<ItemStack> allMaps = new ArrayList<>();

        // add all maps to the list
        List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));
        if (!(maps.isEmpty())) {
            for (String map : maps) {
                if (player.hasPermission("pcore.maps." + map)) {
                    allMaps.add(getMapItem(map, player));
                }
            }
        } else {
            player.sendMessage(CC.translate("&cThere are no maps available!"));
        }

        // left & right page buttons
        ItemStack left, right;
        if (isPageValid(allMaps, page - 1, 7)) {
            left = new ItemBuilder(Material.ARROW).name("&fBack").build();
        } else {
            left = glass;
        }
        if (isPageValid(allMaps, page + 1, 7)) {
            right = new ItemBuilder(Material.ARROW).name("&fNext").build();
        } else {
            right = glass;
        }

        ItemStack cancel = new ItemBuilder(Material.BARRIER).name(messagesConfig.getString("maps-gui.cancel")).localizedName(page + "").build();

        gui.setItem(18, left);
        gui.setItem(26, right);
        gui.setItem(22, cancel);

        for (int i = 0; i < 10; i++) {
            gui.setItem(i, glass);
        }
        for (int i = 19; i < 26; i++) {
            if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                gui.setItem(i, glass);
            }
        }
        gui.setItem(17, glass);

        for (ItemStack item : getPageItems(allMaps, page, 7)) {
            gui.setItem(gui.firstEmpty(), item);
        }

        player.openInventory(gui);

        allMaps.clear();
        maps.clear();

    }

    private ItemStack getMapItem(String map, Player player) {

        String path = "maps." + map + ".";
        ItemBuilder item;

        if (Material.getMaterial(mapsConfig.getString(path + "icon").toUpperCase()) != null) {
            item = new ItemBuilder(Material.getMaterial(mapsConfig.getString(path + "icon").toUpperCase()));
        } else {
            item = new ItemBuilder(Material.GRASS);
        }

        String top;
        float time = SqlManager.getInstance().getTime(player, map);
        if (time == 0.0f) {
            top = "0";
        } else {
            top = String.format("%.1f", time);
        }

        List<String> toReplace = new ArrayList<>(mapsConfig.getStringList(path + "description"));
        List<String> lore = new ArrayList<>();
        for (String s : toReplace) {
            s = s.replace("{time}", top);
            s = s.replace("{played}", Integer.toString(SqlManager.getInstance().getPlayed(player, map)));
            if (SqlManager.getInstance().getAverageVotes(map) == null) {
                s = s.replace("{difficulty}", "Not rated yet");
            } else {
                s = s.replace("{difficulty}", CC.translate(SqlManager.getInstance().getAverageVotes(map)));
            }
            lore.add(s);
        }

        item.lore(lore);
        item.name(mapsConfig.getString(path + "name"));
        item.localizedName(map);

        return item.build();

    }

}
