package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.minexon.minexon.MineXON;

@BaseCommand(
    name = "kredi",
    description = "Kredi Komutu",
    usage = "/kredi [ver/sil/bak] [oyuncu] [sayı]"
)
public class KrediCommand {
    @SubCommand(
            args = "ver"
    )
    public void verSubCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("minexon.admin")) {
            if(args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);
                int amount = Integer.parseInt(args[2]);
                if(player == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPlayerIsGiven")));
                    return;
                }
                if(amount == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPlayerIsGiven")));
                    return;
                }
                MineXON.getDataSource().addCredit(player, amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("creditGived").replaceAll("%player%", player.getName()).replaceAll("%amount%", String.valueOf(amount))));
            } else {
                sender.sendMessage("/kredi ver [oyuncu] [sayı]");
            }
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPerm")));
    }
    @SubCommand(
            args = "sil"
    )
    public void silSubCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("minexon.admin")) {
            if(args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);
                int amount = Integer.parseInt(args[2]);
                if (player == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPlayerIsGiven")));
                    return;
                }
                if (amount == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPlayerIsGiven")));
                    return;
                }
                MineXON.getDataSource().removeCredit(player, amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("creditRemoved").replaceAll("%player%", player.getName()).replaceAll("%amount%", String.valueOf(amount))));
            } else {
                sender.sendMessage("/kredi sil [oyuncu] [sayı]");
            }
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPerm")));
    }
    @SubCommand(
            args = "bak"
    )
    public void bakSubCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("minexon.admin")) {
            if(args.length == 2) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPlayerIsGiven")));
                    return;
                }
                String credit = MineXON.getDataSource().getCredit(player.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("credits").replaceAll("%player%", player.getName()).replaceAll("%amount%", credit)));
            } else {
                sender.sendMessage("/kredi bak [oyuncu]");
            }
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MineXON.getLanguage().getString("noPerm")));
    }
}
