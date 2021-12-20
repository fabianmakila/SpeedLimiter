package fi.fabianadrian.speedlimiter;

import fi.fabianadrian.speedlimiter.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedLimiter extends JavaPlugin {

    private Logger logger;
    private ConfigManager configManager;
    private CooldownManager cooldownManager;

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }


    @Override
    public void onEnable() {
        this.logger = LoggerFactory.getLogger(this.getName());
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfigs();

        this.cooldownManager = new CooldownManager(this);

        PluginCommand pluginCommand = getCommand("speedlimiter");
        if (pluginCommand != null) {
            CommandHandler commandHandler = new CommandHandler(this);
            pluginCommand.setExecutor(commandHandler);
            pluginCommand.setTabCompleter(commandHandler);
        }

        Bukkit.getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reload() {
        this.configManager.loadConfigs();
    }

    public Logger logger() {
        return this.logger;
    }
}
