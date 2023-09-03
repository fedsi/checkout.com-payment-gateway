package com.checkout.challenge.paymentgateway.common;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Encode sensitive data using Base64 scheme
 */
@Component
public class Encoder {

    public static String encodeData(String dataToEncode) {
        return Base64.getEncoder().encodeToString(dataToEncode.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeData(String dataToDecode) {
        return new String(Base64.getDecoder().decode(dataToDecode));
    }
}
