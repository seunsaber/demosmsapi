package com.seun.demosms.services;

import com.seun.demosms.dtos.ResponseDTO;
import com.seun.demosms.dtos.SmsRequestDTO;
import com.seun.demosms.exceptions.BadRequestException;
import com.seun.demosms.exceptions.ResourceNotFoundException;
import com.seun.demosms.models.PhoneNumber;
import com.seun.demosms.repositories.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneNumberService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private PhoneNumberRepository repository;

    @Value("${stop.requests.cacheName}")
    private String stopRequestsCacheName;

    @Value("${request.count.cacheName}")
    private String requestCountCacheName;

    @Value("${request.dailyTracker.cacheName}")
    private String requestDailyTrackerCacheName;

    public ResponseEntity<ResponseDTO> logInboundSms(SmsRequestDTO request){
        validate(request);

        Optional<PhoneNumber> optionalPhoneNumber = repository.findByNumber(request.getTo());

        optionalPhoneNumber.orElseThrow(() -> new ResourceNotFoundException("to parameter not found"));

        if(request.getText().equals("STOP") || request.getText().equals("STOP\n")
                || request.getText().equals("STOP\r") || request.getText().equals("STOP\r\n")){
            cacheService.addToCache(stopRequestsCacheName, request.getFrom(), request.getTo()); //4 hours to leave
        }

        return new ResponseEntity<>(new ResponseDTO("inbound sms ok",""), HttpStatus.OK);

    }

    public ResponseEntity<ResponseDTO> logOutboundSms(SmsRequestDTO request){
        validate(request);

        Optional<PhoneNumber> optionalPhoneNumber = repository.findByNumber(request.getFrom());

        optionalPhoneNumber.orElseThrow(() -> new ResourceNotFoundException("from parameter not found"));

        String to = cacheService.getItem(stopRequestsCacheName, request.getFrom());
        if(to != null && to.equals(request.getTo()))
            throw new BadRequestException(String.format("sms from %s to %s blocked by STOP request", request.getFrom(), request.getTo()));


        if(cacheService.hasKey(requestDailyTrackerCacheName, request.getFrom())){
            //if key exist in 24hour cache, fetch value from counter cache
            int count = Integer.valueOf(cacheService.getItem(requestCountCacheName, request.getFrom()));

            if(count > 50)
                throw new BadRequestException("limit reached for from "+request.getFrom());

            count = count + 1;
            //increment count in counter cache
            cacheService.addToCache(requestCountCacheName, request.getFrom(), String.valueOf(count));
        }else{
            //create new entry in 24hour cache and counter cache
            cacheService.addToCache(requestDailyTrackerCacheName, request.getFrom(), "");//24 hours to leave
            cacheService.addToCache(requestCountCacheName, request.getFrom(), String.valueOf(0));
        }


        return new ResponseEntity<>(new ResponseDTO("outbound sms ok",""), HttpStatus.OK);

    }

    private void validate(SmsRequestDTO request){
        if(request.getFrom() == null || request.getFrom().isBlank())
            throw new BadRequestException("'from' is required");
        if(request.getTo() == null || request.getTo().isBlank())
            throw new BadRequestException("'to' is required");
        if(request.getText() == null || request.getText().isBlank())
            throw new BadRequestException("'text' is required");


        if(request.getFrom().length() < 6 || request.getFrom().length() > 16)
            throw new BadRequestException("'from' is invalid");
        if(request.getTo().length() < 6 || request.getTo().length() > 16)
            throw new BadRequestException("'to' is invalid");
        if(request.getText().length() < 1 || request.getText().length() > 120)
            throw new BadRequestException("'text' is invalid");
    }

}
