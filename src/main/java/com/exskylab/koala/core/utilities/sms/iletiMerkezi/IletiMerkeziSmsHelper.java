package com.exskylab.koala.core.utilities.sms.iletiMerkezi;

import com.exskylab.koala.core.constants.SmsMessages;
import com.exskylab.koala.core.exceptions.SmsSendException;
import com.exskylab.koala.core.properties.IletiMerkeziProperties;
import com.exskylab.koala.core.utilities.sms.SmsService;
import com.exskylab.koala.core.utilities.sms.iletiMerkezi.request.*;
import com.exskylab.koala.core.utilities.sms.iletiMerkezi.response.IletiMerkeziSendSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class IletiMerkeziSmsHelper implements SmsService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(IletiMerkeziSmsHelper.class);
    private final IletiMerkeziProperties properties;

    public IletiMerkeziSmsHelper(RestTemplate restTemplate, IletiMerkeziProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public String sendSms(String[] phoneNumbers, String messageText, boolean isIys) {
        logger.info("Sending sms to: {}", phoneNumbers);
        String apiUrl = "https://api.iletimerkezi.com/v1/send-sms/json";
        Authentication authentication = new Authentication(properties.getApiKey(), properties.getApiHash());
        Receipents receipents = new Receipents(phoneNumbers);
        Message message = new Message(messageText, receipents);

        Order order = new Order(
                properties.getSender(),
                isIys ? "1" : "0",
                isIys ? "BIREYSEL" : "",
                message
        );

        Request requestBody = new Request(authentication, order);
        IletiMerkeziSmsRequest smsRequest = new IletiMerkeziSmsRequest(requestBody);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<IletiMerkeziSmsRequest> entity = new HttpEntity<>(smsRequest, headers);

        try{
            IletiMerkeziSendSmsResponse response = restTemplate.postForObject(apiUrl, entity, IletiMerkeziSendSmsResponse.class);

           if (response != null && response.getStatus().getCode().equals("200")){
               logger.info("Sms sent successfully to: {} Order ID: {}", phoneNumbers, response.getOrder().getId());
               return response.getOrder().getId();
           }else {
                logger.error("Failed to send sms to: {}. Response: {}", phoneNumbers, response != null ? response.getStatus().getMessage() : "No response");
               throw new SmsSendException(SmsMessages.SMS_SEND_FAILED);
           }
        }catch (Exception e){
            logger.error("Error occurred while sending sms to: {}. Error: {}", phoneNumbers, e.getMessage());
            throw new SmsSendException(SmsMessages.SMS_SEND_FAILED);
        }
    }




}
