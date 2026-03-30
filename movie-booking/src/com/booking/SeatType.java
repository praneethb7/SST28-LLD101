package com.booking;

public enum SeatType {
    REGULAR(150),
    PREMIUM(250),
    VIP(400);

    private int price;

    SeatType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
