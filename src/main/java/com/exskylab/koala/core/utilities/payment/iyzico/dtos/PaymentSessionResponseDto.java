package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSessionResponseDto {

    private String status;

    private String token;

    private String paymentPageUrl;

    private String checkoutFormContent;

}
