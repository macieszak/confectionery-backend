package app.confectionery.authorization.service;

import app.confectionery.user.model.User;
import app.confectionery.authorization.model.UserRegistrationDto;

public interface RegistrationUserService {
    User registerUser(UserRegistrationDto userRegistrationDto);
}
