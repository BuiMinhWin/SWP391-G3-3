package com.example.demo.util;


public class DistanceCalculator {

    public static int calculateTotalPrice(double distance, int ratePerKm) {
        int distancePrice;

        if (distance <= 10) {
            distancePrice = (int) (distance * ratePerKm);
        } else if (distance <= 50) {
            distancePrice = (int) ((distance * ratePerKm * 0.2));
        } else if (distance <= 100) {
            distancePrice = (int) ((distance * ratePerKm * 1.2) /10);
        }else if (distance <= 500) {
            distancePrice = (int) ((distance * ratePerKm * 1.4) /10);
        } else {
            distancePrice = (int) ((distance * ratePerKm * 1.7) /10);
        }
        return distancePrice;
    }
}
