package com.darwgom.userapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    private UUID id;
    private String number;
    private String citycode;
    private String countrycode;
}
