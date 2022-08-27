package net.minexon.minexon.manager;

import com.hakan.core.utils.query.criteria.order.OrderType;
import com.hakan.core.utils.query.select.SelectQuery;
import net.minexon.minexon.MineXON;
import net.minexon.minexon.models.Product;
import net.minexon.minexon.models.SocialMedia;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSource {

    private final MineXON plugin;
    private Connection connection;

    public DataSource(MineXON plugin) {
        this.plugin = plugin;

        String username = plugin.getConfig().getString("Database.username");
        String host = plugin.getConfig().getString("Database.host");
        String password = plugin.getConfig().getString("Database.password");
        String database = plugin.getConfig().getString("Database.database");

        boolean isConnected = true;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+host+"/" + database, username, password);
        } catch (SQLException e) {
            isConnected = false;
            e.printStackTrace();
        }

        if (connection == null || !isConnected) plugin.shutdown();
    }

    private String lastSupporter = "Yok";
    private String lastSupporterCredit = "0";

    private final Map<String, Integer> credits = new HashMap<>();
    private final Map<String, SocialMedia> social = new HashMap<>();

    private final Map<String, Integer> creditsTop = new LinkedHashMap<>();
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public void updateLastSupporter() {
        try {
            ResultSet results = connection.prepareStatement("SELECT * FROM creditHistory ORDER BY id DESC LIMIT 1").executeQuery();

            if (results.next()) {
                lastSupporter = results.getString("username");
                lastSupporterCredit = results.getString("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCredits(String name) {
        try {
            ResultSet results = connection.prepareStatement("SELECT * FROM accounts WHERE username=?").executeQuery();

            if (results.next()) {
                int credit = (int) results.getDouble("credit");
                credits.put(name, credit);
            }
        } catch (SQLException e) {
            credits.put(name, 0);
        }
    }

    public void removeCredits(String name, int amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET credit=credit-? WHERE username=?");

            statement.setDouble(1, amount);
            statement.setString(2, name);

            statement.executeQuery();

            int base = credits.get(name);
            credits.put(name, base - amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCredits(String name, int amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET credit=credit+? WHERE username=?");

            statement.setDouble(1, amount);
            statement.setString(2, name);

            statement.executeQuery();

            int base = credits.get(name);
            credits.put(name, base + amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSocialMedia(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username=?");
            statement.setString(1, name);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                String instagram = results.getString("instagram");
                String discord = results.getString("discord");
                String twitter = results.getString("twitter");
                String skype = results.getString("skype");
                String youtube = results.getString("youtube");

                SocialMedia media = new SocialMedia(instagram, discord, twitter, skype, youtube);
                social.put(name, media);
            }
        } catch (SQLException e) {
            social.put(name, new SocialMedia());
        }
    }

    public void updateTop() {
        creditsTop.clear();

        int pref = plugin.getConfig().getInt("TopGui.Players");
        int limit = Math.min(pref, 26);

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts ORDER BY credit DESC LIMIT ?");
            statement.setInt(1, limit);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String name = results.getString("username");
                int credit = results.getInt("credit");

                creditsTop.put(name, credit);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateShop() {
        try {
            products.clear();

            PreparedStatement statement = connection.prepareStatement(new SelectQuery("categoryProduct").fromAll().orderBy(OrderType.DESC, "id").build());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int serverId = results.getInt("serverID");
                if (serverId != plugin.getConfig().getInt("ServerId")) continue;

                String name = results.getString("name");
                String command = results.getString("productCommand");

                int price = results.getInt("price");
                int id = results.getInt("id");

                Product product = new Product(id, name, command, price);
                products.put(id, product);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Product getProduct(Integer id) {
        return products.get(id);
    }

    public int getCredits(String name) {
        return credits.get(name);
    }

    public Map<String, SocialMedia> getSocialMedias() {
        return social;
    }

    public Map<String, Integer> getTop10() {
        return creditsTop;
    }

    public String getLastSupporter() {
        return lastSupporter;
    }

    public String getLastSupporterCredit() {
        return lastSupporterCredit;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

}
