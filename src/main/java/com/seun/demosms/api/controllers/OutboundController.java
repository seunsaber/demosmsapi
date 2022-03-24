package com.seun.demosms.api.controllers;

import com.seun.demosms.dtos.ResponseDTO;
import com.seun.demosms.dtos.SmsRequestDTO;
import com.seun.demosms.services.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outbound")
public class OutboundController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    @PostMapping("/sms")
    public ResponseEntity<ResponseDTO> sms(@RequestBody SmsRequestDTO request){
        return phoneNumberService.logInboundSms(request);
    }
}
