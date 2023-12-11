package com.darwgom.userapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInputDTO {
    private String number;
    private String citycode;
    private String countrycode;
}