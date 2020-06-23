package com.ihells.parcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack is;

//    private final Map<ChatColor, Integer> chatColors = new HashMap<>();

    public ItemBuilder(Material mat) {
        is = new ItemStack(mat);

//        chatColors.put(ChatColor.WHITE, 0);
//        chatColors.put(ChatColor.GOLD, 1);
//        chatColors.put(ChatColor.MAGIC, 2);
//        chatColors.put(ChatColor.AQUA, 3);
//        chatColors.put(ChatColor.YELLOW, 4);
//        chatColors.put(ChatColor.GREEN, 5);
//        chatColors.put(ChatColor.LIGHT_PURPLE, 6);
//        chatColors.put(ChatColor.DARK_GRAY, 7);
//        chatColors.put(ChatColor.GRAY, 8);
//        chatColors.put(ChatColor.DARK_AQUA, 9);
//        chatColors.put(ChatColor.DARK_PURPLE, 10);
//        chatColors.put(ChatColor.DARK_BLUE, 11);
//        chatColors.put(ChatColor.BOLD, 12);
//        chatColors.put(ChatColor.DARK_GREEN, 13);
//        chatColors.put(ChatColor.RED, 14);
//        chatColors.put(ChatColor.BLACK, 15);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder amount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name) {
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);

        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder lore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder durability(int durability) {
        is.setDurability((short) durability);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material) {
        is.setType(material);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        ItemMeta toSet = is.getItemMeta();
        toSet.spigot().setUnbreakable(unbreakable);
        is.setItemMeta(toSet);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = is.getItemMeta();

        meta.setLore(new ArrayList<>());
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (Enchantment e : is.getEnchantments().keySet()) {
            is.removeEnchantment(e);
        }

        return this;
    }

    public ItemBuilder localizedName(String name) {
        ItemMeta meta = is.getItemMeta();
        meta.setLocalizedName(name);
        is.setItemMeta(meta);

        return this;
    }

    public ItemStack build() {
        return is;
    }

    public ItemBuilder woolColor(DyeColor color) {
        this.durability(color.getWoolData());

        return this;
    }

    public ItemBuilder glassColor(DyeColor color) {
        this.durability(color.getDyeData());

        return this;
    }
}
