package fi.fabianadrian.speedlimiter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import fi.fabianadrian.speedlimiter.config.SpeedLimiterConfig;
import fi.fabianadrian.speedlimiter.util.Components;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private final SpeedLimiter speedLimiter;
    private final SpeedLimiterConfig mainConfig;
    private final List<String> COMMANDS = ImmutableList.of("reload");

    public CommandHandler(SpeedLimiter speedLimiter) {
        this.speedLimiter = speedLimiter;
        this.mainConfig = speedLimiter.getConfigManager().mainConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Constants.PERMISSION_ADMIN)) return false;

        if (args.length < 1) {
            this.about(sender);
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            this.reload(sender);
            return true;
        }

        return true;
    }

    public void about(Audience audience) {
        if (speedLimiter.getDescription().getWebsite() == null) {
            //Website should never be null :P
            return;
        }

        final String authors = Joiner.on(", ").join(speedLimiter.getDescription().getAuthors());
        Stream.of(
                text()
                        .content("SpeedLimiter version ")
                        .append(text(speedLimiter.getDescription().getVersion(), AQUA))
                        .append(text(" by "))
                        .append(text(authors, AQUA))
                        .build(),
                text(speedLimiter.getDescription().getWebsite(), GREEN)

        ).forEach(audience::sendMessage);
    }

    public void reload(Audience audience) {
        try {
            this.speedLimiter.reload();
        } catch (Exception e) {
            this.speedLimiter.logger().warn("Failed to reload SpeedLimiter. Ensure there are no errors in your config files.", e);

            audience.sendMessage(Components.ofChildren(
                    Constants.COMMAND_PREFIX,
                    space(),
                    mainConfig.messages().reloadFailed()
            ));
            return;
        }

        audience.sendMessage(Components.ofChildren(
                Constants.COMMAND_PREFIX,
                space(),
                mainConfig.messages().reloadComplete()));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length < 2) {
            return COMMANDS;
        }

        return null;
    }
}
