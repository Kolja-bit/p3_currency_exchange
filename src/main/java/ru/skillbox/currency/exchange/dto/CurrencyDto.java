package ru.skillbox.currency.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private Long id;

    private String name;

    private Long nominal;

    private Double value;

    private Long isoNumCode;

    private String isoCharCode;
}