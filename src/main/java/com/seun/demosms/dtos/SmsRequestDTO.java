package com.seun.demosms.dtos;

import lombok.Data;

@Data
public class SmsRequestDTO {
    private String from;
    private String to;
    private String text;
}
