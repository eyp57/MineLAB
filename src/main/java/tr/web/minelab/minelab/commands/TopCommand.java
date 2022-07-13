package tr.web.minelab.minelab.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import tr.web.minelab.minelab.MineLAB;
import tr.web.minelab.minelab.guis.TopGui;

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
public class TopCommand implements HCommandAdapter {
    @SubCommand()
    public void mainCommand(Player player, String[] args) {
        HInventory inventory = new TopGui("topGui", ChatColor.translateAlternateColorCodes('&', MineLAB.getInstance().getConfig().getString("TopGui.title")), MineLAB.getInstance().getConfig().getInt("TopGui.rows"), InventoryType.CHEST);
        inventory.open(player);
    }
}
