package com.onlineshop.hishop.exception;

import org.apache.shiro.authc.AccountException;

public class UserLockException extends AccountException {


    public UserLockException() {
        super();
    }

    public UserLockException(String message) {
        super(message);
    }

    public UserLockException(Throwable cause) {
        super(cause);
    }

    public UserLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
