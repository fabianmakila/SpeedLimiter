package fi.fabianadrian.speedlimiter;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private final ConfigManager configManager;
    private final HashMap<UUID, Long> cooldownMap = new HashMap<>();

    public CooldownManager(SpeedLimiter plugin) {
        this.configManager = plugin.getConfigManager();
    }

    public void updateCooldown(UUID uuid, CooldownType cooldownType) {
        int length = switch (cooldownType) {
            case FIREWORK -> configManager.getCooldownNode().node("firework-boost").getInt(5);
            case TRIDENT -> configManager.getCooldownNode().node("trident-riptide").getInt(15);
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
