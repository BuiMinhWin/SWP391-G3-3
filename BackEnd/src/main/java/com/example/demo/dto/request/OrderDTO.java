package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String orderId;
    private String accountId;
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private String origin;
    private String destination;
    private String freight;
    private String receiverName;
    private String senderName;
    private String receiverPhone;
    private String senderPhone;
    private String senderNote;
    private String receiverNote;
    private String orderNote;
    private String province;
    private int totalPrice;
    private int status;
    private int paymentStatus;
    private String sale;
    private String deliver;

//    private double originLatitude;
//    private double originLongitude;
//    private double destinationLatitude;
//    private double destinationLongitude;
    private double distance;
    private String vnpTxnRef;
    private List<Integer> serviceIds;

    private float totalWeight;
    private int totalQuantity;
    private String totalDiscount;

}
