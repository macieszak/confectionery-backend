package app.confectionery.user.service;

import app.confectionery.registration.model.RegistrationUserDTO;

public interface UserService {

    boolean registerUser(RegistrationUserDTO unregisteredUser);


}
