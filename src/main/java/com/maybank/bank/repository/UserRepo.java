package com.maybank.bank.repository;

import com.maybank.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    // TODO
}
