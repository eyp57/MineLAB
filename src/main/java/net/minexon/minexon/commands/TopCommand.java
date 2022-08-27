package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import net.minexon.minexon.MineXON;
import net.minexon.minexon.menus.TopGui;

@BaseCommand(
        name = "topkredi",
        usage = "/topkredi",
        description = "Top Kredi Komutu",
        aliases = {
                "top10kredi",
                "topk",
                "credittop",
                "kreditop"
        })
public class TopCommand {

    private final MineXON plugin;

    public TopCommand(MineXON plugin) {
        this.plugin = plugin;
    }

    @SubCommand()
    public void main(Player player, String[] args) {
        String title = MineXON.color(plugin.getConfig().getString("TopGui.title"));
        int size = plugin.getConfig().getInt("TopGui.rows");

        HInventory inventory = new TopGui(plugin, "topGui", title, size, InventoryType.CHEST);
        inventory.open(player);
    }

}
