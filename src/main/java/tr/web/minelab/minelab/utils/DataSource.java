package tr.web.minelab.minelab.utils;

import org.bukkit.entity.Player;
import tr.web.minelab.minelab.MineLAB;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DataSource {
    private static Connection connection;
    public DataSource() throws SQLException {

        String username = MineLAB.getInstance().getConfig().getString("Database.username");
        String host = MineLAB.getInstance().getConfig().getString("Database.host");
        String password = MineLAB.getInstance().getConfig().getString("Database.password");
        String database = MineLAB.getInstance().getConfig().getString("Database.database");

        connection = DriverManager.getConnection("jdbc:mysql://"+host+"/" + database,username,password);
    }

    public static Connection getConnection() {
        return connection;
    }

    private String lastSupporter = "Yok";
    private String lastSupporterCredit = "0";

    private Map<UUID, String> credits = new HashMap<>();
    private Map<UUID, String> instagrams = new HashMap<>();
    private Map<UUID, String> twitters = new HashMap<>();
    private Map<UUID, String> youtubes = new HashMap<>();
    private Map<UUID, String> discords = new HashMap<>();
    private Map<UUID, String> skypes = new HashMap<>();
    private Map<String, Integer> top10 = new LinkedHashMap<>();
    private Map<Integer, String> shop = new LinkedHashMap<>();

    public String updateLastSupporter() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `creditHistory` ORDER BY `id` DESC LIMIT 1");
            if(resultSet.next())
                lastSupporter = resultSet.getString("username");
            return lastSupporter;

        } catch (SQLException ex) {
            return "dbErr";
        }
    }

    public String updateLastSupporterCredit() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `creditHistory` ORDER BY `id` DESC LIMIT 1");
            if(resultSet.next())
                lastSupporterCredit = resultSet.getString("amount");
            return lastSupporterCredit;
        } catch (SQLException ex) {
            return "dbErr";
        }
    }

    public void updateCredit(Player player) {
        String credit = "0";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `accounts` WHERE `username` = '" + player.getName() + "'");
            if (resultSet.next())
                credit = resultSet.getString("credit");
            credits.put(player.getUniqueId(), credit);
        } catch (SQLException ex) {
            credits.put(player.getUniqueId(), "0");
        }
    }
    public void removeCredit(Player player, int amount) {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("UPDATE `accounts` SET `credit` = credit - " + amount + " WHERE `username` = '" + player.getName() + "'");
            updateCredit(player);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void addCredit(Player player, int amount) {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("UPDATE `accounts` SET `credit` = credit + " + amount + " WHERE `username` = '" + player.getName() + "'");
            updateCredit(player);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void updateSocialAccounts(Player player) {
        String instagram = "-";
        String discord = "-";
        String twitter = "-";
        String skype = "-";
        String youtube = "-";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `accounts` WHERE `username` = '" + player.getName() + "'");
            if(resultSet.next()) {
                instagram = resultSet.getString("instagram");
                discord = resultSet.getString("discord");
                twitter = resultSet.getString("twitter");
                skype = resultSet.getString("skype");
                youtube = resultSet.getString("youtube");
            }
            instagrams.put(player.getUniqueId(), instagram);
            discords.put(player.getUniqueId(), discord);
            twitters.put(player.getUniqueId(), twitter);
            youtubes.put(player.getUniqueId(), youtube);
            skypes.put(player.getUniqueId(), skype);
        } catch (SQLException e) {
            instagrams.put(player.getUniqueId(), "-");
            discords.put(player.getUniqueId(), "-");
            skypes.put(player.getUniqueId(), "-");
            youtubes.put(player.getUniqueId(), "-");
            twitters.put(player.getUniqueId(), "-");
        }
    }
    public void updateTop10() {
        top10.clear();
        int limit = 26;
        if(MineLAB.getInstance().getConfig().getInt("TopGui.Players") <= 26)
            limit = MineLAB.getInstance().getConfig().getInt("TopGui.Players");
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `accounts` ORDER BY `credit` DESC LIMIT " + limit);
            while(resultSet.next()) {
                String name = resultSet.getString("username");
                int credit = resultSet.getInt("credit");
                this.top10.put(name, credit);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void updateShop() {
        shop.clear();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `categoryProduct` ORDER BY `id` DESC");
            while(resultSet.next()) {
                String product = resultSet.getString("name");
                int serverId = resultSet.getInt("serverID");
                int id = resultSet.getInt("id");
                if(serverId == MineLAB.getInstance().getConfig().getInt("ServerId")) {
                    shop.put(id, product);
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    public String[] getProductCommandsById(Integer id) {
        if(shop.get(id) == null) return null;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `categoryProduct` WHERE `id` = " + id);
            if(resultSet.next()) {
                Array commands = resultSet.getArray("productCommand");
                String[] cmds = (String[]) commands.getArray();
                return cmds;
            }
        } catch(SQLException ex) {
            return null;
        }
        return null;
    }
    public Integer getProductPriceById(Integer id) {
        if(shop.get(id) == null) return null;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `categoryProduct` WHERE `id` = " + id);
            if(resultSet.next()) {
                int price = resultSet.getInt("price");
                return price;
            }
        } catch(SQLException ex) {
            return null;
        }
        return null;
    }
    public String getCredit(UUID uuid) {
        return credits.get(uuid);
    }
    public String getInstagram(UUID uuid) {
        return instagrams.get(uuid);
    }
    public String getYouTube(UUID uuid) {
        return youtubes.get(uuid);
    }
    public String getSkype(UUID uuid) {
        return skypes.get(uuid);
    }
    public String getTwitter(UUID uuid) {
        return twitters.get(uuid);
    }
    public String getDiscord(UUID uuid) {
        return discords.get(uuid);
    }
    public Map<String, Integer> getTop10() {
        return top10;
    }
    public String getLastSupporter() {
        return lastSupporter;
    }
    public String getLastSupporterCredit() {
        return lastSupporterCredit;
    }
    public Map<Integer, String> getShop() {
        return shop;
    }
}
