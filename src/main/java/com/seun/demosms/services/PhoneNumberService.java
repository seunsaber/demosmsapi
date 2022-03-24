package com.seun.demosms.services;

import com.seun.demosms.dtos.ResponseDTO;
import com.seun.demosms.dtos.SmsRequestDTO;
import com.seun.demosms.exceptions.BadRequestException;
import com.seun.demosms.exceptions.ResourceNotFoundException;
import com.seun.demosms.models.PhoneNumber;
import com.seun.demosms.repositories.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneNumberService {

    @Autowired
    private PhoneNumberRepository repository;

    public ResponseEntity<ResponseDTO> logInboundSms(SmsRequestDTO request){
        validate(request);

        Optional<PhoneNumber> optionalPhoneNumber = repository.findByNumber(request.getTo());

        optionalPhoneNumber.orElseThrow(() -> new ResourceNotFoundException("to parameter not found"));

        if(request.getText().equals("STOP") || request.getText().equals("STOP\\n")
                || request.getText().equals("STOP\\r") || request.getText().equals("STOP\\r\\n")){
            //TODO: store from and to pair in cache, expire after 4 hours.
        }else{
            //store normally
        }

        return new ResponseEntity<>(new ResponseDTO("inbound sms ok",""), HttpStatus.OK);

    }

    public ResponseEntity<ResponseDTO> logOutboundSms(SmsRequestDTO request){
        validate(request);

        Optional<PhoneNumber> optionalPhoneNumber = repository.findByNumber(request.getFrom());

        optionalPhoneNumber.orElseThrow(() -> new ResourceNotFoundException("from parameter not found"));

        //if (to and from match any pair in cache){
        //  return error
        // }

        //do not allow more than 50 API requests from same from in 24 hours from first use, return error
        //reset after 24 hours


        if(request.getText().equals("STOP") || request.getText().equals("STOP\\n")
                || request.getText().equals("STOP\\r") || request.getText().equals("STOP\\r\\n")){
            //TODO: store from and to pair in cache, expire after 4 hours.
        }

        return new ResponseEntity<>(new ResponseDTO("inbound sms ok",""), HttpStatus.OK);

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
