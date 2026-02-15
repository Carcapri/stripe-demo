package edu.uclm.esi.payments.services;

import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Product;
import com.stripe.param.PriceListParams;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.payments.model.ProductInfo;
import edu.uclm.esi.payments.model.Transaction;
import edu.uclm.esi.payments.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private TransactionRepository transactionRepository;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey; 
        
    }

    public List<ProductInfo> getStripeProducts() {
        List<ProductInfo> catalog = new ArrayList<>();
        //List that avoid douplicate products
        List<String> addedNames = new ArrayList<>();

        try {
            PriceListParams params = PriceListParams.builder()
                    .setActive(true) //Only active prices
                    .addExpand("data.product") //Expand product details in the same call
                    .setLimit(20L) // Limit to 20 prices
                    .build();

            PriceCollection prices = Price.list(params);

            for (Price p : prices.getData()) {
                Product producto = p.getProductObject();
                String name = producto.getName();
                
            //To avoid some products that are not for sale, we can filter by name or metadata
            if (name.equalsIgnoreCase("Crédito circuito") || 
                name.contains("generacion de codigo cuantico")) {
                continue;
            }


            // To avoid duplicate products with different prices, we can use the name as a key
            if (addedNames.contains(name)) {
                continue;
            }

                String fotoUrl = null;
                if (!producto.getImages().isEmpty()) {
                    fotoUrl = producto.getImages().get(0);
                }

                catalog.add(new ProductInfo(
                    p.getId(),
                    producto.getName(),
                    fotoUrl,
                    p.getUnitAmount() / 100.0,
                    p.getCurrency().toUpperCase()
                ));
                
                addedNames.add(producto.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catalog;
    }

    public PaymentIntent createPaymentIntent(String priceId, int quantity) throws Exception {

        // Check price details from Stripe to avoid manipulation
        Price priceObj = Price.retrieve(priceId);
        long amountCents = priceObj.getUnitAmount() * quantity;

        // Create PaymentIntent with Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountCents)
                .setCurrency(priceObj.getCurrency())
                .putMetadata("priceId", priceId)
                //For only card payments, we can specify the payment method types to avoid confusion
                .addPaymentMethodType("card")
                .build();
        
        PaymentIntent intent = PaymentIntent.create(params);

        // Save transaction in DB with status PENDING
        Transaction t = new Transaction();
        t.setIntentId(intent.getId());
        t.setPriceId(priceId);
        t.setQuantity(quantity);
        t.setAmount(amountCents / 100.0);
        t.setStatus("PENDING");
        transactionRepository.save(t);

        return intent;
    }

    // Verify payment status after redirection from Stripe
    public boolean verifyPayment(String intentId) throws Exception {
        PaymentIntent intent = PaymentIntent.retrieve(intentId);
        Optional<Transaction> txOpt = transactionRepository.findByIntentId(intentId);

        if (txOpt.isPresent()) {
            Transaction tx = txOpt.get();
            tx.setStatus(intent.getStatus()); // "succeeded" 
            transactionRepository.save(tx);
            return "succeeded".equals(intent.getStatus());
        }
        return false;
    }

}