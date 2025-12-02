package com.swafy.pricing.dto;

public class PriceBreakdownDto {
    private double baseFare;
    private double distanceFare;
    private double total;

    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
    public double getDistanceFare() { return distanceFare; }
    public void setDistanceFare(double distanceFare) { this.distanceFare = distanceFare; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
