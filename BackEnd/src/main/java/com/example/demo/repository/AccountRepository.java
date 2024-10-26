package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findOneByUserNameAndPassword(String userName, String password);
    Account findByUserName(String userName);
    Account findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
