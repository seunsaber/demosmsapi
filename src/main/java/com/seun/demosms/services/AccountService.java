package com.seun.demosms.services;

import com.seun.demosms.models.Account;
import com.seun.demosms.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    public Optional<Account> findByUsername(String username){
        return repository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String password){

        Optional<Account> optionalAccount = findByUsername(username);

        if(optionalAccount.isEmpty())
            return false;

        return optionalAccount.get().getAuthId().equals(password);

    }
}
