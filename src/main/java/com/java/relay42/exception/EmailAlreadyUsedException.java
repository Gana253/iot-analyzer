package com.java.relay42.exception;

import com.java.relay42.constants.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
