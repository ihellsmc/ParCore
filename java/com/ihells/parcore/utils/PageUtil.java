package com.ihells.parcore.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    public static List<ItemStack> getPageItems(List<ItemStack> items, int page, int spaces) {
        int upperBound = page * spaces; // the upper limit for the page display
        int lowerBound = upperBound - spaces; // the lower limit for the page display

        List<ItemStack> newItems = new ArrayList<>(); // all the new items within the bounds
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                newItems.add(items.get(i)); // set the item that is between the bounds
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        return newItems;
    }

    public static boolean isPageValid(List<ItemStack> items, int page, int spaces) {
        if (page <= 0) {
            return false;
        }
        int upperBound = page * spaces; // the upper limit for the page display
        int lowerBound = upperBound - spaces; // the lower limit for the page display

        return items.size() > lowerBound;
    }

}
