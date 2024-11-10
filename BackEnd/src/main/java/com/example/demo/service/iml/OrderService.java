package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Services;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.*;
import com.example.demo.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final int RATE_PER_KM = 14000;

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final ServicesRepository servicesRepository;

    @Value("${manager.email}")
    private String managerEmail;

    private static final int STATUS_CANCELLED = 9000;
    private static final int STATUS_WAITING_APPROVAL = 0;
    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_ASSIGNED_TO_CARRIER = 2;
    private static final int STATUS_PICKED_UP = 3;
    private static final int STATUS_IN_TRANSIT = 4;
    private static final int STATUS_DELIVERED = 5;
    private static final int STATUS_OTHER = 6;
    private static final int STATUS_SUCCESS = 7;

    private static final int ADDITIONAL_FEE = 500000;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Received OrderDTO: {}", orderDTO);

        Account account = accountRepository.findById(orderDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));

        logger.info("Account found: {}", account);

        if (account.getStatus() == 0) {
            throw new IllegalArgumentException("Account is inactive. Order creation is not allowed.");
        }

        Order order = orderMapper.mapToOrder(orderDTO, account);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() < 0 || order.getStatus() > 1) {
            order.setStatus(STATUS_WAITING_APPROVAL);
            logger.debug("Order status was invalid, set to: {}", STATUS_WAITING_APPROVAL);
        }

        order.setPaymentStatus(0);

        if (orderDTO.getServiceIds() != null && !orderDTO.getServiceIds().isEmpty()) {
            String serviceIdsString = orderDTO.getServiceIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            order.setServiceIds(serviceIdsString);
        }

        setServicePrices(orderDTO, order);

        if ("Narita".contains(order.getOrigin()) ||
                "Haneda".contains(order.getOrigin()) ||
                "Kansai".contains(order.getOrigin())) {

            int updatedTotalPrice = order.getTotalPrice() + ADDITIONAL_FEE;
            order.setTotalPrice(updatedTotalPrice);
            logger.info("Additional fee of {} applied due to origin. Updated total price: {}", ADDITIONAL_FEE, updatedTotalPrice);
        }

        int totalQuantity = 0;
        float totalWeight = 0;

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail detail : orderDetails) {
            totalQuantity += detail.getQuantity();
            totalWeight += detail.getWeight();
            logger.debug("Added order detail: Quantity: {}, Weight: {}", detail.getQuantity(), detail.getWeight());
        }

        order.setOrderDetails(orderDetails);
        order.setTotalQuantity(totalQuantity);
        order.setTotalWeight(totalWeight);

        int amount = (int) (order.getTotalPrice() + order.getTotalQuantity() * 1000 + order.getTotalWeight() * 1000);
        order.setTotalPrice(amount);

        String discountCode = order.getDiscount();
        if ("10PKL".equals(discountCode) && order.getTotalWeight() >= 5) {
            double discountAmount = order.getTotalPrice() * 0.10;
            double newTotalPrice = order.getTotalPrice() - discountAmount;
            order.setTotalPrice((int) newTotalPrice);
            order.setDiscount("10PKL");
            logger.info("Applied discount code '10PKL'. New total price: {}", order.getTotalPrice());
        }


        logger.info("Creating Order with Account ID: {}", order.getAccount().getAccountId());

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created with ID: {}", savedOrder.getOrderId());

        return orderMapper.mapToOrderDTO(savedOrder);
    }

    private void setServicePrices(OrderDTO orderDTO, Order order) {
        int totalServicePrice = 0;
        Set<Services> servicesSet = new HashSet<>();

        Set<Integer> requestedServiceIds = new HashSet<>(orderDTO.getServiceIds());

        for (Integer serviceId : requestedServiceIds) {
            Services service = servicesRepository.findById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));

            servicesSet.add(service);
            totalServicePrice += service.getPrice();
        }
        if(order.getServiceIds().contains("3")){
            order.setPaymentStatus(2);
        }

        order.setServices(servicesSet);

        int distancePrice = DistanceCalculator.calculateTotalPrice(order.getDistance(), RATE_PER_KM);
        order.setTotalPrice(totalServicePrice + distancePrice);

        logger.debug("Total service price: {}, Total distance price: {}", totalServicePrice, distancePrice);
    }


    public OrderDTO cancelOrder(String orderId) {
        logger.info("Cancelling Order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() != STATUS_CANCELLED) {
            order.setStatus(STATUS_CANCELLED);
            order.setOrderDate(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            logger.info("Order with ID: {} has been canceled.", orderId);
            return orderMapper.mapToOrderDTO(savedOrder);
        } else {
            logger.warn("Attempted to cancel an already canceled order with ID: {}", orderId);
            throw new IllegalStateException("Order is already canceled.");
        }
    }

    public OrderDTO updateOrder(String orderId, OrderDTO orderDTO) {
        logger.info("Updating Order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() == 1) {
            logger.info("Updating 'sale' information for Pending Order with ID: {}", orderId);
            Optional.ofNullable(orderDTO.getSale()).ifPresent(order::setSale);
        }
        if (order.getStatus() == 2) {
            logger.info("Updating 'deliver' information for Processing Order with ID: {}", orderId);
            Optional.ofNullable(orderDTO.getDeliver()).ifPresent(deliver -> {
                order.setDeliver(deliver);

                sendEmailToDelivery(deliver, order);
            });
        }

        updateOrderFields(order, orderDTO);
        Order updatedOrder = orderRepository.save(order);

        logger.info("Order with ID: {} has been updated.", orderId);
        return orderMapper.mapToOrderDTO(updatedOrder);
    }

    private void updateOrderFields(Order order, OrderDTO orderDTO) {
        Optional.ofNullable(orderDTO.getOrderDate()).ifPresent(order::setOrderDate);
        Optional.ofNullable(orderDTO.getShippedDate()).ifPresent(order::setShippedDate);
        Optional.ofNullable(orderDTO.getOrigin()).ifPresent(order::setOrigin);
        Optional.ofNullable(orderDTO.getDestination()).ifPresent(order::setDestination);
        Optional.ofNullable(orderDTO.getFreight()).ifPresent(order::setFreight);
        if (orderDTO.getTotalPrice() != 0) {
            order.setTotalPrice(orderDTO.getTotalPrice());
        }
        if (orderDTO.getPaymentStatus() != 0) {
            order.setPaymentStatus(orderDTO.getPaymentStatus());
        }

        Optional.ofNullable(orderDTO.getAccountId()).ifPresent(accountId -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));
            order.setAccount(account);
        });
    }


    public List<OrderDTO> getAllOrders() {
        logger.info("Fetching all orders.");
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());
        logger.info("Fetched {} orders.", orderDTOs.size());
        return orderDTOs;
    }

    public OrderDTO getOrderById(String orderId) {
        logger.info("Fetching Order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        return orderMapper.mapToOrderDTO(order);
    }

    public List<OrderDTO> getOrdersByAccountId(String accountId) {
        logger.info("Fetching orders for accountId: {}", accountId);

        List<Order> orders = orderRepository.findByAccount_AccountId(accountId);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for accountId: " + accountId);
        }

        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());

        logger.info("Fetched {} orders for accountId: {}", orderDTOs.size(), accountId);

        return orderDTOs;
    }



    public void deleteOrder(String orderId) {
        logger.info("Deleting Order and related entities with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        orderRepository.delete(order);
        logger.info("Order with ID: {} and its related entities have been deleted.", orderId);
    }



    public OrderDTO updateOrderStatus(String orderId, int newStatus) {
        logger.info("Updating status of Order with ID: {} to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        newStatus = validateStatus(newStatus);

        order.setStatus(newStatus);

        if (newStatus == STATUS_DELIVERED) {
            order.setShippedDate(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);

        handleStatusSpecificLogic(order, newStatus);

        return orderMapper.mapToOrderDTO(updatedOrder);
    }

    private int validateStatus(int newStatus) {
        if (newStatus < STATUS_WAITING_APPROVAL || newStatus > STATUS_SUCCESS) {
            logger.info("Invalid status provided. Defaulting to Processing.");
            return STATUS_ASSIGNED_TO_CARRIER;
        }
        return newStatus;
    }

    private void handleStatusSpecificLogic(Order order, int status) {
        switch (status) {
            case STATUS_WAITING_APPROVAL:
                processRefund(order);
                break;
            case STATUS_APPROVED:
                logger.info("Order status updated to Pending.");
                sendEmailToCustomer(order, status);
                break;
            case STATUS_ASSIGNED_TO_CARRIER:
                logger.info("Order status updated to Processing.");
                break;
            case STATUS_PICKED_UP:
                logger.info("Order status updated to Ready for Pickup.");
                break;
            case STATUS_IN_TRANSIT:
                logger.info("Order status updated to In Transit.");
                break;
            case STATUS_DELIVERED:
                logger.info("Order status updated to Delivered.");
                break;
            case STATUS_OTHER:
                logger.info("There was a problem with the order.");
                sendEmailToCustomer(order, status);
                sendEmailToManager(order);
            case STATUS_SUCCESS:
                logger.info("Order success.");
                break;
            default:
                logger.warn("Order status updated to an unrecognized status: {}", status);
                break;
        }
    }

    private void processRefund(Order order) {
        String vnpTxnRef = order.getVnpTxnRef();
        String accountId = order.getAccount().getAccountId();
        double refundAmount = order.getTotalPrice();
        logger.info("Refund of {} has been processed for Order ID: {}, vnpTxnRef: {} of Account ID: {}",
                refundAmount, order.getOrderId(), vnpTxnRef, accountId);
    }

    public OrderDTO getOrderByVnpTxnRef(String vnpTxnRef) {
        return orderRepository.findByVnpTxnRef(vnpTxnRef)
                .map(orderMapper::convertToDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with vnpTxnRef: " + vnpTxnRef));
    }


    public OrderDTO getOrderByIdV2(String vnpTxnRef) {
        return orderRepository.findById(vnpTxnRef)
                .map(order -> orderMapper.convertToDTO(order))
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + vnpTxnRef));
    }

    public void updateVnpTxnRef(String orderId, String vnpTxnRef) {
        log.debug("Updating vnpTxnRef for orderId: {} with vnpTxnRef: {}", orderId, vnpTxnRef);
        orderRepository.findById(orderId).ifPresentOrElse(order -> {
            log.debug("Order found: {}", order);
            order.setVnpTxnRef(vnpTxnRef);
            orderRepository.save(order);
            log.debug("vnpTxnRef updated and order saved: {}", order);
        }, () -> {
            log.warn("Order not found with orderId: {}", orderId);
            throw new OrderNotFoundException("Order not found with orderId: " + orderId);
        });
    }

    public void updatePaymentStatus(String orderId, int paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for orderId: " + orderId));

        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }

    public Order findOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for orderId: " + orderId));
    }

    private void sendEmailToCustomer(Order order, int status) {
        Account account = order.getAccount();
        String recipientEmail = account.getEmail();
        String subject = "Thông báo về đơn hàng của bạn";
        String message;

        if (status == STATUS_OTHER) {
            message = String.format(
                    "Kính gửi %s,\n\nĐơn hàng với mã %s của bạn hiện đang gặp vấn đề. Xin vui lòng kiểm tra đơn hàng để biết thêm chi tiết.\n\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi!",
                    account.getUserName(), order.getOrderId());
        } else {
            message = String.format(
                    "Kính gửi %s,\n\nĐơn hàng với mã %s của bạn đã được phê duyệt. Xin vui lòng kiểm tra đơn hàng để biết thêm chi tiết.\n\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi!",
                    account.getUserName(), order.getOrderId());
        }

        sendEmail(recipientEmail, subject, message, "Thông báo Cho Khách Hàng");
    }

    private void sendEmailToDelivery(String deliver, Order order) {
        Account deliveryAccount = accountRepository.findById(deliver)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản giao hàng với ID: " + deliver));

        String subject = "Đơn hàng mới được giao cho bạn";
        String body = String.format("Kính gửi %s,\n\nBạn đã được giao một đơn hàng mới.\nMã đơn hàng: %s\nNgười nhận: %s\nĐịa chỉ giao hàng: %s\n\nTrân trọng,\nNhóm phát triển",
                deliveryAccount.getUserName(), order.getOrderId(), order.getReceiverName(), order.getDestination());

        sendEmail(deliveryAccount.getEmail(), subject, body, "Thông Báo Cho Tài Xế");
    }


    private void sendEmailToManager(Order order) {
        String subject = "Thông Báo Vấn Đề Đơn Hàng";
        String message = String.format(
                "Kính gửi Quản lý,\n\nĐã xảy ra vấn đề với đơn hàng có mã: %s. Vui lòng kiểm tra chi tiết đơn hàng và thực hiện các hành động cần thiết.\n\nXin cảm ơn.",
                order.getOrderId());

        sendEmail(managerEmail, subject, message, "Thông Báo Cho Quản Lý");
    }

    private void sendEmail(String recipientEmail, String subject, String message, String logContext) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            logger.info("Email sent to {}: {}", recipientEmail, logContext);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}: {}", recipientEmail, logContext, e.getMessage());
        }
    }

    public List<OrderDTO> getOrderByDeliver(String deliver) {
        logger.info("Fetching orders for deliver: {}", deliver);

        List<Order> orders = orderRepository.findByDeliver(deliver);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for deliver: " + deliver);
        }

        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());

        logger.info("Fetched {} orders for deliver: {}", orderDTOs.size(), deliver);

        return orderDTOs;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("orderId cannot be null or empty");
        }
        return orderDetailRepository.findByOrder_OrderId(orderId);
    }
}
