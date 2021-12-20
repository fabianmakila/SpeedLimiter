package fi.fabianadrian.speedlimiter.util;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public final class Components {
    public static TextComponent ofChildren(final ComponentLike... children) {
        if (children.length == 0) {
            return empty();
        }

        return text().append(children).build();
    }
}
