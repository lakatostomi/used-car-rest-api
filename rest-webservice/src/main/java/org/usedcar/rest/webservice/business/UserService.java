package org.usedcar.rest.webservice.business;

import org.usedcar.rest.webservice.dto.RegistrationDTO;
import org.usedcar.rest.webservice.model.User;

public interface UserService {

    User findByEmail(String email);
    void saveUser(RegistrationDTO registrationDTO);
    void logoutUser();
}
