package edu.uclm.esi.payments.model;

public class ProductInfo {
    private String id;       
    private String name;
    private String imagen;   
    private double price;   
    private String currency;

    public ProductInfo(String id, String name, String imagen, double price, String currency) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
        this.price = price;
        this.currency = currency;
    }

    // Getters obligatorios para Thymeleaf
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return imagen; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
}