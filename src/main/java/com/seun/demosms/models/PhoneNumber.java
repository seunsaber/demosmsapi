package com.seun.demosms.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class PhoneNumber {
    @Id
    private Integer id;
    private String number;
    private Integer accountId;
}
