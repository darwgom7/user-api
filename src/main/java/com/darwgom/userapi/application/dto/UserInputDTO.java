package com.darwgom.userapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO {
    private String name;
    private String email;
    private String password;
    private Set<PhoneDTO> phones;
}
