package net.minexon.minexon.listener;

import net.minexon.minexon.MineXON;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final MineXON plugin;

    public PlayerListener(MineXON plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabase().updateCredits(player.getName());
            plugin.getDatabase().updateSocialMedia(player.getName());
        });
    }

}
