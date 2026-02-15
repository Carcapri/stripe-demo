package edu.uclm.esi.payments.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;          // Internal ID MongoDB
    private String intentId;    // Stripe id of the PaymentIntent
    private String priceId;     // Price ID
    private double amount;      // Total price (amount * quantity)
    private int quantity;       // Number of items bought
    private String status;      // PENDING, SUCCEEDED
    private LocalDateTime date; 

    public Transaction() {
        this.date = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIntentId() { return intentId; }
    public void setIntentId(String intentId) { this.intentId = intentId; }
    public String getPriceId() { return priceId; }
    public void setPriceId(String priceId) { this.priceId = priceId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}