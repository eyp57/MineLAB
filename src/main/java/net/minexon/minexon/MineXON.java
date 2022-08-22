package net.minexon.minexon;

import com.hakan.core.HCore;
import com.hakan.core.utils.yaml.HYaml;
import net.minexon.minexon.commands.KrediCommand;
import net.minexon.minexon.commands.ShopCommand;
import net.minexon.minexon.commands.TopCommand;
import net.minexon.minexon.hooks.PlaceholderAPI;
import net.minexon.minexon.utils.DataSource;
import net.minexon.minexon.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class MineXON extends JavaPlugin implements Listener {
    private static MineXON instance;
    private static Connection connection;
    private static DataSource dataSource;
    private static HYaml language;
    @Override
    public void onEnable() {
        // Plugin startup logic
        HCore.initialize(this);
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&eEklenti aktifleştiriliyor..."));
        String javaVersion = System.getProperty("java.version");
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&bKullanılan Java Sürümü: " + javaVersion));
        Metrics metrics = new Metrics(this, 14696);
        metrics.addCustomChart(new Metrics.DrilldownPie("javaVersion", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.8")) {
                map.put("Java 8", entry);
            } else if (javaVersion.startsWith("9")) {
                map.put("Java 9", entry);
            } else if (javaVersion.startsWith("11")) {
                map.put("Java 11", entry);
            } else if (javaVersion.startsWith("12")) {
                map.put("Java 12", entry);
            } else if (javaVersion.startsWith("16")) {
                map.put("Java 16", entry);
            } else if (javaVersion.startsWith("17")) {
                map.put("Java 17", entry);
            }  else if (javaVersion.startsWith("18")) {
                map.put("Java 18", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
        saveDefaultConfig();
        saveConfig();
        instance = this;
        language = HYaml.create(this, "language.yml", "language.yml");
        try {
            dataSource = new DataSource();
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&aDatabase bağlantısı kuruldu."));
        } catch (SQLException ex) {
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&cDatabase bağlantısı kurulamadı."));
            ex.printStackTrace();
            this.setEnabled(false);
            return;
        }
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderAPI().register();
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
        HCore.asyncScheduler().every(5*1200L).run(() -> {
            getLogger().info("All MineXON data reloaded.");
            dataSource.updateLastSupporterCredit();
            dataSource.updateLastSupporter();
            dataSource.updateTop10();
            if(getConfig().getBoolean("ShopSystem")) {
                dataSource.updateShop();
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                dataSource.updateCredit(player);
                dataSource.updateSocialAccounts(player);
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        HCore.registerCommands(new KrediCommand());
        HCore.registerCommands(new TopCommand());
        HCore.registerCommands(new ShopCommand());
    }

    public static MineXON getInstance() {
        return instance;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            dataSource.updateCredit(p);
            dataSource.updateSocialAccounts(p);
        });
    }

    public static HYaml getLanguage() {
        return language;
    }
}
