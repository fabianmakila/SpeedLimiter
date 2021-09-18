package fi.fabianadrian.speedlimiter;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
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
    private final ChatManager chatManager;

    public Events(SpeedLimiter plugin) {
        this.plugin = plugin;
        cooldownManager = plugin.getCooldownManager();
        chatManager = plugin.getChatManager();
    }

    private boolean isDisabledWorld(World world) {
        return plugin.getConfigManager().getDisabledWorlds().contains(world.getName());
    }

    @EventHandler
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        if (event.getPlayer().hasPermission("speedlimiter.bypass") || isDisabledWorld(event.getPlayer().getWorld()))
            return;

        long cooldown = cooldownManager.getCooldown(event.getPlayer().getUniqueId());
        if (cooldown > 0) {
            event.setCancelled(true);
            chatManager.sendActionBar(event.getPlayer(), "cooldown-left", "seconds", String.valueOf(cooldown));
            return;
        }

        cooldownManager.updateCooldown(event.getPlayer().getUniqueId(), CooldownManager.CooldownType.FIREWORK);
    }

    @EventHandler
    public void onPlayerRiptideEvent(PlayerRiptideEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("speedlimiter.bypass") || isDisabledWorld(event.getPlayer().getWorld())) return;

        long cooldown = cooldownManager.getCooldown(event.getPlayer().getUniqueId());
        if (cooldown > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(player.getLocation()), 1);

            chatManager.sendActionBar(event.getPlayer(), "cooldown-left", "seconds", String.valueOf(cooldown));
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

        if (player.hasPermission("speedlimiter.bypass") || isDisabledWorld(player.getWorld()) || player.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getType() == Material.AIR)
            return;

        cooldownManager.removePlayer(player.getUniqueId());
    }
}
