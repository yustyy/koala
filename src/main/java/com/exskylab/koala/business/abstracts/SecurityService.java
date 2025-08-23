package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.User;

public interface SecurityService {
    User getAuthenticatedUserFromContext();

    User getAuthenticatedUserFromDatabase();

    User getSystemUser();
}
