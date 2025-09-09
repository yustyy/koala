package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IyzicoPaymentResultResponseDto {
    private String status;
    private String paymentId;
    private String paymentTransactionId;
    private String conversationId;
}
