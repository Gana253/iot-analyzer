package com.java.relay42.exception;

import com.java.relay42.constants.ErrorConstants;

public class InvalidPasswordException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Incorrect Password!", "userManagement", "passwordincorrect");
    }
}
