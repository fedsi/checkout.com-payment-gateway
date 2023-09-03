package com.checkout.challenge.paymentgateway.acquiringbank;

import com.checkout.challenge.paymentgateway.dto.acquirer.AcquirerRequestDTO;
import com.checkout.challenge.paymentgateway.dto.acquirer.AcquirerResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class AcquiringBankMock {

    private static final String AUTHORISE_PAYMENT = "100.00";
    private static final String DECLINE_PAYMENT = "200.00";


    public AcquirerResponseDTO authorisePayment(AcquirerRequestDTO transaction) {
        AcquirerResponseDTO response = new AcquirerResponseDTO();
        String amount = transaction.getAmount().toString();
        switch (amount) {
            case AUTHORISE_PAYMENT:
                Random ran = new Random();
                StringBuilder authCode = new StringBuilder();
                authCode.append("AUTH");
                authCode.append(ran.nextInt(9999));

                response.setStatus("AUTHORISED");
                response.setAuthorisationCode(authCode.toString());
                break;
            case DECLINE_PAYMENT:
                response.setStatus("DECLINED");
                response.setDescription("Insufficient funds");
                break;
            default:
                response.setStatus("DECLINED");
                response.setDescription("Declined transaction");
                break;
        }
        return response;
    }
}
