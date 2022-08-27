package net.minexon.minexon;

import com.hakan.core.HCore;
import com.hakan.core.utils.yaml.HYaml;
import net.minexon.minexon.commands.KrediCommand;
import net.minexon.minexon.commands.ShopCommand;
import net.minexon.minexon.commands.TopCommand;
import net.minexon.minexon.hooks.PlaceholderAPI;
import net.minexon.minexon.listener.PlayerListener;
import net.minexon.minexon.manager.DataSource;
import net.minexon.minexon.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class MineXON extends JavaPlugin implements Listener {

    private static DataSource database;
    private static HYaml language;

    @Override
    public void onEnable() {
        HCore.initialize(this);

        setMetrics();
        saveDefaultConfig();
        saveConfig();

        language = HYaml.create(this, "language.yml", "language.yml");

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
            getLogger().info("PlaceholderAPI detected you can use MineXON placeholders");
        }

        registerCommands();
        registerEvents();

        database = new DataSource(this);

        HCore.asyncScheduler().every(6000).run(() -> {
            database.updateLastSupporter();
            database.updateTop();

            if (getConfig().getBoolean("ShopSystem")) {
                database.updateShop();
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                database.updateCredits(player.getName());
                database.updateSocialMedia(player.getName());
            }
        });
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public DataSource getDatabase() {
        return database;
    }

    public HYaml getLanguage() {
        return language;
    }

    public void shutdown() {
        setEnabled(false);
    }

    private void setMetrics() {
        String java = System.getProperty("java.version");
        Metrics metrics = new Metrics(this, 14696);

        metrics.addCustomChart(new Metrics.DrilldownPie("javaVersion", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();

            entry.put(java, 1);

            if (java.startsWith("1.8")) {
                map.put("Java 8", entry);
            } else if (java.startsWith("9")) {
                map.put("Java 9", entry);
            } else if (java.startsWith("11")) {
                map.put("Java 11", entry);
            } else if (java.startsWith("16")) {
                map.put("Java 16", entry);
            } else if (java.startsWith("17")) {
                map.put("Java 17", entry);
            }  else if (java.startsWith("18")) {
                map.put("Java 18", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
    }

    private void registerCommands() {
        HCore.registerCommands(new KrediCommand(this));
        HCore.registerCommands(new TopCommand(this));
        HCore.registerCommands(new ShopCommand(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

}
