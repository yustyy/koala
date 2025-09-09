package com.exskylab.koala.core.consumers;

import com.exskylab.koala.core.configs.RabbitMQConfig;
import com.exskylab.koala.core.producers.user.UserUpdatedEventDto;
import com.exskylab.koala.core.utilities.payment.iyzico.IyzicoPaymentService;
import com.exskylab.koala.core.utilities.payment.iyzico.dtos.SubMerchantUpdateRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class IyzicoSynchronizationConsumer {

    private final IyzicoPaymentService iyzicoPaymentService;
    private static final Logger logger = LoggerFactory.getLogger(IyzicoSynchronizationConsumer.class);


    public IyzicoSynchronizationConsumer(IyzicoPaymentService iyzicoPaymentService) {
        this.iyzicoPaymentService = iyzicoPaymentService;
    }



    @RabbitListener(queues = RabbitMQConfig.USER_PROFILE_UPDATED_FOR_SYNC_QUEUE,
            containerFactory = "iyzicoListenerContainerFactory")
    public void handleUserProfileUpdatedForSync(UserUpdatedEventDto userDto){
        logger.info("Handling user profile updated for sync event for id: {}", userDto.getId());
        if (userDto.getIyzicoSubMerchantKey() == null || userDto.getIyzicoSubMerchantKey().isEmpty()) {
            logger.warn("User with id: {} does not have an Iyzico sub-merchant key. Skipping synchronization with iyzico.", userDto.getId());
            return;
        }


        SubMerchantUpdateRequestDto subMerchantUpdateRequestDto = new SubMerchantUpdateRequestDto();
        subMerchantUpdateRequestDto.setSubMerchantKey(userDto.getIyzicoSubMerchantKey());
        subMerchantUpdateRequestDto.setName(userDto.getFirstName() + " " + userDto.getLastName());
        subMerchantUpdateRequestDto.setEmail(userDto.getEmail());
        subMerchantUpdateRequestDto.setGsmNumber(userDto.getGsmNumber());
        subMerchantUpdateRequestDto.setAddress(userDto.getAddress());
        subMerchantUpdateRequestDto.setIdentityNumber(userDto.getIdentityNumber());
        subMerchantUpdateRequestDto.setContactName(userDto.getFirstName());
        subMerchantUpdateRequestDto.setContactSurname(userDto.getLastName());
        subMerchantUpdateRequestDto.setIdentityNumber(userDto.getIdentityNumber());
        subMerchantUpdateRequestDto.setIban(userDto.getIban());
        try {
            iyzicoPaymentService.updateSubMerchant(subMerchantUpdateRequestDto);
        }catch (Exception e){
            logger.error("Failed to update Iyzico sub-merchant for user id: {}. Error: {}", userDto.getId(), e.getMessage());
            throw e;
        }


    }
}
