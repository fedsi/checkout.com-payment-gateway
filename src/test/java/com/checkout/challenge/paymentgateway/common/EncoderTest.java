package com.checkout.challenge.paymentgateway.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncoderTest {

    @Test
    public void encodeData() {
        String expectedEncodedData = "RmVkZXJpY28gU2lsdmVyaQ==";
        String actualEncodedData = Encoder.encodeData("Federico Silveri");

        assertEquals(expectedEncodedData, actualEncodedData);
    }

    @Test
    public void decodeData() {
        String expectedDecodedData = "Federico Silveri";
        String actualDecodedData = Encoder.decodeData("RmVkZXJpY28gU2lsdmVyaQ==");

        assertEquals(expectedDecodedData, actualDecodedData);
    }
}