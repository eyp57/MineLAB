package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import net.minexon.minexon.MineXON;
import net.minexon.minexon.guis.TopGui;

@BaseCommand(
        name = "topkredi",
        usage = "/topkredi",
        description = "Top Kredi Komutu",
        aliases = {
                "top10kredi",
                "topk",
                "credittop",
                "kreditop"
        }
)
public class TopCommand {
    @SubCommand()
    public void mainCommand(Player player, String[] args) {
        HInventory inventory = new TopGui("topGui", ChatColor.translateAlternateColorCodes('&', MineXON.getInstance().getConfig().getString("TopGui.title")), MineXON.getInstance().getConfig().getInt("TopGui.rows"), InventoryType.CHEST);
        inventory.open(player);
    }
}
