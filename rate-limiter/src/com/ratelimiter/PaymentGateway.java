package com.ratelimiter;

public class PaymentGateway implements ExternalService {

    @Override
    public String call(String request) {
        return "PaymentGateway processed: " + request;
    }
}
