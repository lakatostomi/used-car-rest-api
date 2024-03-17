package org.usedcar.rest.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class CarAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    private String brandOfCar;
    private String carType;
    private String description;
    private Long price;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public CarAd(String brandOfCar, String carType, String description, Long price, User user) {
        this.brandOfCar = brandOfCar;
        this.carType = carType;
        this.description = description;
        this.price = price;
        this.user = user;
    }
}
