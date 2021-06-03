package fi.fabianadrian.speedlimiter;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ConfigManager {

    private final SpeedLimiter plugin;

    private final YamlConfigurationLoader loader;

    private ConfigurationNode config;

    public ConfigManager(SpeedLimiter plugin) throws ConfigurateException {
        this.plugin = plugin;

        loader = YamlConfigurationLoader.builder()
                .path(plugin.getDataFolder().toPath().resolve("config.yml"))
                .build();

        load();
    }

    public void load() throws ConfigurateException {
        plugin.saveResource("config.yml", false);
        config = loader.load();
    }

    public ConfigurationNode getCooldownNode() {
        return config.node("cooldowns");
    }

    public ConfigurationNode getMessageNode() {
        return config.node("messages");
    }
}
