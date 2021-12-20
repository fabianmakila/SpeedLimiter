package fi.fabianadrian.speedlimiter;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import fi.fabianadrian.speedlimiter.config.SpeedLimiterConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRiptideEvent;

public class Events implements Listener {

    private final SpeedLimiter plugin;
    private final CooldownManager cooldownManager;
    private final SpeedLimiterConfig mainConfig;

    public Events(SpeedLimiter speedLimiter) {
        this.plugin = speedLimiter;
        this.cooldownManager = speedLimiter.getCooldownManager();
        this.mainConfig = speedLimiter.getConfigManager().mainConfig();
    }

    private boolean isDisabledWorld(World world) {
        return mainConfig.disabledWorlds().contains(world.getName());
    }

    private Component cooldownComponent(long cooldown) {
        return MiniMessage.get().parse(mainConfig.messages().cooldownLeft(), "seconds", cooldown);
    }

    @EventHandler
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        if (event.getPlayer().hasPermission(Constants.PERMISSION_BYPASS) || isDisabledWorld(event.getPlayer().getWorld()))
            return;

        long cooldown = cooldownManager.getCooldown(event.getPlayer().getUniqueId());
        if (cooldown > 0) {
            event.setCancelled(true);

            event.getPlayer().sendActionBar(cooldownComponent(cooldown));
            return;
        }

        cooldownManager.updateCooldown(event.getPlayer().getUniqueId(), CooldownManager.CooldownType.FIREWORK);
    }

    @EventHandler
    public void onPlayerRiptideEvent(PlayerRiptideEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(Constants.PERMISSION_BYPASS) || isDisabledWorld(event.getPlayer().getWorld())) return;

        long cooldown = cooldownManager.getCooldown(event.getPlayer().getUniqueId());
        if (cooldown > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(player.getLocation()), 1);

            event.getPlayer().sendActionBar(cooldownComponent(cooldown));
            return;
        }

        cooldownManager.updateCooldown(event.getPlayer().getUniqueId(), CooldownManager.CooldownType.TRIDENT);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldownManager.removePlayer(event.getPlayer().getUniqueId());
    }


    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.isGliding()) return;

        if (player.hasPermission(Constants.PERMISSION_BYPASS) || isDisabledWorld(player.getWorld()) || player.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getType() == Material.AIR)
            return;

        cooldownManager.removePlayer(player.getUniqueId());
    }
}
