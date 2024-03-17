package org.usedcar.rest.webservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.usedcar.rest.webservice.model.CarAd;
import org.usedcar.rest.webservice.model.User;
import org.usedcar.rest.webservice.repository.CarAdRepository;
import org.usedcar.rest.webservice.repository.UserRepository;

import java.util.List;

@Component
@Profile("dev")
@Slf4j
public class InitDataOnStartup implements ApplicationListener<ApplicationContextEvent> {

    private final UserRepository userRepository;
    private final CarAdRepository carAdRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDataOnStartup(UserRepository userRepository, CarAdRepository carAdRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.carAdRepository = carAdRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        log.info("Initializing data has started...");
        User tamas = saveUser(new User("Tamas", "tamas@mail.com", passwordEncoder.encode("AAbb11CC")));
        User peter = saveUser(new User("Peter", "peter@mail.com", passwordEncoder.encode("AAbb12CC")));
        CarAd opel = new CarAd("Opel", "Astra H 1.9 DTI", "This is an Opel car", 1000000L, tamas);
        CarAd opelG = new CarAd("Opel", "Astra G 1.6", "This is an Astra G car", 800000L, tamas);
        CarAd ford = new CarAd("Ford", "Focus GT", "This is a Ford car", 4000000L, peter);
        CarAd suzki = new CarAd("Suziki", "Swift Sedan 1.3", "This is a Suzuki car", 500000L, peter);
        saveAds(List.of(opel, opelG, ford, suzki));
        log.info("...data initializing has finished!");
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private void saveAds(List<CarAd> carAdList) {
        carAdRepository.saveAll(carAdList);
    }
}
