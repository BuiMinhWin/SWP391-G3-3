package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final DocumentRepository documentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final TransactionRepository transactionRepository;
    private final OrderMapper orderMapper;

    private static final int STATUS_CANCELLED = 0;
    private static final int STATUS_PENDING = 1;
    private static final int STATUS_PROCESSING = 2;
    private static final int STATUS_READY_FOR_PICKUP = 3;
    private static final int STATUS_IN_TRANSIT = 4;
    private static final int STATUS_DELIVERED = 5;


    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Received OrderDTO: {}", orderDTO);

        Account account = accountRepository.findById(orderDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));

        logger.info("Account found: {}", account);

        Order order = orderMapper.mapToOrder(orderDTO, account);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
            logger.debug("Order date was null, set to current time: {}", order.getOrderDate());
        }

        if (order.getStatus() < 0 || order.getStatus() > 1) {
            order.setStatus(STATUS_CANCELLED);
            logger.debug("Order status was 0", STATUS_CANCELLED);
        }

//        order.setPaymentStatus(0);

        logger.info("Creating Order with Account ID: {}", order.getAccount().getAccountId());

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created with ID: {}", savedOrder.getOrderId());

        return orderMapper.mapToOrderDTO(savedOrder);
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

    public OrderDTO updateOrderWhenCanceled(String orderId, OrderDTO orderDTO) {
        logger.info("Updating canceled Order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() == STATUS_CANCELLED) {

            if (orderDTO.getOrderDate() != null) {
                order.setOrderDate(orderDTO.getOrderDate());
                logger.debug("Updated orderDate to: {}", orderDTO.getOrderDate());
            }

            if (orderDTO.getShippedDate() != null) {
                order.setShippedDate(orderDTO.getShippedDate());
                logger.debug("Updated shippedDate to: {}", orderDTO.getShippedDate());
            }

            if (orderDTO.getOrigin() != null) {
                order.setOrigin(orderDTO.getOrigin());
                logger.debug("Updated origin to: {}", orderDTO.getOrigin());
            }

            if (orderDTO.getDestination() != null) {
                order.setDestination(orderDTO.getDestination());
                logger.debug("Updated destination to: {}", orderDTO.getDestination());
            }

            if (orderDTO.getFreight() != null) {
                order.setFreight(orderDTO.getFreight());
                logger.debug("Updated freight to: {}", orderDTO.getFreight());
            }

            if (orderDTO.getTotalPrice() != 0) {
                order.setTotalPrice(orderDTO.getTotalPrice());
                logger.debug("Updated totalPrice to: {}", orderDTO.getTotalPrice());
            }

            if (orderDTO.getAccountId() != null) {
                Account account = accountRepository.findById(orderDTO.getAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderDTO.getAccountId()));
                order.setAccount(account);
                logger.debug("Updated account to: {}", account.getAccountId());
            }

            if (orderDTO.getPostalCode() != null) {
                order.setPostalCode(orderDTO.getPostalCode());
                logger.debug("Updated postalCode to: {}", orderDTO.getPostalCode());
            }

            order.setStatus(STATUS_PROCESSING);
            Order updatedOrder = orderRepository.save(order);
            logger.info("Order with ID: {} has been updated and status set to {} (Processing).", orderId, STATUS_PROCESSING);

            return orderMapper.mapToOrderDTO(updatedOrder);
        } else {
            logger.warn("Attempted to update an order that is not canceled with ID: {}", orderId);
            throw new IllegalStateException("Order can only be updated if it is canceled.");
        }
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

    public void deleteOrder(String orderId) {
        logger.info("Deleting Order and related entities with ID: {}", orderId);

        // Fetch the order from the repository
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        // Delete documents associated with the order
        if (order.getDocuments() != null && !order.getDocuments().isEmpty()) {
            documentRepository.deleteAll(order.getDocuments());
            logger.info("Deleted {} documents associated with Order ID: {}", order.getDocuments().size(), orderId);
        }

        // Delete order details associated with the order
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            orderDetailRepository.deleteAll(order.getOrderDetails());
            logger.info("Deleted {} order details associated with Order ID: {}", order.getOrderDetails().size(), orderId);
        }

        // Delete transactions associated with the order
        if (order.getTransactions() != null && !order.getTransactions().isEmpty()) {
            transactionRepository.deleteAll(order.getTransactions());
            logger.info("Deleted {} transactions associated with Order ID: {}", order.getTransactions().size(), orderId);
        }

        // Finally, delete the order
        orderRepository.delete(order);
        logger.info("Order with ID: {} and its related entities have been deleted.", orderId);
    }


    public OrderDTO updateOrderStatus(String orderId, int newStatus) {
        logger.info("Updating status of Order with ID: {} to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (newStatus < 0 || newStatus > 5) { // Ensure valid status range
            newStatus = STATUS_PROCESSING; // Default to Processing
            logger.info("Invalid status provided. Status automatically set to Processing (2).");
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        switch (newStatus) {
            case STATUS_CANCELLED:
                logger.info("Order status updated to Canceled.");
                String vnpTxnRef = order.getVnpTxnRef();
                String accountId = order.getAccount().getAccountId();
                double refundAmount = order.getTotalPrice();
                logger.info("Refund of {} has been processed for Order ID: {}, vnpTxnRef: {} of Account ID: {}",
                        refundAmount, orderId, vnpTxnRef, accountId);
                break;
            case STATUS_PENDING:
                logger.info("Order status updated to Pending.");
                break;
            case STATUS_PROCESSING:
                logger.info("Order status updated to Processing.");
                break;
            case STATUS_READY_FOR_PICKUP:
                logger.info("Order status updated to Ready for Pickup.");
                break;
            case STATUS_IN_TRANSIT:
                logger.info("Order status updated to In Transit.");
                break;
            case STATUS_DELIVERED:
                logger.info("Order status updated to Delivered.");
                break;
            default:
                logger.warn("Order status updated to an unrecognized status: {}", newStatus);
                break;
        }

        return orderMapper.mapToOrderDTO(updatedOrder);
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


    public Optional<String> getVnpTxnRefByOrderId(String orderId) {
        return orderRepository.findById(orderId)
                .map(Order::getVnpTxnRef);
    }

    public void updateVnpTxnRef(String orderId, String vnpTxnRef) {
        log.debug("Updating vnpTxnRef for orderId: {} with vnpTxnRef: {}", orderId, vnpTxnRef);
        orderRepository.findById(orderId).ifPresentOrElse(order -> {
            log.debug("Order found: {}", order);
            order.setVnpTxnRef(vnpTxnRef); // Sửa lỗi ở đây: set vnpTxnRef đúng cách
            orderRepository.save(order);
            log.debug("vnpTxnRef updated and order saved: {}", order);
        }, () -> {
            log.warn("Order not found with orderId: {}", orderId);
            throw new OrderNotFoundException("Order not found with orderId: " + orderId);
        });
    }

}
