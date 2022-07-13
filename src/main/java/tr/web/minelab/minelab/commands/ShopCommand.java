package tr.web.minelab.minelab.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tr.web.minelab.minelab.MineLAB;

@BaseCommand(
        name = "market",
        usage = "/market [urunler/al] (ürün id)"
)
public class ShopCommand implements HCommandAdapter {
    @SubCommand(
            args = "al"
    )
    public void alSubCommand(Player player, String[] args) {
        if(args.length == 2) {
            int id = Integer.parseInt(args[0]);
            int price = MineLAB.getDataSource().getProductPriceById(id);
            String[] commands = MineLAB.getDataSource().getProductCommandsById(id);
            if(commands == null || MineLAB.getDataSource().getShop().get(id) == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
            }
            MineLAB.getDataSource().updateCredit(player);
            if(price >= Integer.parseInt(MineLAB.getDataSource().getCredit(player.getUniqueId()))) {
                MineLAB.getDataSource().removeCredit(player, price);
                for (String cmd : commands) {
                    cmd = cmd.replaceAll("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("productBuySuccess").replaceAll("%productId%", String.valueOf(id)).replaceAll("%product%", MineLAB.getDataSource().getShop().get(id))));
            }
        } else {
            player.sendMessage("/market al (ürün id)");
        }
    }
}
