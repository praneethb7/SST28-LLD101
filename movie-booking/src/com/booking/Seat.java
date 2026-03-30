package com.booking;

public class Seat {

    private String seatId;
    private int row;
    private int col;
    private SeatType type;
    private SeatStatus status;

    public Seat(String seatId, int row, int col, SeatType type) {
        this.seatId = seatId;
        this.row = row;
        this.col = col;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getSeatId() { return seatId; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public SeatType getType() { return type; }
    public SeatStatus getStatus() { return status; }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return seatId + "(" + type + ")";
    }
}
