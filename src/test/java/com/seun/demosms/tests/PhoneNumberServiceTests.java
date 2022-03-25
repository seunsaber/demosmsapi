package com.seun.demosms.tests;

import com.seun.demosms.dtos.SmsRequestDTO;
import com.seun.demosms.exceptions.BadRequestException;
import com.seun.demosms.exceptions.ResourceNotFoundException;
import com.seun.demosms.models.PhoneNumber;
import com.seun.demosms.repositories.PhoneNumberRepository;
import com.seun.demosms.services.CacheService;
import com.seun.demosms.services.PhoneNumberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberServiceTests {

    @Mock
    private PhoneNumberRepository repository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private PhoneNumberService phoneNumberService;

    @BeforeEach
    private void init(){
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber("9999999999");
        phoneNumber.setAccountId(100001);
        phoneNumber.setId(100002);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("7777777777");
        phoneNumber2.setAccountId(100005);
        phoneNumber2.setId(100008);

        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setNumber("7777777777");
        phoneNumber3.setAccountId(100019);
        phoneNumber3.setId(100026);

        ReflectionTestUtils.setField(phoneNumberService, "stopRequestsCacheName", "STOP-REQUESTS");
        ReflectionTestUtils.setField(phoneNumberService, "requestCountCacheName", "REQUEST-COUNT");
        ReflectionTestUtils.setField(phoneNumberService, "requestDailyTrackerCacheName", "REQUEST-DAILY-COUNTER");

        lenient().when(repository.findByNumber("9999999999")).thenReturn(Optional.of(phoneNumber));
        lenient().when(repository.findByNumber("7777777777")).thenReturn(Optional.of(phoneNumber2));
        lenient().when(repository.findByNumber("8888888888")).thenReturn(Optional.of(phoneNumber3));
        lenient().when(cacheService.getItem("STOP-REQUESTS", "8888888888" )).thenReturn("2222222222");
        lenient().when(cacheService.getItem("REQUEST-COUNT", "9999999999" )).thenReturn("51");
        lenient().when(cacheService.getItem("REQUEST-COUNT", "7777777777" )).thenReturn("27");
        lenient().when(cacheService.hasKey("REQUEST-DAILY-COUNTER", "9999999999")).thenReturn(true);
        lenient().when(cacheService.hasKey("REQUEST-DAILY-COUNTER", "7777777777")).thenReturn(true);
    }
    @Test
    void shouldThrowExceptionIfFromIsNull(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setTo("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'from' is required";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfTosNull(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'to' is required";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfTextIsNull(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setTo("12324242");
        request.setFrom("23343523");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'text' is required";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfFromIsInvalid(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("124");
        request.setTo("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'from' is invalid";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfToIsInvalid(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("12423442");
        request.setTo("12324242343234353534232435345322422");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'to' is invalid";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfTextIsInvalid(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("12423442");
        request.setTo("1234353423");
        request.setText("HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "'text' is invalid";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfFromIsNotFoundWhenInbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("00000000");
        request.setTo("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            var response = phoneNumberService.logInboundSms(request);
        });

        String expectedMessage = "not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldPassIfFromIsFoundWhenInbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("1234232424");
        request.setTo("9999999999");
        request.setText("Hello");

        var responseEntity = phoneNumberService.logInboundSms(request);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void shouldThrowExceptionIfToIsNotFoundWhenOutbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("00000000");
        request.setTo("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            var response = phoneNumberService.logOutboundSms(request);
        });

        String expectedMessage = "not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldPassIfToIsFoundWhenOutbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("1234232424");
        request.setTo("7777777777");
        request.setText("Hello");

        var responseEntity = phoneNumberService.logInboundSms(request);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void shouldThrowExceptionIfCountGreaterThanLimitWhenOutbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("9999999999");
        request.setTo("12324242");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logOutboundSms(request);
        });

        String expectedMessage = "limit";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldPassIfCountLessThanLimitWhenOutbound(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("7777777777");
        request.setTo("12324242");
        request.setText("Hello");

        var response = phoneNumberService.logOutboundSms(request);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldThrowExceptionIfBlockedBySTOPRequest(){
        SmsRequestDTO request = new SmsRequestDTO();

        request.setFrom("8888888888");
        request.setTo("2222222222");
        request.setText("Hello");

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            var response = phoneNumberService.logOutboundSms(request);
        });

        String expectedMessage = "blocked by STOP request";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
