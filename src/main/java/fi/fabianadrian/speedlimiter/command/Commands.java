package fi.fabianadrian.speedlimiter.command;

import fi.fabianadrian.speedlimiter.ChatManager;
import fi.fabianadrian.speedlimiter.SpeedLimiter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public class Commands implements CommandExecutor {

    private final SpeedLimiter plugin;
    private final ChatManager chatManager;

    public Commands(SpeedLimiter plugin) {
        this.plugin = plugin;
        chatManager = plugin.getChatManager();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("speedlimiter.admin")) return false;

        if (args.length > 0 && "reload".equalsIgnoreCase(args[0])) {
            try {
                plugin.getConfigManager().load();
                chatManager.sendChatMessage(sender, "reload-complete");
            } catch (ConfigurateException e) {
                chatManager.sendChatMessage(sender, "reload-failed");
                e.printStackTrace();
            }

            return true;
        }

        chatManager.sendComponent(sender, Component.text("SpeedLimiter version " + plugin.getDescription().getVersion() + "\nAuthor: FabianAdrian").color(NamedTextColor.AQUA));
        return true;
    }
}
