package fi.fabianadrian.speedlimiter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Constants {
    public static final Component COMMAND_PREFIX = MiniMessage.get().parse("<white>[<aqua>Speedlimiter</aqua>]</white>");

    public static final String PERMISSION_ADMIN = "speedlimiter.admin";
    public static final String PERMISSION_BYPASS = "speedlimiter.bypass";
}
