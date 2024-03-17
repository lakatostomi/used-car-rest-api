package org.usedcar.rest.webservice.business;

import org.usedcar.rest.webservice.dto.CarAdDTO;
import org.usedcar.rest.webservice.dto.CarSearchDTO;
import org.usedcar.rest.webservice.model.CarAd;

import java.util.List;

public interface CarAdService {

    Integer saveCarAd(CarAdDTO carAdDTO);
    void deleteCarAd(int id);
    List<CarAd> searchCarAd(CarSearchDTO carSearchDTO);
    CarAd findCarAdById(int id);
}
