package org.usedcar.rest.webservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "The email can not be empty")
    private String email;
    @NotBlank(message = "The password can not be empty")
    private String password;
}
