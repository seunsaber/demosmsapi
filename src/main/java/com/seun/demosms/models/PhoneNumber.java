package com.seun.demosms.models;

import lombok.Data;

@Data
public class PhoneNumber {
    private Integer id;
    private String number;
    private Integer accountId;
}
