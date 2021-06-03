package fi.fabianadrian.speedlimiter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Arrays;

public class ChatManager {

    private final SpeedLimiter plugin;
    private final ConfigManager configManager;
    private final Component prefix;

    public ChatManager(SpeedLimiter plugin) {
        this.plugin = plugin;
        configManager = plugin.getConfigManager();

        prefix = Component.text("[")
                .color(NamedTextColor.WHITE)
                .append(Component.text("SpeedLimiter")
                        .color(NamedTextColor.AQUA)
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Author: FabianAdrian \nVersion: " + plugin.getDescription().getVersion()))
                        ))
                .append(Component.text("]")
                        .color(NamedTextColor.WHITE))
                .append(Component.text(" "));

    }

    private Component getComponent(String key, String... replacements) {
        String string = configManager.getMessageNode().node(key).getString("");
        if (string.isBlank()) {
            plugin.getLogger().warning("Missing message in config: " + key);
        }

        if (replacements != null)
            string = MessageFormat.format(string, Arrays.stream(replacements).toArray());

        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

    public void sendActionBar(Player player, String key, String... replacements) {
        player.sendActionBar(getComponent(key, replacements));
    }

    public void sendChatMessage(CommandSender sender, String key) {
        sender.sendMessage(prefix.append(getComponent(key)));
    }

    public void sendComponent(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }

    private String replace(String string, String... replacements) {

        if (replacements == null) return string;
        if (replacements.length % 2 != 0) throw new IllegalStateException();

        for (int i = 0; i < replacements.length; i++) {
            string = string.replace("%" + i + "%", replacements[i]);
        }

        return string;
    }
}
