package com.checkout.challenge.paymentgateway.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestApiError {

    @JsonIgnore
    private HttpStatus status;
    private String type;
    private String message;
    private List<String> errors;

}
