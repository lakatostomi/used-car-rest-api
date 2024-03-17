package org.usedcar.rest.webservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CarSearchDTO {

    @NotBlank(message = "Brand of the Car can not be empty")
    @Size(max = 50, message = "Max 50 characters allowed")
    private String brandOfCar;
    @NotBlank(message = "Type of the Car can not be empty")
    @Size(max = 20, message = "Max 20 characters allowed")
    private String carType;
}
