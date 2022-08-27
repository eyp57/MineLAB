package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import net.minexon.minexon.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.minexon.minexon.MineXON;

@BaseCommand(name = "kredi", description = "Kredi Komutu", usage = "/kredi [ver/sil/bak] [oyuncu] [sayı]")
public class KrediCommand {

    private final MineXON plugin;

    public KrediCommand(MineXON plugin) {
        this.plugin = plugin;
    }

    @SubCommand(args = "ver")
    public void give(CommandSender sender, String[] args) {
        if (!sender.hasPermission("minexon.admin")) {
            sender.sendMessage(MineXON.color(plugin.getLanguage().getString("noPerm")));
            return;
        }
        if (args.length != 3) {
            sender.sendMessage(MineXON.color("&c/kredi ver [oyuncu] [sayı]"));
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(MineXON.color(plugin.getLanguage().getString("noPlayerIsGiven")));
            return;
        }
        if (!NumberUtils.isNumeric(args[2])) {
            player.sendMessage(MineXON.color("&cMiktarın sayı cinsinden olması gerekiyor"));
            return;
        }
        int amount = Integer.parseInt(args[2]);

        plugin.getDatabase().addCredits(player.getName(), amount);

        String message = plugin.getLanguage().getString("creditGived")
                .replaceAll("%player%", player.getName())
                .replaceAll("%amount%", String.valueOf(amount));

        sender.sendMessage(MineXON.color(message));
    }

    @SubCommand(args = "sil")
    public void remove(CommandSender sender, String[] args) {
        if (!sender.hasPermission("minexon.admin")) {
            sender.sendMessage(plugin.getLanguage().getString("noPerm"));
            return;
        }
        if (args.length != 3) {
            sender.sendMessage(MineXON.color("&c/kredi sil [oyuncu] [sayı]"));
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(MineXON.color(plugin.getLanguage().getString("noPlayerIsGiven")));
            return;
        }
        if (!NumberUtils.isNumeric(args[2])) {
            player.sendMessage(MineXON.color("&cMiktarın sayı cinsinden olması gerekiyor"));
            return;
        }
        int amount = Integer.parseInt(args[2]);

        plugin.getDatabase().removeCredits(player.getName(), amount);

        String message = plugin.getLanguage().getString("creditRemoved")
                .replaceAll("%player%", player.getName())
                .replaceAll("%amount%", String.valueOf(amount));

        sender.sendMessage(MineXON.color(message));
    }

    @SubCommand(args = "bak")
    public void check(CommandSender sender, String[] args) {
        if (!sender.hasPermission("minexon.admin")) {
            sender.sendMessage(MineXON.color(plugin.getLanguage().getString("noPerm")));
            return;
        }
        if (args.length != 2) {
            sender.sendMessage(MineXON.color("&c/kredi bak [oyuncu]"));
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(MineXON.color(plugin.getLanguage().getString("noPlayerIsGiven")));
            return;
        }
        int credits = plugin.getDatabase().getCredits(player.getName());

        String message = plugin.getLanguage().getString("credits")
                .replaceAll("%player%", player.getName())
                .replaceAll("%amount%", String.valueOf(credits));

        sender.sendMessage(MineXON.color(message));
    }

}
