package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentSessionRequestDto {

    private String conversationId; //jobAssignmentID

    private double price;

    private double paidPrice;

    private String currency = "TRY";

    private String basketId; //jobAssignmentID

    private String paymentGroup = "PRODUCT";

    private String callbackUrl; //our frontend url

    private IyzicoBuyerDto buyer;

    private IyzicoAddressDto shippingAddress;

    private List<IyzicoBasketItemDto> basketItems;

    private IyzicoAddressDto billingAddress;


}
