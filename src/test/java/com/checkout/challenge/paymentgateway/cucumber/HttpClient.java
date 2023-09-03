package com.checkout.challenge.paymentgateway.cucumber;

import com.checkout.challenge.paymentgateway.dto.merchant.PaymentRequestDTO;
import com.checkout.challenge.paymentgateway.dto.merchant.PaymentResponseDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class HttpClient {

    private static final String RETRIEVE_PAYMENT_ENDPOINT = "http://localhost:8080/api/payment/95b53506-b52b-4f67-8765-5b3b16c7f0e1";
    private static final String MAKE_PAYMENT_ENDPOINT = "http://localhost:8080/api/payment";

    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<PaymentResponseDTO> post(final PaymentRequestDTO paymentRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        setBasicAuth(headers);
        HttpEntity<PaymentRequestDTO> entity = new HttpEntity<PaymentRequestDTO>(paymentRequestDTO, headers);

        return  restTemplate.exchange(MAKE_PAYMENT_ENDPOINT, HttpMethod.POST, entity, PaymentResponseDTO.class);
    }

    public ResponseEntity<PaymentResponseDTO> retrievePaymentByPaymentId() {
        HttpHeaders headers = new HttpHeaders();
        setBasicAuth(headers);
        HttpEntity<PaymentResponseDTO> entity = new HttpEntity<PaymentResponseDTO>(null, headers);
        return  restTemplate.exchange(RETRIEVE_PAYMENT_ENDPOINT, HttpMethod.GET, entity, PaymentResponseDTO.class);
    }

    private void setBasicAuth(HttpHeaders headers) {
        headers.setBasicAuth(USER,PASSWORD);
    }

}
