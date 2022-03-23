package com.seun.demosms.repositories;

import com.seun.demosms.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Integer, Account> {
}
