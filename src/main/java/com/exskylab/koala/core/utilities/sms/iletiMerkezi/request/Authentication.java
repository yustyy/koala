package com.exskylab.koala.core.utilities.sms.iletiMerkezi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {

    private String key;
    private String hash;

}
