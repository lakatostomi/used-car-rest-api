package org.usedcar.rest.webservice.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarAdDTO {
    @NotBlank(message = "The brand of the car can not be empty!")
    @Size(max = 20, message = "Max 20 characters allowed")
    private String brandOfCar;
    @NotBlank(message = "The type of the car can not be empty!")
    @Size(max = 20, message = "Max 20 characters allowed")
    private String carType;
    @NotBlank(message = "The description of the car can not be empty!")
    @Size(max = 200, message = "Max 200 characters allowed")
    private String description;
    @Positive(message = "The price has to be positive")
    @Max(value = 999999999, message = "The price can contain only 10 digits")
    private Long price;
}
