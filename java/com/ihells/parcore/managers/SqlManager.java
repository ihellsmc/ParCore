package com.ihells.parcore.managers;

import com.ihells.parcore.ParCore;
import com.ihells.parcore.utils.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SqlManager {

    @Getter
    private static SqlManager instance;

    private final YamlConfiguration mapsConfig = ParCore.getInstance().getMapsConfig().getConfiguration();
    private final YamlConfiguration messagesConfig = ParCore.getInstance().getMessagesConfig().getConfiguration();

    private final List<String> maps = new ArrayList<>(mapsConfig.getConfigurationSection("maps").getKeys(false));

    public SqlManager() {
        instance = this;
        init();
    }

    protected void init() {
        try (Statement statement = ParCore.getInstance().connection.createStatement()) {
            for (String map : maps) {
                statement.execute("CREATE TABLE IF NOT EXISTS `" + map + "-votes` (`uuid` VARCHAR(36), `vote` VARCHAR(32))");
                statement.execute("CREATE TABLE IF NOT EXISTS `" + map + "-times` (`uuid` VARCHAR(36), `time` FLOAT)");
                statement.execute("CREATE TABLE IF NOT EXISTS `" + map + "-played` (`uuid` VARCHAR(36), `amount` INTEGER)");
            }
            statement.execute("CREATE TABLE IF NOT EXISTS `mania` (`uuid` VARCHAR(36), `mania` INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPlayed(Player player, String map) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT amount FROM `" + map + "-played` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean playerPlayed(UUID uuid, String map) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `" + map + "-played` WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updatePlayed(Player player, String map) {
        try {
            if (playerPlayed(player.getUniqueId(), map)) {
                try (PreparedStatement update = ParCore.getInstance().connection.prepareStatement("UPDATE `" + map + "-played` SET amount = ? WHERE uuid = ? ;")) {
                    update.setInt(1, getPlayed(player, map) + 1);
                    update.setString(2, player.getUniqueId().toString());
                    update.executeUpdate();
                }
            } else {
                try (PreparedStatement insert = ParCore.getInstance().connection.prepareStatement("INSERT INTO `" + map + "-played` (uuid, amount) VALUES (?, ?);")) {
                    insert.setString(1, player.getUniqueId().toString());
                    insert.setInt(2, 1);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setVote(Player player, String map, String vote) {
        if (hasVoted(player, map)) {
            try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("UPDATE `" + map + "-votes` SET vote = ? WHERE uuid = ? ;")) {
                statement.setString(1, vote);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("INSERT INTO `" + map + "-votes` (uuid, vote) VALUES (?, ?);")) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, vote);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean hasVoted(Player player, String map) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `" + map + "-votes` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getVote(Player player, String map) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `" + map + "-votes` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("vote");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMania(Player player, int mania) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `mania` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                try (PreparedStatement update = ParCore.getInstance().connection.prepareStatement("UPDATE `mania` SET mania = ? WHERE uuid = ? ;")) {
                    update.setInt(1, getMania(player) + mania);
                    update.setString(2, player.getUniqueId().toString());
                    update.executeUpdate();
                }
            } else {
                try (PreparedStatement insert = ParCore.getInstance().connection.prepareStatement("INSERT INTO `mania` (uuid, mania) VALUES (?, ?);")) {
                    insert.setString(1, player.getUniqueId().toString());
                    insert.setInt(2, getMania(player) + mania);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setMania(Player player, int mania) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `mania` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                try (PreparedStatement update = ParCore.getInstance().connection.prepareStatement("UPDATE `mania` SET mania = ? WHERE uuid = ? ;")) {
                    update.setInt(1, mania);
                    update.setString(2, player.getUniqueId().toString());
                    update.executeUpdate();
                }
            } else {
                try (PreparedStatement insert = ParCore.getInstance().connection.prepareStatement("INSERT INTO `mania` (uuid, mania) VALUES (?, ?);")) {
                    insert.setString(1, player.getUniqueId().toString());
                    insert.setInt(2, mania);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMania(Player player) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `mania` WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("mania");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ItemStack> getTopTimes(String map) {
        List<ItemStack> times = new ArrayList<>();
        int count = 1; // needs to get to 6
        try (Statement statement = ParCore.getInstance().connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM `" + map + "-times` ORDER BY time ASC;");
            System.out.println("TRIED");
            while (rs.next()) {
                System.out.println(count);
                float time = rs.getFloat("time");
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                if (count < 7) {
                    times.add(getHead(player.getUniqueId(), count, time));
                    count++;
                }
            }
            while (times.size() < 6) {
                times.add(getVacantHead(times.size() + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

    public String getAverageVotes(String map) {
        HashMap<String, Integer> voteAmounts = new HashMap<>();
        try (Statement statement = ParCore.getInstance().connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM `" + map + "-votes`;");
            while (rs.next()) {
                if (voteAmounts.containsKey(rs.getString("vote"))) {
                    voteAmounts.replace(rs.getString("vote"), voteAmounts.get(rs.getString("vote")) + 1);
                } else {
                    voteAmounts.put(rs.getString("vote"), 1);
                }
            }
            if (voteAmounts.isEmpty()) { return null; } else { return sort(voteAmounts).get(0).getKey(); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public float getTime(Player player, String map) {
        try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("SELECT * FROM `" + map + "-times` WHERE uuid = ?;")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getFloat("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (float) 0.0;
    }

    public void setTime(Player player, float time, String map) {
        if (getTime(player, map) == 0.0) {
            try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("INSERT INTO `" + map + "-times` (uuid, time) VALUES (?, ?);")) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setFloat(2, time);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (time < getTime(player, map)) {
                try (PreparedStatement statement = ParCore.getInstance().connection.prepareStatement("UPDATE `" + map + "-times` SET time = ? WHERE uuid = ?;")) {
                    statement.setFloat(1, time);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ItemStack getHead(UUID uuid, int place, float time) {
        List<String> lore;
        String title;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();

        if (place < 4) {
            title = messagesConfig.getString("top-times-gui.format.top-3.name");
            lore = new ArrayList<>(messagesConfig.getStringList("top-times-gui.format.top-3.lore"));
        } else {
            title = messagesConfig.getString("top-times-gui.format.other.name");
            lore = new ArrayList<>(messagesConfig.getStringList("top-times-gui.format.other.lore"));
        }

        skull.setDisplayName(CC.translate(title.replace("{position}", place + "").replace("{name}", Bukkit.getOfflinePlayer(uuid).getName())));

        skull.setLocalizedName(Float.toString(time));
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(CC.translate(line.replace("{time}", String.format("%.1f", time))));
        }
        skull.setLore(newLore);
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        lore.clear();
        newLore.clear();
        item.setItemMeta(skull);
        return item;
    }

    public ItemStack getVacantHead(int place) {
        List<String> lore;
        String title;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = item.getItemMeta();
        if (place < 4) {
            title = messagesConfig.getString("top-times-gui.format.top-3.name");
            lore = new ArrayList<>(messagesConfig.getStringList("top-times-gui.format.top-3.lore"));
        } else {
            title = messagesConfig.getString("top-times-gui.format.other.name");
            lore = new ArrayList<>(messagesConfig.getStringList("top-times-gui.format.other.lore"));
        }
        meta.setDisplayName(CC.translate(title.replace("{position}", Integer.toString(place)).replace("{name}", "???")));
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(CC.translate(line.replace("{time}", "???")));
        }
        meta.setLore(newLore);
        meta.setLocalizedName("None");
        lore.clear();
        newLore.clear();
        item.setItemMeta(meta);
        return item;
    }

    public <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sort(Map<K, V> map) {
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }

}
