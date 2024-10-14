package com.example.demo.service.iml;

import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final OrderMapper orderMapper;

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

        if (order.getStatus() == 0) {
            order.setStatus(1);
            logger.debug("Order status was 0, updated to 1 (Processing)");
        }

        logger.info("Creating Order with Account ID: {}", order.getAccount().getAccountId());

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created with ID: {}", savedOrder.getOrderId());

        return orderMapper.mapToOrderDTO(savedOrder);
    }

    public OrderDTO cancelOrder(String orderId) {
        logger.info("Cancelling Order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() != 0) {
            order.setStatus(0);
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

        if (order.getStatus() == 0) {

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

            order.setStatus(1);
            Order updatedOrder = orderRepository.save(order);
            logger.info("Order with ID: {} has been updated and status set to 1 (Processing).", orderId);

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

    public OrderDTO updateOrderStatus(String orderId, int newStatus) {
        logger.info("Updating status of Order with ID: {} to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        // Automatically set status to 1 (Processing) if newStatus is not provided or specific
        if (newStatus < 0 || newStatus > 3) {
            newStatus = 1; // Default to Processing
            logger.info("Invalid status provided. Status automatically set to Processing (1).");
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        switch (newStatus) {
            case 0:
                logger.info("Order status updated to Canceled.");
                break;
            case 1:
                logger.info("Order status updated to Processing.");
                break;
            case 2:
                logger.info("Order status updated to Shipped.");
                break;
            case 3:
                logger.info("Order status updated to Delivered.");
                break;
            default:
                logger.warn("Order status updated to an unrecognized status: {}", newStatus);
                break;
        }

        return orderMapper.mapToOrderDTO(updatedOrder);
    }

    public OrderDTO getOrderByIdV2(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> orderMapper.convertToDTO(order))
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }



    public void updateVnpTxnRef(String orderId, String vnpTxnRef) {
        log.debug("Updating vnpTxnRef for orderId: {} with vnpTxnRef: {}", orderId, vnpTxnRef);
        orderRepository.findByOrderId(orderId).ifPresentOrElse(order -> {
            log.debug("Order found: {}", order);
            order.setVnpTxnRef(vnpTxnRef);
            orderRepository.save(order);
            log.debug("vnpTxnRef updated and order saved: {}", order);
        }, () -> {
            log.warn("Order not found with orderId: {}", orderId);
        });
    }

}
