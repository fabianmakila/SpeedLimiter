package fi.fabianadrian.speedlimiter;

import fi.fabianadrian.speedlimiter.config.SpeedLimiterConfig;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private final SpeedLimiterConfig mainConfig;
    private final HashMap<UUID, Long> cooldownMap = new HashMap<>();

    public CooldownManager(SpeedLimiter plugin) {
        this.mainConfig = plugin.getConfigManager().mainConfig();
    }

    public void updateCooldown(UUID uuid, CooldownType cooldownType) {
        int length = switch (cooldownType) {
            case FIREWORK -> mainConfig.cooldowns().firework();
            case TRIDENT -> mainConfig.cooldowns().trident();
        };

        cooldownMap.put(uuid, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(length));
    }

    public void removePlayer(UUID uuid) {
        cooldownMap.remove(uuid);
    }

    public long getCooldown(UUID uuid) {
        return TimeUnit.MILLISECONDS.toSeconds(cooldownMap.getOrDefault(uuid, 0L) - System.currentTimeMillis());
    }

    public enum CooldownType {
        FIREWORK, TRIDENT
    }
}
