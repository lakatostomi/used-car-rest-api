package org.usedcar.rest.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.usedcar.rest.webservice.model.CarAd;

import java.util.List;
@Repository
public interface CarAdRepository extends JpaRepository<CarAd, Integer> {

    List<CarAd> findCarAdByBrandOfCarIgnoreCaseAndCarTypeContaining(String brandOfCar, String carType);
}
