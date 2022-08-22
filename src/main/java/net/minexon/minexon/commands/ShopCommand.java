package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.minexon.minexon.MineXON;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@BaseCommand(
        name = "market",
        usage = "/market [urunler/al] (ürün id)"
)
public class ShopCommand {
    @SubCommand(
            args = {
                    "al"
            }
    )
    public void alSubCommand(Player player, String[] args) {
        if(args.length == 2) {
            int id = Integer.parseInt(args[1]);
            try {
                int price = MineXON.getDataSource().getProductPriceById(id);
                String commands = MineXON.getDataSource().getProductCommandsById(id);

                if (commands == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
                    return;
                }
                MineXON.getDataSource().updateCredit(player);
                if (Integer.parseInt(MineXON.getDataSource().getCredit(player.getUniqueId())) >= price) {
                    MineXON.getDataSource().removeCredit(player, price);

                    Matcher matcher = Pattern.compile("(((?<=,\")|(?<=\\[\"))(?<command>.*?)(?=\"))").matcher(commands);
                    while (matcher.find()) {
                        System.out.println(matcher.group("command"));
                        String cmd = matcher.group("command");
                        cmd = cmd.replaceAll("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("productBuySuccess").replaceAll("%productId%", String.valueOf(id)).replaceAll("%product%", MineXON.getDataSource().getShop().get(id))));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("notEnoughCredit")));
                }
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id))));
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
        for(Map.Entry<Integer, String> x : MineXON.getDataSource().getShop().entrySet()) {
            int id = x.getKey();
            String product = x.getValue();
            sender.sendMessage(ChatColor.GRAY + String.valueOf(id) + " - " + ChatColor.RED + product);
        }
    }
}
