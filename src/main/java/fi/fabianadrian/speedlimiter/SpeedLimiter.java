package fi.fabianadrian.speedlimiter;

import fi.fabianadrian.speedlimiter.command.Commands;
import fi.fabianadrian.speedlimiter.command.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

public class SpeedLimiter extends JavaPlugin {

    private ConfigManager configManager;
    private CooldownManager cooldownManager;
    private ChatManager chatManager;

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        try {
            configManager = new ConfigManager(this);
        } catch (ConfigurateException e) {
            getLogger().severe("Error loading config, disabling plugin!");
            pluginManager.disablePlugin(this);
            return;
        }

        cooldownManager = new CooldownManager(this);
        chatManager = new ChatManager(this);

        PluginCommand pluginCommand = getCommand("speedlimiter");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(new Commands(this));
            pluginCommand.setTabCompleter(new TabCompletion());
        }

        pluginManager.registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

}
