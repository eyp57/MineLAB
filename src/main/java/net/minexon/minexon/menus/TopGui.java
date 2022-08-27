package net.minexon.minexon.menus;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.ui.inventory.HInventory;
import net.minexon.minexon.MineXON;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TopGui extends HInventory {

    private final MineXON plugin;
    private final int rows;

    public TopGui(MineXON plugin, String id, String title, int size, InventoryType type) {
        super(id, title, size, type);
        this.plugin = plugin;

        rows = plugin.getConfig().getInt("TopGui.rows");
    }

    @Override
    protected void onOpen(@NotNull Player player) {
        int rank = 0;

        for (Map.Entry<String, Integer> entry : plugin.getDatabase().getTop10().entrySet()) {
            rank++;

            if (rank >= rows * 9) return;

            ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
            Objects.requireNonNull(item);

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            Objects.requireNonNull(meta);

            meta.setOwner(entry.getKey());

            List<String> lore = new ArrayList<>();
            for (String line : plugin.getConfig().getStringList("TopGui.item.lore")) {
                String newLine = MineXON.color(line)
                        .replaceAll("%player%", entry.getKey())
                        .replaceAll("%rank%", String.valueOf(rank))
                        .replaceAll("%credit%", String.valueOf(entry.getValue()));

                lore.add(newLine);
            }

            meta.setLore(lore);
            meta.setDisplayName(ChatColor.YELLOW + entry.getKey());

            item.setItemMeta(meta);
            setItem(rank, item);
        }
    }
}
