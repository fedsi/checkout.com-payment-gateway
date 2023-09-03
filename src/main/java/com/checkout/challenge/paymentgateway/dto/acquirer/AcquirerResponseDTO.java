package com.checkout.challenge.paymentgateway.dto.acquirer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcquirerResponseDTO {

    private String authorisationCode;

    private String status;

    private String description;

}
