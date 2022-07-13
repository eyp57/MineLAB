package tr.web.minelab.minelab.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tr.web.minelab.minelab.MineLAB;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Map;

@BaseCommand(
        name = "market",
        usage = "/market [urunler/al] (ürün id)"
)
public class ShopCommand implements HCommandAdapter {
    @SubCommand(
            args = {
                    "al"
            }
    )
    public void alSubCommand(Player player, String[] args) {
        if(args.length == 2) {
            int id = Integer.parseInt(args[1]);
            try {
                int price = MineLAB.getDataSource().getProductPriceById(id);
                Array commands = MineLAB.getDataSource().getProductCommandsById(id);
                String[] cmds = (String[]) commands.getArray();
                if (commands == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
                }
                MineLAB.getDataSource().updateCredit(player);
                if (price >= Integer.parseInt(MineLAB.getDataSource().getCredit(player.getUniqueId()))) {
                    MineLAB.getDataSource().removeCredit(player, price);
                    for(int i = 0; i < cmds.length; i++) {
                        String cmd = cmds[i].replaceAll("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("productBuySuccess").replaceAll("%productId%", String.valueOf(id)).replaceAll("%product%", MineLAB.getDataSource().getShop().get(id))));
                }
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage("/market al (ürün id)");
        }
    }
    @SubCommand(
            args = {
                    "urunler"
            }
    )
    public void urunlerSubCommand(CommandSender sender, String args[]) {
        for(Map.Entry<Integer, String> x : MineLAB.getDataSource().getShop().entrySet()) {
            int id = x.getKey();
            String product = x.getValue();
            sender.sendMessage(ChatColor.GRAY + String.valueOf(id) + " - " + ChatColor.RED + product);
        }
    }
}
