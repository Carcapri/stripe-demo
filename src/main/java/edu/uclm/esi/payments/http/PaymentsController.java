package edu.uclm.esi.payments.http;

import com.stripe.model.PaymentIntent;
import edu.uclm.esi.payments.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tienda")
public class PaymentsController {

    @Value("${stripe.publicKey}")
    private String stripePublicKey;

    @Autowired
    private PaymentService paymentService;

    // Shop page with product catalog (GET)
    @GetMapping(value = {"", "/"})
    public String catalogo(Model model) {
        model.addAttribute("productos", paymentService.getStripeProducts());
        return "catalogo";
    }

    // Checkout page (POST) - called when user clicks "Buy" on a product
    @PostMapping("/checkout")
    public String checkout(@RequestParam String priceId, 
                           @RequestParam int quantity, 
                           Model model) {
        try {
            PaymentIntent intent = paymentService.createPaymentIntent(priceId, quantity);
            
            // Pass necessary data to the checkout page
            model.addAttribute("stripePublicKey", stripePublicKey);
            model.addAttribute("clientSecret", intent.getClientSecret());
            model.addAttribute("amount", intent.getAmount() / 100.0);
            return "pasarela";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "catalogo";
        }
    }

    // Result page after redirection from Stripe (GET)
    @GetMapping("/resultado")
    public String resultado(@RequestParam("payment_intent") String intentId, Model model) {
        try {
            boolean exito = paymentService.verifyPayment(intentId);
            model.addAttribute("exito", exito);
            model.addAttribute("ref", intentId);
            return "resultado"; 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "resultado";
        }
    }
}