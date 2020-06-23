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

import java.util.ArrayList;

public class VotingGUI {

    @Getter
    private static VotingGUI instance;

    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();
    private final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).glassColor(DyeColor.SILVER).name("&f").build();

    public VotingGUI() {
        instance = this;
    }

    public void applyVotingGUI(Player player, String map) {

        Inventory gui = Bukkit.createInventory(player, 27, CC.translate(messagesConfig.getString("rating-gui.title")));

        ItemBuilder veryEasy = new ItemBuilder(Material.WOOL).woolColor(DyeColor.LIGHT_BLUE);
        ItemBuilder easy = new ItemBuilder(Material.WOOL).woolColor(DyeColor.LIME);
        ItemBuilder medium = new ItemBuilder(Material.WOOL).woolColor(DyeColor.YELLOW);
        ItemBuilder hard = new ItemBuilder(Material.WOOL).woolColor(DyeColor.RED);
        ItemBuilder insane = new ItemBuilder(Material.WOOL).woolColor(DyeColor.PURPLE);

        nameFunction(map, "very-easy", veryEasy);
        nameFunction(map, "easy", easy);
        nameFunction(map, "medium", medium);
        nameFunction(map, "hard", hard);
        nameFunction(map, "insane", insane);

        gui.setItem(11, veryEasy.build());
        gui.setItem(12, easy.build());
        gui.setItem(13, medium.build());
        gui.setItem(14, hard.build());
        gui.setItem(15, insane.build());
        for (int i = 0; i < 27; i++) {
            if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                gui.setItem(i, glass);
            }
        }

        player.openInventory(gui);

    }

    protected void nameFunction(String map, String mode, ItemBuilder builder) {
        builder.name(messagesConfig.getString("rating-gui." + mode + ".name"));
        builder.lore(new ArrayList<>(messagesConfig.getStringList("rating-gui." + mode + ".lore")));
        builder.localizedName(map);
    }

}
