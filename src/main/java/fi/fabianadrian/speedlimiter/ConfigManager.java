package fi.fabianadrian.speedlimiter;

import com.google.common.collect.Sets;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO Recode this someday, does the job for now...
public class ConfigManager {

    private final SpeedLimiter plugin;

    private final YamlConfigurationLoader mainConfigLoader;
    private final YamlConfigurationLoader messagesConfigLoader;
    private ConfigurationNode mainConfig;
    private ConfigurationNode messagesConfig;

    private Set<String> disabledWorlds = new HashSet<>();

    public ConfigManager(SpeedLimiter plugin) throws ConfigurateException {
        this.plugin = plugin;

        mainConfigLoader = YamlConfigurationLoader.builder()
                .path(plugin.getDataFolder().toPath().resolve("config.yml"))
                .build();

        messagesConfigLoader = YamlConfigurationLoader.builder()
                .path(plugin.getDataFolder().toPath().resolve("messages.yml"))
                .build();

        load();
    }

    public void load() throws ConfigurateException {
        if (!new File(plugin.getDataFolder(), "config.yml").exists())
            plugin.saveResource("config.yml", false);
        mainConfig = mainConfigLoader.load();

        if (!new File(plugin.getDataFolder(), "messages.yml").exists())
            plugin.saveResource("messages.yml", false);
        messagesConfig = messagesConfigLoader.load();

        List<String> stringList = mainConfig.node("disabled-worlds").getList(String.class);
        if (stringList == null) return;
        disabledWorlds = Sets.newHashSet(stringList);
    }

    public ConfigurationNode getCooldownNode() {
        return mainConfig.node("cooldowns");
    }

    public Set<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public ConfigurationNode getMessageNode() {
        return messagesConfig;
    }
}
