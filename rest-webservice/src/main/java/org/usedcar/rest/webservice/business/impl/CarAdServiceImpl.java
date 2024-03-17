package org.usedcar.rest.webservice.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usedcar.rest.webservice.business.CarAdService;
import org.usedcar.rest.webservice.dto.CarAdDTO;
import org.usedcar.rest.webservice.dto.CarSearchDTO;
import org.usedcar.rest.webservice.exception.ResourceNotFoundException;
import org.usedcar.rest.webservice.exception.ResourceDeletingIsNotAllowedException;
import org.usedcar.rest.webservice.model.CarAd;
import org.usedcar.rest.webservice.model.User;
import org.usedcar.rest.webservice.repository.CarAdRepository;
import org.usedcar.rest.webservice.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CarAdServiceImpl implements CarAdService {

    private final CarAdRepository carAdRepository;
    private final UserRepository userRepository;

    public CarAdServiceImpl(CarAdRepository carAdRepository, UserRepository userRepository) {
        this.carAdRepository = carAdRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Integer saveCarAd(CarAdDTO carAdDTO) {
        log.info("Saving new car ad {}", carAdDTO);
        User user = getUser();
        return carAdRepository.save(new CarAd(carAdDTO.getBrandOfCar(),
                carAdDTO.getCarType(),
                carAdDTO.getDescription(),
                carAdDTO.getPrice(),
                user)).getId();
    }

    @Override
    public void deleteCarAd(int id) {
        log.info("Deleting Car Ad with id: {}", id);
        User user = getUser();
        CarAd carAd = carAdRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("There is no Car Ad with this id: " + id));
        if (Objects.equals(carAd.getUser().getId(), user.getId())) {
            carAdRepository.delete(carAd);
        } else {
            throw new ResourceDeletingIsNotAllowedException("You do not have permission to delete this Car Ad!");
        }

    }

    @Override
    public List<CarAd> searchCarAd(CarSearchDTO carSearchDTO) {
        log.info("Searching for Car Ad with values of {}", carSearchDTO);
        List<CarAd> carAdList = carAdRepository.findCarAdByBrandOfCarIgnoreCaseAndCarTypeContaining(carSearchDTO.getBrandOfCar(), carSearchDTO.getCarType());
        if (carAdList.isEmpty()) {
            throw new ResourceNotFoundException("Your search has no result!");
        }
        return carAdList;
    }

    @Override
    public CarAd findCarAdById(int id) {
        log.info("Searching for Car Ad with id: {}", id);
        return carAdRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("There is no Car Ad with this id: " + id));
    }

    private User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username);
    }
}
