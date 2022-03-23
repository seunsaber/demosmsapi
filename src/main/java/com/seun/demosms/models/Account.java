package com.seun.demosms.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Account {
    @Id
    private Integer id;
    private String authId;
    private String username;
}
