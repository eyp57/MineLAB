package net.minexon.minexon.models;

public class Product {

    private final String name;
    private final String command;
    private final int id;
    private final int price;

    public Product(int id, String name, String command, int price) {
        this.name = name;
        this.command = command;
        this.id = id;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

}
