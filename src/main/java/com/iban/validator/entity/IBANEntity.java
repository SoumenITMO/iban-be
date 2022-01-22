package com.iban.validator.entity;

import lombok.Data;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * This is a Entity class to handle IBAN data from database table
 * @author Soumen Banerjee
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "iban")
public class IBANEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iban", nullable = false, unique = true)
    private String iban;

    @Column(name = "status", nullable = false)
    private String status;
}
