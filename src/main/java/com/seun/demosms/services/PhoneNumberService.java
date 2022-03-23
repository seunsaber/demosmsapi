package com.seun.demosms.services;

import com.seun.demosms.models.PhoneNumber;
import com.seun.demosms.repositories.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneNumberService {
    @Autowired
    private PhoneNumberRepository repository;

    public PhoneNumber findByNumber(String phoneNumber){
        Optional<PhoneNumber> optionalPhoneNumber = repository.findByNumber(phoneNumber);

        if (optionalPhoneNumber.isEmpty()){
            //TODO: return 404
        }

        return optionalPhoneNumber.get();

    }

}
