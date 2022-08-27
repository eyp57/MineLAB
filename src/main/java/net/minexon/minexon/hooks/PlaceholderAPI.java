package net.minexon.minexon.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minexon.minexon.MineXON;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final MineXON plugin;

    public PlaceholderAPI(MineXON plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "minelab";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "zRooter";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        if (params.equals("kredi")) {
            return player == null ? "0" : String.valueOf(plugin.getDatabase().getCredits(player.getName()));
        }
        else if (params.equals("son_bagisci")) {
            return plugin.getDatabase().getLastSupporter();
        }
        else if (params.equals("son_bagisci_kredi")) {
            return plugin.getDatabase().getLastSupporterCredit();
        }
        return null;
    }

}
