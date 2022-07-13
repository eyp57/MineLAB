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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                String commands = MineLAB.getDataSource().getProductCommandsById(id);

                if (commands == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
                    return;
                }
                MineLAB.getDataSource().updateCredit(player);
                if (Integer.parseInt(MineLAB.getDataSource().getCredit(player.getUniqueId())) >= price) {
                    MineLAB.getDataSource().removeCredit(player, price);

                    Matcher matcher = Pattern.compile("(((?<=,\")|(?<=\\[\"))(?<command>.*?)(?=\"))").matcher(commands);
                    while (matcher.find()) {
                        System.out.println(matcher.group("command"));
                        String cmd = matcher.group("command");
                        cmd = cmd.replaceAll("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("productBuySuccess").replaceAll("%productId%", String.valueOf(id)).replaceAll("%product%", MineLAB.getDataSource().getShop().get(id))));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("notEnoughCredit")));
                }
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineLAB.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
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
