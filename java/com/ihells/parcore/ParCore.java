package com.ihells.parcore;

import com.ihells.parcore.commands.*;
import com.ihells.parcore.gamemap.listeners.PlayerListener;
import com.ihells.parcore.guis.*;
import com.ihells.parcore.listeners.InteractListener;
import com.ihells.parcore.listeners.InventoryListener;
import com.ihells.parcore.listeners.MoveListener;
import com.ihells.parcore.managers.MapManager;
import com.ihells.parcore.managers.PlayerManager;
import com.ihells.parcore.managers.SqlManager;
import com.ihells.parcore.utils.framework.CommandFramework;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ParCore extends JavaPlugin {

    @Getter
    private static ParCore instance;
    @Getter @Setter
    public BasicConfigurationFile mapsConfig;
    @Getter @Setter
    public BasicConfigurationFile messagesConfig;
    @Getter @Setter
    public BasicConfigurationFile sqlConfig;
    public Connection connection;
    public String host, name, username, password;
    public int port;
    private CommandFramework framework;

    @Override
    public void onEnable() {
        instance = this;
        framework = new CommandFramework(this);

        registerConfigs();
        registerDetails();

        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        registerGUIs();
        registerManagers();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    private void registerConfigs() {
        mapsConfig = new BasicConfigurationFile(this, "maps");
        messagesConfig = new BasicConfigurationFile(this, "messages");
        sqlConfig = new BasicConfigurationFile(this, "details");
    }

    private void registerCommands() {
        new HelpCommand(framework);
        new InfoCommand(framework);
        new LeaveCommand(framework);
        new LocationCommand(framework);
        new MapsCommand(framework);
        new MapTpCommand(framework);
        new PlayerCommand(framework);
        new TimeCommand(framework);
        new VotesCommand(framework);
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new InteractListener(), this);
        manager.registerEvents(new InventoryListener(),this);
        manager.registerEvents(new MoveListener(), this);
    }

    private void registerManagers() {
        new SqlManager();
        new MapManager();
        new PlayerManager();
    }

    private void registerGUIs() {
        new MapGUI();
        new ModeGUI();
        new PlayerInventory();
        new TopTimesGUI();
        new VotingGUI();
    }

    private void registerDetails() {
        host = sqlConfig.getConfiguration().getString("host");
        port = sqlConfig.getConfiguration().getInt("port");
        name = sqlConfig.getConfiguration().getString("name");
        username = sqlConfig.getConfiguration().getString("username");
        password = sqlConfig.getConfiguration().getString("password");
    }

    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.host + ":" + this.port + "/" + this.name, this.username, this.password);
    }

}
