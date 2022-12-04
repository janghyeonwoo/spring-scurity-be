package com.example.springscuritybe.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Table(name = "authority")
@Entity
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
