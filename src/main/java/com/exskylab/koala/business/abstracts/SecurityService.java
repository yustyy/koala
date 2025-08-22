package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.User;

public interface SecurityService {
    User getAuthenticatedUser();

    User getSystemUser();
}
