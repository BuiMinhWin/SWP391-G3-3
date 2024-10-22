package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByVnpTxnRef(String vnpTxnRef);
    List<Order> findByAccount_AccountId(String accountId);
    @Query("SELECT o FROM Order o WHERE LOWER(o.province) = LOWER(:province)")
    List<Order> findByProvinceIgnoreCase(@Param("province") String province);

}
