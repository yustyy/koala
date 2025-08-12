package com.exskylab.koala.core.utilities.sms;

public interface SmsService {

    String sendSms(String[] phoneNumber, String messageText, boolean isIys);



}
