package com.seun.demosms.tests;

import com.seun.demosms.models.Account;
import com.seun.demosms.repositories.AccountRepository;
import com.seun.demosms.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import javax.validation.constraints.AssertTrue;
import java.util.Optional;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    @BeforeEach
    private void init(){
        Account account = new Account();
        account.setUsername("gooduser");
        account.setAuthId("mypassword");
        account.setId(1000001);

        lenient().when(repository.findByUsername("gooduser")).thenReturn(Optional.of(account));
    }

    @Test
    void shouldReturnUserWhenUsernameIsCorrect(){
        String username = "gooduser";

        Optional<Account> accountOptional = service.findByUsername(username);

        Assertions.assertTrue(accountOptional.isPresent());
    }

    @Test
    void shouldNotReturnUserWhenUsernameIsWrong(){
        String username = "baduser";

        Optional<Account> accountOptional = service.findByUsername(username);

        Assertions.assertFalse(accountOptional.isPresent());
    }

    @Test
    void shouldPassAuthenticationWithCorrectCredentials(){
        String username = "gooduser";
        String password = "mypassword";

        boolean isAuthenticated = service.authenticateUser(username, password);

        Assertions.assertTrue(isAuthenticated);
    }

    @Test
    void shouldFailAuthenticationWithCorrectUsernameWrongPassword(){
        String username = "gooduser";
        String password = "wrongpassword";

        boolean isAuthenticated = service.authenticateUser(username, password);

        Assertions.assertFalse(isAuthenticated);
    }
    @Test
    void shouldFailAuthenticationWithWrongUsernameWrongPassword(){
        String username = "baduser";
        String password = "wrongpassword";

        boolean isAuthenticated = service.authenticateUser(username, password);

        Assertions.assertFalse(isAuthenticated);
    }
}
