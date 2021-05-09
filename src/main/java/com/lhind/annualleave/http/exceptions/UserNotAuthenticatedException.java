package com.lhind.annualleave.http.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException() {
        this("User is not authenticated to perform this action! Please login again!");
    }

    public UserNotAuthenticatedException(String message) {
        super(message);
    }

}
