package net.minexon.minexon.commands;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import net.minexon.minexon.models.Product;
import net.minexon.minexon.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.minexon.minexon.MineXON;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@BaseCommand(name = "market", usage = "/market [urunler/al] (ürün id)")
public class ShopCommand {

    private final MineXON plugin;

    public ShopCommand(MineXON plugin) {
        this.plugin = plugin;
    }

    @SubCommand(args = {"al"})
    public void buy(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("/market al (ürün id)");
            return;
        }
        if (!NumberUtils.isNumeric(args[1])) {
            player.sendMessage(MineXON.color("&cAlmak istediğin ürünün id numarasını yazman gerekiyor"));
            return;
        }
        int id = Integer.parseInt(args[1]);

        Product product = plugin.getDatabase().getProduct(id);

        if (product == null) {
            String message = plugin.getLanguage().getString("noProductFound").replaceAll("%id%", String.valueOf(id));
            player.sendMessage(MineXON.color(message));
            return;
        }

        int price = product.getPrice();
        String name = product.getName();
        String commands = product.getCommand();

        if (plugin.getDatabase().getCredits(player.getName()) < price) {
            player.sendMessage(MineXON.color(plugin.getLanguage().getString("notEnoughCredit")));
            return;
        }

        plugin.getDatabase().removeCredits(player.getName(), price);

        Matcher matcher = Pattern.compile("(((?<=,\")|(?<=\\[\"))(?<command>.*?)(?=\"))").matcher(commands);
        while (matcher.find()) {
            String command = matcher.group("command")
                    .replaceAll("%player%", player.getName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        String message = plugin.getLanguage().getString("productBuySuccess")
                .replaceAll("%productId%", String.valueOf(id))
                .replaceAll("%product%", name);

        player.sendMessage(MineXON.color(message));
    }

    @SubCommand(args = {"urunler"})
    public void products(CommandSender sender, String args[]) {
        for (Map.Entry<Integer, Product> entry : plugin.getDatabase().getProducts().entrySet()) {
            String id = String.valueOf(entry.getKey());
            String name = entry.getValue().getName();

            sender.sendMessage(ChatColor.GRAY + id + " - " + ChatColor.RED + name);
        }
    }

}
