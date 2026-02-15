package edu.uclm.esi.payments.repository;

import edu.uclm.esi.payments.model.Transaction;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    // Nos servirá para buscar la transacción si Stripe nos confirma el pago más tarde
    Optional<Transaction> findByIntentId(String intentId);
}