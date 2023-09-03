package com.checkout.challenge.paymentgateway.controller;

import com.checkout.challenge.paymentgateway.dto.merchant.PaymentRequestDTO;
import com.checkout.challenge.paymentgateway.dto.merchant.PaymentResponseDTO;
import com.checkout.challenge.paymentgateway.entity.Payment;
import com.checkout.challenge.paymentgateway.service.PaymentGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(path = "payment")
public class PaymentGatewayController {

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Return a previously made {@link Payment} by paymentId
     *
     * @return Return a {@link Payment} object with masked card details
     */
    @GetMapping(value = "/{paymentId}")
    public ResponseEntity retrievePayment(@PathVariable String paymentId) {
        log.info("Received a new request to retrieve payment with id: {}", paymentId);

        Optional<Payment> payment = paymentGatewayService.retrievePayment(UUID.fromString(paymentId));
        if(payment.isPresent()) {
            PaymentResponseDTO responseDTO = convertToResponseDto(payment.get());
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Make a new {@link Payment}
     *
     * @return The new {@link Payment} with masked card details
     */
    @PostMapping
    ResponseEntity makePayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        log.info("Received a new makePayment request from merchant: {}", paymentRequestDTO.getMerchantId());
        Payment payment = convertToEntity(paymentRequestDTO);
        log.info("Payment request: {}", payment);

        payment = paymentGatewayService.makePayment(payment);
        PaymentResponseDTO responseDTO = convertToResponseDto(payment);

        return new ResponseEntity(responseDTO, HttpStatus.CREATED);
    }

    private Payment convertToEntity(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = modelMapper.map(paymentRequestDTO, Payment.class);
        payment.setPaymentId(UUID.randomUUID());
        return payment;
    }

    private PaymentResponseDTO convertToResponseDto(Payment payment) {
        PaymentResponseDTO paymentResponseDTO = modelMapper.map(payment, PaymentResponseDTO.class);
        paymentResponseDTO.setCardHolder(payment.getDecodedCardHolder());
        paymentResponseDTO.setCardNumber(maskCardNumber(payment.getDecodedCardNumber()));
        paymentResponseDTO.setExpiryDate("xx/xx");
        paymentResponseDTO.setCvv("xxx");
        return paymentResponseDTO;
    }

    /**
     * Mask the card number.
     *
     * @param cardNumber The card number in plain format
     * @return The masked card number
     */
    public static String maskCardNumber(String cardNumber) {

        StringBuilder maskedCardNumber = new StringBuilder();
        maskedCardNumber.append(cardNumber, 0, 4);
        maskedCardNumber.append("-xxxx-xxxx-xx");
        maskedCardNumber.append(cardNumber, cardNumber.length() -2, cardNumber.length());

        return maskedCardNumber.toString();
    }

}
