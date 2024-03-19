package app.confectionery.user.service;

import app.confectionery.registration.model.RegistrationUserDTO;
import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public boolean registerUser(RegistrationUserDTO unregisteredUser) {
        if (userRepository.existsUserByUsernameOrEmail(unregisteredUser.getUsername(), unregisteredUser.getEmail())) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(unregisteredUser.getUsername());
        newUser.setFirstName(unregisteredUser.getFirstName());
        newUser.setLastName(unregisteredUser.getLastName());
        newUser.setEmail(unregisteredUser.getEmail());
        newUser.setPassword(unregisteredUser.getPassword());
        userRepository.save(newUser);

        return true;
    }


}
