package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IyzicoPaymentResultRequestDto {

    private String token;
    private String conversationId;

}
