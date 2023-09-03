package com.checkout.challenge.paymentgateway.repository;

import com.checkout.challenge.paymentgateway.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>  {

    @Query("SELECT p from Payment p where p.requestId = :request_id")
    Optional<Payment> findByRequestId(@Param("request_id") String requestId);
}
