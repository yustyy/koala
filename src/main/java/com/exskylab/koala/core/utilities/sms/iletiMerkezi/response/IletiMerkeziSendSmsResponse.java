package com.exskylab.koala.core.utilities.sms.iletiMerkezi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IletiMerkeziSendSmsResponse {

    private IletiMerkeziSendSmsStatus status;
    private IletiMerkeziSendSmsOrder order;

}