package tr.web.minelab.minelab.guis;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import tr.web.minelab.minelab.MineLAB;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

public class TopGui extends HInventory {
    public TopGui(@Nonnull String id, @Nonnull String title, int size, @Nonnull InventoryType type) {
        super(id, title, size, type);
    }

    @Override
    protected void onOpen(@Nonnull Player player) {
        int rank = 0;
        for(Map.Entry<String, Integer> x : MineLAB.getDataSource().getTop10().entrySet()) {
            rank++;
            if(rank >= MineLAB.getInstance().getConfig().getInt("TopGui.rows")*9) return;
            ItemStack itemSkull = XMaterial.PLAYER_HEAD.parseItem();
            SkullMeta skullMeta = (SkullMeta) itemSkull.getItemMeta();
            skullMeta.setOwner(x.getKey());
            int finalRank = rank;
            skullMeta.setLore(
                    MineLAB.getInstance().getConfig().getStringList("TopGui.item.lore")
                            .stream()
                            .map(line -> ChatColor.translateAlternateColorCodes('&', line
                                    .replaceAll("%player%", x.getKey())
                                    .replaceAll("%rank%", String.valueOf(finalRank))
                                    .replaceAll("%credit%", String.valueOf(x.getValue()))))
                            .collect(Collectors.toList()));
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e" + x.getKey()));
            itemSkull.setItemMeta(skullMeta);
            setItem(finalRank, itemSkull);
        }
    }
}
