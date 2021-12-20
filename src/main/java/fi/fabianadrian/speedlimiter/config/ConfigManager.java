package fi.fabianadrian.speedlimiter.config;

import fi.fabianadrian.speedlimiter.SpeedLimiter;
import org.spongepowered.configurate.ConfigurateException;

public final class ConfigManager {
    private final ConfigLoader<SpeedLimiterConfig> mainConfigLoader;

    private SpeedLimiterConfig mainConfig;

    public ConfigManager(SpeedLimiter speedLimiter) {
        this.mainConfigLoader = new ConfigLoader<>(
                SpeedLimiterConfig.class,
                speedLimiter.getDataFolder().toPath().resolve("main.conf"),
                options -> options.header("SpeedLimiter Main Configuration")
        );
    }

    public void loadConfigs() {
        try {
            this.mainConfig = this.mainConfigLoader.load();
            this.mainConfigLoader.save(mainConfig);
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to load config", e);
        }
    }

    public SpeedLimiterConfig mainConfig() {
        return mainConfig;
    }
}
