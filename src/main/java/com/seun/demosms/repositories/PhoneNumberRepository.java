package com.seun.demosms.repositories;

import com.seun.demosms.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<Integer, PhoneNumber> {
}
