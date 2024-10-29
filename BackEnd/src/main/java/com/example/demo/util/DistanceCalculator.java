package com.example.demo.util;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;

import java.util.Collection;

public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6378.0;

//    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return EARTH_RADIUS_KM * c;
//    }

    public static int calculateTotalPrice(double distance, int ratePerKm, OrderDetail orderDetail) {
        int distancePrice;

        if (distance <= 10) {
            distancePrice = (int) (distance * ratePerKm);
        } else if (distance <= 50) {
            distancePrice = (int) (distance * ratePerKm * 0.2);
        } else if (distance <= 100) {
            distancePrice = (int) (distance * ratePerKm * 1.2);
        } else {
            distancePrice = (int) (distance * ratePerKm * 1.5);
        }

        double servicesTotal = 0;
        if (orderDetail.getKoiServices() != null && !orderDetail.getKoiServices().isEmpty()) {
            for (Services service : orderDetail.getKoiServices()) {
                servicesTotal += service.getPrice();
            }
        }

        int totalPrice = distancePrice + (int) servicesTotal;

        if (orderDetail.getDiscount() != null && !orderDetail.getDiscount().isEmpty()) {
            double discount = Double.parseDouble(orderDetail.getDiscount());
            totalPrice *= (1 - discount / 100);
        }

        return totalPrice;
    }
}
