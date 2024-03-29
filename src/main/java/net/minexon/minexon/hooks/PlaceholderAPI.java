package net.minexon.minexon.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minexon.minexon.MineXON;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "minelab";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zRooter";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equals("kredi")) {
            return player == null ? "0" : MineXON.getDataSource().getCredit(player.getUniqueId());
        }
        if(params.equals("son_bagisci")) {
            return MineXON.getDataSource().getLastSupporter();
        }
        if(params.equals("son_bagisci_kredi")) {
            return MineXON.getDataSource().getLastSupporterCredit();
        }
        return null;
    }

}
