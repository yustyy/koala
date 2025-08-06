package com.exskylab.koala.core.utilities.exceptions;

import com.exskylab.koala.core.exceptions.KoalaException;

public class TokenCannotBeNullException extends KoalaException {
    public TokenCannotBeNullException(String message) {
        super(message);
    }
}
