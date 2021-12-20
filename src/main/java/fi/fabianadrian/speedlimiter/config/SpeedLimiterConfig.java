package fi.fabianadrian.speedlimiter.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Set;

@ConfigSerializable
public class SpeedLimiterConfig {

    private Cooldowns cooldowns = new Cooldowns();

    private Set<String> disabledWorlds;

    private Messages messages = new Messages();

    public Cooldowns cooldowns() {
        return cooldowns;
    }

    public Set<String> disabledWorlds() {
        return disabledWorlds;
    }

    public Messages messages() {
        return messages;
    }

    @ConfigSerializable
    public static final class Cooldowns {
        private int fireworkBoost = 5;
        private int tridentRiptide = 10;

        public int firework() {
            return fireworkBoost;
        }

        public int trident() {
            return tridentRiptide;
        }
    }

    @ConfigSerializable
    public static final class Messages {
        private String reloadComplete = "<green>Reload complete!";
        private String reloadFailed = "<red>Reload failed! Ensure there are no errors in your config files. See console for more details.";
        private String cooldownLeft = "<gold>Please wait <white><seconds> seconds</white> before using that again!";

        public Component reloadComplete() {
            return MiniMessage.get().parse(reloadComplete);
        }

        public Component reloadFailed() {
            return MiniMessage.get().parse(reloadFailed);
        }

        public String cooldownLeft() {
            return cooldownLeft;
        }
    }
}
