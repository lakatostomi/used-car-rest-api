package org.usedcar.rest.webservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.usedcar.rest.webservice.validator.ValidPassword;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RegistrationDTO {

    @Size(max = 50, message = "Name max size is 50 character!")
    @NotBlank(message = "Name can not be empty")
    private String name;
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}$", message = "Your email format is not supported!")
    @NotBlank(message = "Email can not be empty!")
    private String email;
    @ValidPassword
    @NotBlank(message = "Password can not be empty")
    private String password;

}
