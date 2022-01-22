package com.iban.validator.repository;

import com.iban.validator.entity.IBANEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is a JPA Repository interface to communicate with database
 * @author Soumen Banerjee
 */
public interface IBANRepository extends JpaRepository<IBANEntity, Long> { }
